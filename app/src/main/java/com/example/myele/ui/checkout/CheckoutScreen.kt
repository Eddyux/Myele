package com.example.myele.ui.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.myele.data.CartManager
import com.example.myele.ui.components.ProductImage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CheckoutScreen(navController: NavController, repository: com.example.myele.data.DataRepository) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var deliveryMethod by remember { mutableStateOf("外卖配送") }
    var deliveryTimeType by remember { mutableStateOf("立即送出") } // "立即送出" or "预约配送"
    var selectedDeliveryDate by remember { mutableStateOf("今日") }
    var selectedDeliveryTimeSlot by remember { mutableStateOf("") }
    var showDeliveryTimeDialog by remember { mutableStateOf(false) }
    var showCouponDialog by remember { mutableStateOf(false) }
    var selectedCoupon by remember { mutableStateOf<com.example.myele.model.Coupon?>(null) }
    var paymentMethod by remember { mutableStateOf("微信支付") }
    var showPaymentDialog by remember { mutableStateOf(false) }

    // Get checkout products from CartManager
    val checkoutProducts = remember { CartManager.getCheckoutProducts() }
    val subtotal = remember { CartManager.getSubtotal() }

    // Group products by restaurant
    val productsByRestaurant = remember {
        checkoutProducts.groupBy { it.first.restaurantName }
    }

    // Get restaurant IDs from products
    val restaurantIds = remember {
        checkoutProducts.map { it.first.restaurantId }.distinct()
    }

    // Load available coupons
    val allCoupons = remember { repository.getCoupons() }
    val deliveryFee = 5.0 // 配送费
    val availableCoupons = remember(subtotal, restaurantIds) {
        allCoupons.filter { coupon ->
            coupon.status == com.example.myele.model.CouponStatus.AVAILABLE &&
            (coupon.applicableRestaurants.isEmpty() ||
             restaurantIds.any { it in coupon.applicableRestaurants }) &&
            (coupon.minOrderAmount == null || subtotal >= (coupon.minOrderAmount ?: 0.0))
        }.sortedByDescending {
            it.calculateDiscount(subtotal, deliveryFee)
        }
    }

    // Calculate discount
    val couponDiscount = selectedCoupon?.calculateDiscount(subtotal, deliveryFee) ?: 0.0

    // 记录进入结算页面
    LaunchedEffect(Unit) {
        val pageInfoMap = mutableMapOf<String, Any>(
            "total_amount" to subtotal,
            "product_count" to checkoutProducts.size
        )

        // 如果有searchKey，也记录下来
        val searchKey = CartManager.getSearchKeyword()
        if (searchKey != null) {
            pageInfoMap["search_query"] = searchKey
        }

        com.example.myele.utils.ActionLogger.logAction(
            context = context,
            action = "enter_checkout",
            page = "checkout",
            pageInfo = pageInfoMap
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标题栏
        TopBar(onBackClicked = { navController.popBackStack() })

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // 配送方式选择
            item {
                DeliveryMethodSection(
                    selectedMethod = deliveryMethod,
                    onMethodChanged = { deliveryMethod = it }
                )
            }

            // 收货地址
            item {
                AddressSection()
            }

            // 配送时间
            item {
                DeliveryTimeSection(
                    selectedTimeType = deliveryTimeType,
                    selectedDate = selectedDeliveryDate,
                    selectedTimeSlot = selectedDeliveryTimeSlot,
                    onImmediateClicked = {
                        deliveryTimeType = "立即送出"
                        selectedDeliveryDate = "今日"
                        selectedDeliveryTimeSlot = ""
                    },
                    onScheduleClicked = {
                        showDeliveryTimeDialog = true
                    }
                )
            }

            // 订单商品 - 按商家分组显示
            productsByRestaurant.forEach { (restaurantName, products) ->
                item {
                    OrderItemsSection(
                        restaurantName = restaurantName,
                        products = products
                    )
                }
            }

            // 优惠券选择
            item {
                CouponSelectionSection(
                    selectedCoupon = selectedCoupon,
                    onClick = { showCouponDialog = true }
                )
            }

            // 订单费用明细
            item {
                OrderFeesSection(
                    subtotal = subtotal,
                    couponDiscount = couponDiscount
                )
            }

            // 备注和餐具
            item {
                OrderOptionsSection()
            }

            // 支付方式
            item {
                PaymentMethodSection(
                    selectedMethod = paymentMethod,
                    onClick = { showPaymentDialog = true }
                )
            }
        }

        // 底部提交订单
        BottomSubmitBar(
            total = subtotal + 2.0 + 5.0 - couponDiscount,
            onSubmit = {
                // 创建订单并导航到支付成功页面
                val orderId = "order_${System.currentTimeMillis()}"
                val totalAmount = subtotal + 2.0 + 5.0 - couponDiscount

                // 检查是否从搜索进入（用于任务14检测）
                val searchKey = CartManager.getSearchKeyword()
                val hasKFCProducts = checkoutProducts.any { it.first.restaurantName.contains("肯德基") }

                // 只有从搜索页面进入肯德基商家时才记录
                if (hasKFCProducts && searchKey != null) {
                    // 检查是否使用了优惠券
                    val usedCoupon = selectedCoupon != null
                    // 检查是否选择了最大的优惠券（第一个就是最大的，因为已排序）
                    val selectedMaxCoupon = if (availableCoupons.isNotEmpty() && selectedCoupon != null) {
                        selectedCoupon == availableCoupons.first()
                    } else {
                        false
                    }

                    // 记录肯德基下单流程（用于任务14检测）
                    com.example.myele.utils.ActionLogger.logAction(
                        context = context,
                        action = "complete_order",
                        page = "checkout",
                        pageInfo = mapOf("restaurant" to "肯德基"),
                        extraData = mapOf(
                            "search_query" to searchKey,  // 使用实际的搜索关键词
                            "added_to_cart" to true,
                            "used_coupon" to usedCoupon,
                            "selected_max_coupon" to selectedMaxCoupon,
                            "payment_success" to true
                        )
                    )
                }

                // 记录通用的完成订单（用于任务19等）
                val fromPage = CartManager.getFromPage()
                if (fromPage != null) {
                    val extraDataMap = mutableMapOf<String, Any>(
                        "from_page" to fromPage,
                        "payment_success" to true
                    )

                    // 如果是预约配送，记录预约信息
                    if (deliveryTimeType == "预约配送") {
                        extraDataMap["delivery_type"] = "scheduled"
                        extraDataMap["delivery_date"] = selectedDeliveryDate
                        extraDataMap["delivery_time_slot"] = selectedDeliveryTimeSlot
                    } else {
                        extraDataMap["delivery_type"] = "immediate"
                    }

                    com.example.myele.utils.ActionLogger.logAction(
                        context = context,
                        action = "complete_order",
                        page = "checkout",
                        pageInfo = mapOf(),
                        extraData = extraDataMap
                    )
                }

                // 导航到支付成功页面
                navController.navigate(
                    com.example.myele.navigation.Screen.PaymentSuccess.createRoute(
                        orderId = orderId,
                        amount = totalAmount,
                        paymentMethod = paymentMethod
                    )
                )
            }
        )
    }

    // 优惠券选择弹窗
    if (showCouponDialog) {
        CouponSelectionDialog(
            availableCoupons = availableCoupons,
            selectedCoupon = selectedCoupon,
            subtotal = subtotal,
            onCouponSelected = { coupon ->
                selectedCoupon = coupon
                showCouponDialog = false
            },
            onDismiss = { showCouponDialog = false }
        )
    }

    // 支付方式选择弹窗
    if (showPaymentDialog) {
        PaymentMethodDialog(
            selectedMethod = paymentMethod,
            onMethodSelected = { method ->
                paymentMethod = method
                showPaymentDialog = false
            },
            onDismiss = { showPaymentDialog = false }
        )
    }

    // 配送时间选择弹窗
    if (showDeliveryTimeDialog) {
        DeliveryTimeSelectionDialog(
            onTimeSelected = { date, timeSlot ->
                deliveryTimeType = "预约配送"
                selectedDeliveryDate = date
                selectedDeliveryTimeSlot = timeSlot
                showDeliveryTimeDialog = false

                // 记录预约配送
                com.example.myele.utils.ActionLogger.logAction(
                    context = context,
                    action = "select_scheduled_delivery",
                    page = "checkout",
                    pageInfo = mapOf(
                        "delivery_date" to date,
                        "delivery_time_slot" to timeSlot
                    )
                )
            },
            onDismiss = { showDeliveryTimeDialog = false }
        )
    }
}

@Composable
fun TopBar(onBackClicked: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.Black
                )
            }
            Text(
                text = "确认订单",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DeliveryMethodSection(selectedMethod: String, onMethodChanged: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("外卖配送", "到店自取").forEach { method ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (selectedMethod == method) Color(0xFF00BFFF) else Color(0xFFF5F5F5),
                    modifier = Modifier.clickable { onMethodChanged(method) }
                ) {
                    Text(
                        text = method,
                        fontSize = 14.sp,
                        color = if (selectedMethod == method) Color.White else Color.Black,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddressSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFF00BFFF),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "华中师范大学元宝山公寓二期六栋",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "于骁 138****8888",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun DeliveryTimeSection(
    selectedTimeType: String,
    selectedDate: String,
    selectedTimeSlot: String,
    onImmediateClicked: () -> Unit,
    onScheduleClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("配送时间", fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))

            // 两个按钮：立即送出 和 预约配送
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 立即送出按钮
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onImmediateClicked),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selectedTimeType == "立即送出") Color(0xFF00BFFF) else Color(0xFFE0E0E0)
                    ),
                    color = if (selectedTimeType == "立即送出") Color(0xFFF0F8FF) else Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedTimeType == "立即送出") {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF00BFFF),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "立即送出",
                            fontSize = 14.sp,
                            color = if (selectedTimeType == "立即送出") Color(0xFF00BFFF) else Color.Black
                        )
                    }
                }

                // 预约配送按钮
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onScheduleClicked),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selectedTimeType == "预约配送") Color(0xFF00BFFF) else Color(0xFFE0E0E0)
                    ),
                    color = if (selectedTimeType == "预约配送") Color(0xFFF0F8FF) else Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedTimeType == "预约配送") {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF00BFFF),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "预约配送",
                            fontSize = 14.sp,
                            color = if (selectedTimeType == "预约配送") Color(0xFF00BFFF) else Color.Black
                        )
                    }
                }
            }

            // 显示选中的预约时间（在按钮下方）
            if (selectedTimeType == "预约配送" && selectedTimeSlot.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "已选择：$selectedDate $selectedTimeSlot",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun OrderItemsSection(
    restaurantName: String,
    products: List<Pair<com.example.myele.model.Product, Int>>
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = restaurantName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 商品列表
            products.forEach { (product, quantity) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 商品图片
                    ProductImage(
                        productId = product.productId,
                        productName = product.name,
                        modifier = Modifier.size(50.dp),
                        size = 50.dp,
                        cornerRadius = 6.dp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // 商品名称和数量
                    Text(
                        text = "${product.name} x$quantity",
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    // 价格
                    Text(
                        text = "¥%.1f".format(product.price * quantity),
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun OrderFeesSection(subtotal: Double, couponDiscount: Double) {
    val packagingFee = 2.0
    val deliveryFee = 5.0
    val total = subtotal + packagingFee + deliveryFee - couponDiscount

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FeeItem("商品总额", "¥%.1f".format(subtotal))
            FeeItem("打包费", "¥%.1f".format(packagingFee))
            FeeItem("配送费", "¥%.1f".format(deliveryFee))
            if (couponDiscount > 0) {
                FeeItem("优惠券", "-¥%.1f".format(couponDiscount), Color(0xFFFF3366))
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("小计", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "¥%.1f".format(total),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF3366)
                )
            }
        }
    }
}

@Composable
fun FeeItem(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, color = valueColor)
    }
}

@Composable
fun OrderOptionsSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OptionItem("备注", "口味、偏好等要求")
            Divider()
            OptionItem("餐具", "不需要餐具")
            Divider()
            OptionItem("发票", "不需要发票")
        }
    }
}

@Composable
fun OptionItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = Color.Black)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, fontSize = 14.sp, color = Color.Gray)
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun PaymentMethodSection(selectedMethod: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("支付方式", fontSize = 14.sp, color = Color.Black)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(selectedMethod, fontSize = 14.sp, color = Color(0xFF00BFFF))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BottomSubmitBar(total: Double, onSubmit: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("合计:", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "¥%.1f".format(total),
                    fontSize = 20.sp,
                    color = Color(0xFFFF3366),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3366)),
                modifier = Modifier.height(48.dp)
            ) {
                Text("提交订单", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun CouponSelectionSection(
    selectedCoupon: com.example.myele.model.Coupon?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = null,
                    tint = Color(0xFFFF3366),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("店铺活动/券", fontSize = 14.sp, color = Color.Black)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedCoupon != null) {
                    val subtotal = CartManager.getSubtotal()
                    val deliveryFee = 5.0
                    val discountAmount = selectedCoupon.calculateDiscount(subtotal, deliveryFee)
                    Text(
                        text = "-¥%.0f".format(discountAmount),
                        fontSize = 14.sp,
                        color = Color(0xFFFF3366),
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text("请选择", fontSize = 14.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun CouponSelectionDialog(
    availableCoupons: List<com.example.myele.model.Coupon>,
    selectedCoupon: com.example.myele.model.Coupon?,
    subtotal: Double,
    onCouponSelected: (com.example.myele.model.Coupon?) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF5F5F5),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
                Text("选择饿了么红包", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("兑换码", fontSize = 14.sp, color = Color(0xFF00BFFF))
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            ) {
                item {
                    Text(
                        "饿了么红包  可选1张",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(availableCoupons.size) { index ->
                    val coupon = availableCoupons[index]
                    CouponItem(
                        coupon = coupon,
                        isSelected = coupon.couponId == selectedCoupon?.couponId,
                        subtotal = subtotal,
                        isRecommended = index == 0,
                        onSelect = { onCouponSelected(coupon) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // 不想爆涨选项
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCouponSelected(null) },
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("不想爆涨？可直接使用", fontSize = 14.sp, color = Color.Gray)
                            RadioButton(
                                selected = selectedCoupon == null,
                                onClick = { onCouponSelected(null) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (selectedCoupon != null) {
                        val deliveryFee = 5.0
                        "已选1张，可减 ¥%.0f".format(selectedCoupon.calculateDiscount(subtotal, deliveryFee))
                    } else {
                        "确定"
                    },
                    fontSize = 16.sp
                )
            }
        }
    )
}

@Composable
fun PaymentMethodDialog(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text("选择支付方式", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("微信支付", "支付宝").forEach { method ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMethodSelected(method) },
                        color = if (selectedMethod == method) Color(0xFFF0F8FF) else Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = method,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            if (selectedMethod == method) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF00BFFF)
                                )
                            }
                        }
                    }
                    if (method != "支付宝") {
                        Divider()
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
            ) {
                Text("确定")
            }
        }
    )
}

@Composable
fun CouponItem(
    coupon: com.example.myele.model.Coupon,
    isSelected: Boolean,
    subtotal: Double,
    isRecommended: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF00BFFF)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧金额
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                when (coupon.type) {
                    com.example.myele.model.CouponType.REDUCTION -> {
                        Text(
                            text = "¥${coupon.discountAmount?.toInt() ?: 0}",
                            fontSize = 32.sp,
                            color = Color(0xFFFF3366),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "满${coupon.minOrderAmount?.toInt() ?: 0}可用",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    com.example.myele.model.CouponType.DISCOUNT -> {
                        val discountPercent = ((1 - (coupon.discountRate ?: 1.0)) * 10).toInt()
                        Text(
                            text = "${discountPercent}折",
                            fontSize = 28.sp,
                            color = Color(0xFFFF3366),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "满${coupon.minOrderAmount?.toInt() ?: 0}可用",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    com.example.myele.model.CouponType.DELIVERY_FREE -> {
                        Text(
                            text = "免配送",
                            fontSize = 24.sp,
                            color = Color(0xFFFF3366),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "全场可用",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    com.example.myele.model.CouponType.SPECIAL -> {
                        Text(
                            text = "特价",
                            fontSize = 28.sp,
                            color = Color(0xFFFF3366),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = coupon.description ?: "",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            maxLines = 2
                        )
                    }
                    else -> {
                        Text(
                            text = "¥${coupon.discountAmount?.toInt() ?: 0}",
                            fontSize = 32.sp,
                            color = Color(0xFFFF3366),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "满${coupon.minOrderAmount?.toInt() ?: 0}可用",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 中间信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = coupon.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                val validUntil = java.text.SimpleDateFormat("今日HH:mm到期", java.util.Locale.CHINA)
                    .format(coupon.validUntil)
                Text(
                    text = validUntil,
                    fontSize = 12.sp,
                    color = Color(0xFFFF3366)
                )
                if (coupon.description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = coupon.description,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // 右侧选择按钮和推荐标签
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isRecommended) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFF9800)
                    ) {
                        Text(
                            "推荐",
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00BFFF))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryTimeSelectionDialog(
    onTimeSelected: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf("今日（周二）") }
    var selectedTimeSlot by remember { mutableStateOf("") }

    // 生成日期列表
    val dateList = remember {
        listOf(
            "今日（周二）",
            "明日（周三）",
            "11-06（周四）",
            "11-07（周五）",
            "11-08（周六）",
            "11-09（周日）",
            "11-10（周一）"
        )
    }

    // 生成所有时间段列表（今日+明日及之后的时间段）
    val allTimeSlots = remember {
        listOf(
            // 今日时间段
            "今日" to listOf(
                "尽快送达" to "1元配送费",
                "22:25-22:45" to "1元配送费",
                "22:45-23:05" to "1元配送费",
                "23:05-23:25" to "1元配送费",
                "23:25-23:45" to "1元配送费"
            ),
            // 明日及以后的时间段
            "明日" to listOf(
                "07:40-08:00" to "0元配送费",
                "08:00-08:20" to "0元配送费",
                "08:20-08:40" to "0元配送费",
                "08:40-09:00" to "0元配送费",
                "11:00-11:20" to "1元配送费",
                "11:20-11:40" to "1元配送费",
                "11:40-12:00" to "1元配送费",
                "12:00-12:20" to "1元配送费",
                "12:20-12:40" to "1元配送费",
                "12:40-13:00" to "1元配送费"
            )
        )
    }

    val timeSlots = remember(selectedDate) {
        val todaySlots = allTimeSlots[0].second
        val tomorrowSlots = allTimeSlots[1].second
        when {
            selectedDate.contains("今日") -> todaySlots + tomorrowSlots
            else -> tomorrowSlots
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "选择送达时间",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 左右分栏布局
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                // 左侧：日期列表
                LazyColumn(
                    modifier = Modifier
                        .weight(0.35f)
                        .fillMaxHeight()
                        .background(Color(0xFFF8F8F8)),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(dateList.size) { index ->
                        val date = dateList[index]
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDate = date },
                            color = if (selectedDate == date) Color.White else Color(0xFFF8F8F8)
                        ) {
                            Text(
                                text = date,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(1.dp))

                // 右侧：时间段列表
                LazyColumn(
                    modifier = Modifier
                        .weight(0.65f)
                        .fillMaxHeight()
                        .background(Color.White)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(timeSlots.size) { index ->
                        val (timeSlot, description) = timeSlots[index]
                        val displayText = when {
                            timeSlot == "尽快送达" -> "今日 $timeSlot"
                            selectedDate.contains("今日") && index < 5 -> "今日 $timeSlot"
                            selectedDate.contains("今日") && index >= 5 -> "明日 $timeSlot"
                            else -> "${selectedDate.take(5)} $timeSlot"
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedTimeSlot = timeSlot

                                    // 判断实际是今日还是明日的时间段
                                    val isToday = selectedDate.contains("今日") && index < 5
                                    val isTomorrow = selectedDate.contains("今日") && index >= 5

                                    // 根据选择的时间段自动切换左侧日期
                                    if (isTomorrow) {
                                        selectedDate = "明日（周三）"
                                    }

                                    // 确定实际日期
                                    val actualDate = when {
                                        isToday -> "今日"
                                        isTomorrow -> "明日"
                                        selectedDate.contains("明日") -> "明日"
                                        else -> selectedDate.take(5)
                                    }

                                    onTimeSelected(actualDate, timeSlot)
                                },
                            color = if (selectedTimeSlot == timeSlot) Color(0xFFF0F8FF) else Color.White
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = displayText,
                                        fontSize = 14.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = description,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                                if (selectedTimeSlot == timeSlot) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF00BFFF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

package com.example.myele_sim.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.myele_sim.data.CartManager

@Composable
fun CheckoutScreen(navController: NavController, repository: com.example.myele_sim.data.DataRepository) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var deliveryMethod by remember { mutableStateOf("外卖配送") }
    var deliveryTimeType by remember { mutableStateOf("立即送出") } // "立即送出" or "预约配送"
    var selectedDeliveryDate by remember { mutableStateOf("今日") }
    var selectedDeliveryTimeSlot by remember { mutableStateOf("") }
    var showDeliveryTimeDialog by remember { mutableStateOf(false) }
    var showCouponDialog by remember { mutableStateOf(false) }
    var selectedCoupon by remember { mutableStateOf<com.example.myele_sim.model.Coupon?>(null) }
    var paymentMethod by remember { mutableStateOf("微信支付") }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }

    // Get addresses and select default or first
    val addresses = remember { repository.getAddresses() }
    var selectedAddress by remember { mutableStateOf(addresses.firstOrNull()) }

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
            coupon.status == com.example.myele_sim.model.CouponStatus.AVAILABLE &&
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

        // 如果有searchKey,也记录下来
        val searchKey = CartManager.getSearchKeyword()
        if (searchKey != null) {
            pageInfoMap["search_query"] = searchKey
        }

        com.example.myele_sim.utils.ActionLogger.logAction(
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
                AddressSection(
                    selectedAddress = selectedAddress,
                    onClick = { showAddressDialog = true }
                )
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

                // 检查是否从搜索进入(用于任务14检测)
                val searchKey = CartManager.getSearchKeyword()
                val hasKFCProducts = checkoutProducts.any { it.first.restaurantName.contains("肯德基") }

                // 只有从搜索页面进入肯德基商家时才记录
                if (hasKFCProducts && searchKey != null) {
                    // 检查是否使用了优惠券
                    val usedCoupon = selectedCoupon != null
                    // 检查是否选择了最大的优惠券(第一个就是最大的,因为已排序)
                    val selectedMaxCoupon = if (availableCoupons.isNotEmpty() && selectedCoupon != null) {
                        selectedCoupon == availableCoupons.first()
                    } else {
                        false
                    }

                    // 记录肯德基下单流程(用于任务14检测)
                    com.example.myele_sim.utils.ActionLogger.logAction(
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

                // 记录通用的完成订单(用于任务19等)
                // 注意:如果已经记录了肯德基订单,则不重复记录通用订单
                val fromPage = CartManager.getFromPage()
                if (fromPage != null && !(hasKFCProducts && searchKey != null)) {
                    val extraDataMap = mutableMapOf<String, Any>(
                        "from_page" to fromPage,
                        "payment_success" to true
                    )

                    // 记录收货地址信息
                    selectedAddress?.let { address ->
                        extraDataMap["delivery_address_name"] = address.receiverName
                        extraDataMap["delivery_address_phone"] = address.receiverPhone
                        extraDataMap["delivery_address_street"] = address.street
                        extraDataMap["delivery_address_detail"] = address.detailAddress
                        extraDataMap["delivery_address_full"] = address.getFullAddress()
                        address.yuwei?.let { extraDataMap["delivery_address_yuwei"] = it }
                    }

                    // 如果是预约配送,记录预约信息
                    if (deliveryTimeType == "预约配送") {
                        extraDataMap["delivery_type"] = "scheduled"
                        extraDataMap["delivery_date"] = selectedDeliveryDate
                        extraDataMap["delivery_time_slot"] = selectedDeliveryTimeSlot
                    } else {
                        extraDataMap["delivery_type"] = "immediate"
                    }

                    com.example.myele_sim.utils.ActionLogger.logAction(
                        context = context,
                        action = "complete_order",
                        page = "checkout",
                        pageInfo = mapOf(),
                        extraData = extraDataMap
                    )
                }

                // 创建订单对象并保存到OrderManager
                selectedAddress?.let { address ->
                    // 创建订单项列表
                    val orderItems = checkoutProducts.map { (product, quantity) ->
                        com.example.myele_sim.model.OrderItem(
                            itemId = "item_${System.currentTimeMillis()}_${product.productId}",
                            productName = product.name,
                            productId = product.productId,
                            quantity = quantity,
                            price = product.price,
                            specifications = null
                        )
                    }

                    // 获取餐厅信息(从第一个商品)
                    val firstProduct = checkoutProducts.firstOrNull()?.first
                    if (firstProduct != null) {
                        val order = com.example.myele_sim.model.Order(
                            orderId = orderId,
                            restaurantId = firstProduct.restaurantId,
                            restaurantName = firstProduct.restaurantName,
                            status = com.example.myele_sim.model.OrderStatus.PENDING_ACCEPT,
                            orderTime = java.util.Date(),
                            items = orderItems,
                            totalAmount = subtotal,
                            discountAmount = couponDiscount,
                            actualAmount = totalAmount,
                            deliveryAddress = address,
                            deliveryFee = 5.0,
                            packagingFee = 2.0,
                            usedCouponId = selectedCoupon?.couponId,
                            canCancel = true
                        )

                        // 添加到订单管理器
                        com.example.myele_sim.data.OrderManager.addOrder(order)
                    }
                }

                // 导航到支付成功页面
                navController.navigate(
                    com.example.myele_sim.navigation.Screen.PaymentSuccess.createRoute(
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

    // 地址选择弹窗
    if (showAddressDialog) {
        AddressSelectionDialog(
            addresses = addresses,
            selectedAddress = selectedAddress,
            onAddressSelected = { address ->
                selectedAddress = address
                showAddressDialog = false
                // 记录切换地址
                com.example.myele_sim.utils.ActionLogger.logAction(
                    context = context,
                    action = "change_delivery_address",
                    page = "checkout",
                    pageInfo = mapOf(
                        "address_id" to address.addressId,
                        "address_name" to address.receiverName,
                        "detailed_address" to address.detailAddress
                    )
                )
            },
            onDismiss = { showAddressDialog = false }
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
                com.example.myele_sim.utils.ActionLogger.logAction(
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

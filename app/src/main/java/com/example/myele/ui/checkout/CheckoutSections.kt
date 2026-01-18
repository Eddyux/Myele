package com.example.myele.ui.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myele.data.CartManager
import com.example.myele.ui.components.ProductImage

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
fun AddressSection(
    selectedAddress: com.example.myele.model.Address?,
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
                    text = selectedAddress?.getFullAddress() ?: "请选择收货地址",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (selectedAddress != null) {
                        "${selectedAddress.receiverName} ${selectedAddress.receiverPhone.replace(Regex("(\\d{3})\\d{4}(\\d{4})"), "$1****$2")}"
                    } else {
                        "请选择地址"
                    },
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

            // 两个按钮:立即送出 和 预约配送
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

            // 显示选中的预约时间(在按钮下方)
            if (selectedTimeType == "预约配送" && selectedTimeSlot.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "已选择:$selectedDate $selectedTimeSlot",
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

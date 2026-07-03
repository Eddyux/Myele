package com.example.eleme_sim.ui.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eleme_sim.data.CartManager
import com.example.eleme_sim.model.Address
import com.example.eleme_sim.model.Coupon
import com.example.eleme_sim.model.Product
import com.example.eleme_sim.ui.components.ProductImage

@Composable
fun DeliveryMethodSection(selectedMethod: String, onMethodChanged: (String) -> Unit) {
    CheckoutCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("外卖配送", "到店自取").forEach { method ->
                val selected = selectedMethod == method
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onMethodChanged(method) },
                    shape = RoundedCornerShape(18.dp),
                    color = if (selected) Color(0xFFE8F8FF) else Color(0xFFF4F6F8),
                    border = BorderStroke(
                        1.dp,
                        if (selected) Color(0xFF16B8F3) else Color.Transparent
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = method,
                            modifier = Modifier.padding(vertical = 12.dp),
                            fontSize = 15.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) Color(0xFF16B8F3) else Color(0xFF344054),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddressSection(selectedAddress: Address?, onClick: () -> Unit) {
    CheckoutCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFE8F8FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF16B8F3),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = selectedAddress?.getFullAddress() ?: "请选择收货地址",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF101828),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (selectedAddress != null) {
                        "${selectedAddress.receiverName} ${selectedAddress.receiverPhone.replace(Regex("(\\d{3})\\d{4}(\\d{4})"), "$1****$2")}"
                    } else {
                        "请选择地址"
                    },
                    fontSize = 13.sp,
                    color = Color(0xFF667085)
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFB8C0CC)
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
    CheckoutCard {
        Text(
            text = "配送时间",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF101828)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DeliveryTimePill(
                text = "立即送出",
                selected = selectedTimeType == "立即送出",
                modifier = Modifier.weight(1f),
                onClick = onImmediateClicked
            )
            DeliveryTimePill(
                text = "预约配送",
                selected = selectedTimeType == "预约配送",
                modifier = Modifier.weight(1f),
                onClick = onScheduleClicked
            )
        }
        if (selectedTimeType == "预约配送" && selectedTimeSlot.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "已选择:$selectedDate $selectedTimeSlot",
                fontSize = 12.sp,
                color = Color(0xFF667085)
            )
        }
    }
}

@Composable
fun OrderItemsSection(restaurantName: String, products: List<Pair<Product, Int>>) {
    CheckoutCard {
        Text(
            text = restaurantName,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101828)
        )
        Spacer(modifier = Modifier.height(12.dp))

        products.forEachIndexed { index, (product, quantity) ->
            if (index > 0) {
                HorizontalDivider(color = Color(0xFFF0F2F5), thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProductImage(
                    productId = product.productId,
                    productName = product.name,
                    restaurantName = product.restaurantName,
                    modifier = Modifier.size(58.dp),
                    size = 58.dp,
                    cornerRadius = 12.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${product.name} x$quantity",
                    modifier = Modifier.weight(1f),
                    fontSize = 15.sp,
                    color = Color(0xFF101828),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "¥%.1f".format(product.price * quantity),
                    fontSize = 16.sp,
                    color = Color(0xFF101828),
                    fontWeight = FontWeight.Bold
                )
            }
            if (index < products.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CouponSelectionSection(selectedCoupon: Coupon?, onClick: () -> Unit) {
    CheckoutCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(Color(0xFFFFEFEA), Color(0xFFFFF6F2))),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = Color(0xFFFF6B4A),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("店铺活动/券", fontSize = 15.sp, color = Color(0xFF101828), fontWeight = FontWeight.SemiBold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedCoupon != null) {
                    val subtotal = CartManager.getSubtotal()
                    val deliveryFee = 5.0
                    val discountAmount = selectedCoupon.calculateDiscount(subtotal, deliveryFee)
                    Text(
                        text = "-¥%.0f".format(discountAmount),
                        fontSize = 15.sp,
                        color = Color(0xFFFF5C39),
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text("请选择", fontSize = 14.sp, color = Color(0xFF98A2B3))
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFB8C0CC)
                )
            }
        }
    }
}

@Composable
fun OrderFeesSection(subtotal: Double, couponDiscount: Double) {
    val packagingFee = 2.0
    val deliveryFee = 5.0
    val total = subtotal + packagingFee + deliveryFee - couponDiscount

    CheckoutCard {
        FeeItem("商品总额", "¥%.1f".format(subtotal))
        FeeItem("打包费", "¥%.1f".format(packagingFee))
        FeeItem("配送费", "¥%.1f".format(deliveryFee))
        if (couponDiscount > 0) {
            FeeItem("优惠券", "-¥%.1f".format(couponDiscount), Color(0xFFFF5C39))
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF0F2F5))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("小计", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF101828))
            Text(
                text = "¥%.1f".format(total),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF101828)
            )
        }
    }
}

@Composable
fun OrderOptionsSection() {
    CheckoutCard {
        OptionItem("备注", "口味、偏好等要求")
        HorizontalDivider(color = Color(0xFFF0F2F5))
        OptionItem("餐具", "不需要餐具")
        HorizontalDivider(color = Color(0xFFF0F2F5))
        OptionItem("发票", "不需要发票")
    }
}

@Composable
fun PaymentMethodSection(selectedMethod: String, onClick: () -> Unit) {
    CheckoutCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("支付方式", fontSize = 15.sp, color = Color(0xFF101828), fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(selectedMethod, fontSize = 14.sp, color = Color(0xFF16B8F3), fontWeight = FontWeight.Medium)
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFB8C0CC)
                )
            }
        }
    }
}

@Composable
private fun CheckoutCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp), content = content)
    }
}

@Composable
private fun DeliveryTimePill(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) Color(0xFFE8F8FF) else Color(0xFFF7F8FA),
        border = BorderStroke(1.dp, if (selected) Color(0xFF16B8F3) else Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF16B8F3),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Color(0xFF16B8F3) else Color(0xFF344054)
            )
        }
    }
}

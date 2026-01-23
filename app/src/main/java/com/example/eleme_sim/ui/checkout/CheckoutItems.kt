package com.example.eleme_sim.ui.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CouponItem(
    coupon: com.example.eleme_sim.model.Coupon,
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
                    com.example.eleme_sim.model.CouponType.REDUCTION -> {
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
                    com.example.eleme_sim.model.CouponType.DISCOUNT -> {
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
                    com.example.eleme_sim.model.CouponType.DELIVERY_FREE -> {
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
                    com.example.eleme_sim.model.CouponType.SPECIAL -> {
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

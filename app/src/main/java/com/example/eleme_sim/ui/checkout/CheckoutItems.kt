package com.example.eleme_sim.ui.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eleme_sim.model.Coupon
import com.example.eleme_sim.model.CouponType
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CouponItem(
    coupon: Coupon,
    isSelected: Boolean,
    subtotal: Double,
    isRecommended: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) Color(0xFF16B8F3) else Color(0xFFF0F2F5)
        ),
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(92.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFF0EA), Color(0xFFFFFAF7))
                        ),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    when (coupon.type) {
                        CouponType.REDUCTION -> {
                            Text(
                                text = "¥${coupon.discountAmount?.toInt() ?: 0}",
                                fontSize = 30.sp,
                                color = Color(0xFFFF5C39),
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "满${coupon.minOrderAmount?.toInt() ?: 0}可用",
                                fontSize = 12.sp,
                                color = Color(0xFF98A2B3)
                            )
                        }
                        CouponType.DISCOUNT -> {
                            val discountPercent = ((1 - (coupon.discountRate ?: 1.0)) * 10).toInt()
                            Text(
                                text = "${discountPercent}折",
                                fontSize = 28.sp,
                                color = Color(0xFFFF5C39),
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "满${coupon.minOrderAmount?.toInt() ?: 0}可用",
                                fontSize = 12.sp,
                                color = Color(0xFF98A2B3)
                            )
                        }
                        CouponType.DELIVERY_FREE -> {
                            Text(
                                text = "免配送",
                                fontSize = 22.sp,
                                color = Color(0xFFFF5C39),
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text("全场可用", fontSize = 12.sp, color = Color(0xFF98A2B3))
                        }
                        CouponType.SPECIAL -> {
                            Text(
                                text = "特价",
                                fontSize = 26.sp,
                                color = Color(0xFFFF5C39),
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = coupon.description ?: "",
                                fontSize = 12.sp,
                                color = Color(0xFF98A2B3),
                                maxLines = 2
                            )
                        }
                        else -> {
                            Text(
                                text = "¥${coupon.discountAmount?.toInt() ?: 0}",
                                fontSize = 30.sp,
                                color = Color(0xFFFF5C39),
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "满${coupon.minOrderAmount?.toInt() ?: 0}可用",
                                fontSize = 12.sp,
                                color = Color(0xFF98A2B3)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coupon.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF101828)
                )
                Spacer(modifier = Modifier.height(4.dp))
                val validUntil = SimpleDateFormat("今日HH:mm到期", Locale.CHINA).format(coupon.validUntil)
                Text(
                    text = validUntil,
                    fontSize = 12.sp,
                    color = Color(0xFFFF5C39)
                )
                if (coupon.description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = coupon.description,
                        fontSize = 12.sp,
                        color = Color(0xFF98A2B3)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isRecommended) {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color(0xFFFFF0EA)
                    ) {
                        Text(
                            text = "推荐",
                            fontSize = 10.sp,
                            color = Color(0xFFFF5C39),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF16B8F3))
                )
            }
        }
    }
}

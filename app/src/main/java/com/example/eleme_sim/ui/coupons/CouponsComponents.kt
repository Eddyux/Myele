package com.example.eleme_sim.ui.coupons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.eleme_sim.model.Coupon
import com.example.eleme_sim.model.CouponStatus
import java.text.SimpleDateFormat
import java.util.Locale

private val CouponAccent = Color(0xFFFF5C39)
private val CouponAccentSoft = Color(0xFFFFEFE8)
private val CouponText = Color(0xFF1D1D1F)
private val CouponTextSecondary = Color(0xFF8C8C92)

@Composable
fun SuperFoodieCardCoupon() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFFCF5),
                            Color(0xFFFFF7EB),
                            Color(0xFFFFFBF4)
                        )
                    )
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFFFF8E8)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "5",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC36F17)
                        )
                        Text(text = "元×", fontSize = 14.sp, color = Color(0xFFC36F17))
                        Text(
                            text = "6",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC36F17)
                        )
                        Text(text = "张", fontSize = 14.sp, color = Color(0xFFC36F17))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "无门槛",
                        fontSize = 12.sp,
                        color = CouponTextSecondary
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "30元超级吃货卡",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CouponText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, Color(0xFFD3B77F))
                    ) {
                        Text(
                            text = "0.4折",
                            fontSize = 12.sp,
                            color = Color(0xFFB78624),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "爆红包商家专享",
                    fontSize = 15.sp,
                    color = CouponAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF29B3D)
                ),
                shape = RoundedCornerShape(22.dp),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "¥0.7",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("购买", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun CouponCard(coupon: Coupon) {
    val isExpiring = coupon.isExpiringSoon
    val dateFormat = SimpleDateFormat("今日 HH:mm 到期", Locale.getDefault())

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = if (coupon.status == CouponStatus.AVAILABLE) {
                                    listOf(Color(0xFFFF8B66), Color(0xFFFF266C))
                                } else {
                                    listOf(Color(0xFFD3D4D8), Color(0xFFBCBEC3))
                                }
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "爆",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = coupon.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = CouponText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = dateFormat.format(coupon.validUntil),
                        fontSize = 13.sp,
                        color = CouponAccent,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    if (isExpiring) {
                        Text(
                            text = "将到期",
                            fontSize = 12.sp,
                            color = CouponAccent,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "¥",
                            fontSize = 17.sp,
                            color = CouponAccent,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${coupon.discountAmount?.toInt() ?: 6}",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CouponAccent
                        )
                    }
                    Text(
                        text = "满${coupon.minOrderAmount?.toInt() ?: 20}可用",
                        fontSize = 13.sp,
                        color = CouponTextSecondary
                    )
                }
            }

            if (coupon.description != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFAFAFB)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = coupon.description,
                            fontSize = 12.sp,
                            color = CouponTextSecondary,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = CouponTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (coupon.name.contains("可爆")) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = { },
                            shape = RoundedCornerShape(22.dp),
                            border = BorderStroke(1.dp, Color(0xFFFFC8BA)),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text("去使用", fontSize = 14.sp, color = CouponAccent)
                        }
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CouponAccent
                            ),
                            shape = RoundedCornerShape(22.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text("去爆涨", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CouponAccent
                        ),
                        shape = RoundedCornerShape(22.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text("去使用", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomActionButton(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = CouponAccentSoft
        ) {
            Icon(
                imageVector = when (text) {
                    "历史红包" -> Icons.Default.History
                    "兑换码" -> Icons.Default.QrCode
                    "天天爆红包" -> Icons.Default.LocalOffer
                    else -> Icons.Default.CardGiftcard
                },
                contentDescription = text,
                tint = CouponAccent,
                modifier = Modifier
                    .padding(12.dp)
                    .size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = CouponTextSecondary
        )
    }
}

package com.example.eleme_sim.ui.customerservice

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eleme_sim.ui.components.RestaurantImage

@Composable
fun OrderServiceCard(
    restaurantName: String,
    orderStatus: String,
    orderTime: String,
    orderAmount: Double
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(26.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2FBDFB),
                            Color(0xFF28B5F4)
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "订单服务 & 服务进度",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "更换订单 >",
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RestaurantImage(
                            restaurantName = restaurantName,
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            size = 76.dp,
                            cornerRadius = 16.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = restaurantName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1D24),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = orderTime,
                                fontSize = 12.sp,
                                color = Color(0xFFA0A9B7)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = orderStatus,
                                fontSize = 16.sp,
                                color = Color(0xFF9CA6B3)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "¥$orderAmount",
                                fontSize = 16.sp,
                                color = Color(0xFF6B717C)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "更多",
                            fontSize = 14.sp,
                            color = Color(0xFF9BA4B2),
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable { }
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ServiceButton("电话商家", Modifier.weight(1f))
                            ServiceButton("电话骑士", Modifier.weight(1f))
                            ServiceButton("订单投诉", Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceFunctionsGrid() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(202.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(
                    listOf(
                        Triple("查询吃货卡", Icons.Default.CreditCard, null),
                        Triple("取消续费", Icons.Default.Cancel, null),
                        Triple("红包诊断", Icons.Default.Money, null),
                        Triple("举报中心", Icons.Default.ReportProblem, null),
                        Triple("换绑手机号", Icons.Default.PhoneAndroid, null),
                        Triple("解绑银行卡", Icons.Default.AccountBalance, null),
                        Triple("发票助手", Icons.Default.ReceiptLong, null),
                        Triple("账户检测", Icons.Default.Security, "临期红包")
                    )
                ) { (label, icon, badge) ->
                    ServiceFunctionItem(label, icon, badge)
                }
            }
        }
    }
}

@Composable
fun HotQuestions() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
            listOf(
                "怎么退单？",
                "少送商品怎么办？",
                "商品撒漏怎么办？"
            ).forEach { question ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = question,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1D24)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFA6AFBB)
                    )
                }
                if (question != "商品撒漏怎么办？") {
                    HorizontalDivider(color = Color(0xFFF0F3F7))
                }
            }
        }
    }
}

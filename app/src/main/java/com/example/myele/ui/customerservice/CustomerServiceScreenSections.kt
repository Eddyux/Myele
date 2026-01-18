package com.example.myele.ui.customerservice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

/**
 * 订单服务卡片
 */
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
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF00BCD4)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "订单服务 & 服务进度",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "更换订单 >",
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.clickable { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 店铺图标
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF6B00)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = restaurantName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$orderTime",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = orderStatus,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "¥$orderAmount",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ServiceButton("更多", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                ServiceButton("电话商家", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                ServiceButton("电话骑士", Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                ServiceButton("订单投诉", Modifier.weight(1f))
            }
        }
    }
}

/**
 * 服务功能网格
 */
@Composable
fun ServiceFunctionsGrid() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    listOf(
                        "查询吃货卡" to Icons.Default.CreditCard,
                        "取消续费" to Icons.Default.Cancel,
                        "红包诊断" to Icons.Default.Money,
                        "举报中心" to Icons.Default.Report,
                        "换绑手机号" to Icons.Default.Phone,
                        "解绑银行卡" to Icons.Default.AccountBalance,
                        "发票助手" to Icons.Default.Receipt,
                        "账户检测" to Icons.Default.Security
                    )
                ) { (label, icon) ->
                    ServiceFunctionItem(label, icon)
                }
            }
        }
    }
}

/**
 * 热门问题列表
 */
@Composable
fun HotQuestions() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            listOf(
                "怎么退单？",
                "少送商品怎么办？",
                "商品撒漏怎么办？"
            ).forEach { question ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO */ }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = question,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
                if (question != "商品撒漏怎么办？") {
                    Divider(color = Color(0xFFF0F0F0))
                }
            }
        }
    }
}

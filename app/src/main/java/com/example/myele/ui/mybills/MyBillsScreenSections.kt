package com.example.myele.ui.mybills

import androidx.compose.foundation.background
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

@Composable
fun TimeSelectionSection(selectedTab: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF00BFFF)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedTab == "周账单") "10月13日(一) — 10月19日(日)" else "2025年9月",
                fontSize = 14.sp,
                color = Color.White
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun TotalExpenseCard(selectedTab: String, totalExpense: Double) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF00BFFF)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (selectedTab == "周账单") "本周总花费" else "9月总花费",
                fontSize = 14.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "¥",
                    fontSize = 24.sp,
                    color = Color.White
                )
                Text(
                    text = String.format("%.2f", totalExpense),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("回血攻略", color = Color(0xFF00BFFF))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "· 开通超级吃货卡,每月最高20元包",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    TextButton(onClick = { }) {
                        Text("去开通", fontSize = 12.sp, color = Color.White)
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "获得吃货豆0个",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    TextButton(onClick = { }) {
                        Text("去赚豆", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyComparisonSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "月度对比",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 简化的柱状图
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                repeat(6) { index ->
                    val months = listOf("4月", "5月", "6月", "7月", "8月", "9月")
                    val heights = listOf(0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 1.0f)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height((120 * heights[index]).dp)
                                .background(
                                    if (index == 5) Color(0xFF00BFFF) else Color.LightGray,
                                    RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = months[index],
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryPreferenceSection(selectedTab: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "品类偏好",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (selectedTab == "周账单")
                    "精致一人餐当最便携的快餐,做心情愉快的吃货!"
                else
                    "本月的你爱我地方菜,是想更贴近电感吧~",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 品类占比展示
            val categories = if (selectedTab == "周账单") {
                listOf(
                    BillCategory("湘菜", 40, 102.0, 1),
                    BillCategory("韩国料理", 32, 81.5, 1),
                    BillCategory("北京菜", 14, 36.12, 1),
                    BillCategory("川菜", 13, 33.0, 1)
                )
            } else {
                listOf(
                    BillCategory("地方菜", 64, 26.70, 2),
                    BillCategory("便当简餐", 36, 14.78, 2)
                )
            }

            categories.forEachIndexed { index, category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.width(24.dp)
                        )
                        Column {
                            Text(
                                text = category.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .width((category.percentage * 1.2f).dp)
                                        .height(4.dp)
                                        .background(
                                            Color(0xFFFF6B00),
                                            RoundedCornerShape(2.dp)
                                        )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${category.percentage}%",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "¥${category.amount}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${category.count}笔",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            if (selectedTab == "周账单") {
                TextButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("展开全部", color = Color(0xFF00BFFF))
                }
            }
        }
    }
}

@Composable
fun ConsumptionRankingSection(selectedTab: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (selectedTab == "月账单") "消费排行" else "本周你在这些店下了4次",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            val restaurants = if (selectedTab == "月账单") {
                listOf(
                    TopRestaurant(1, "湘味轩", 26.70, 2),
                    TopRestaurant(2, "川香麻辣烫", 14.78, 2)
                )
            } else {
                listOf(
                    TopRestaurant(1, "湘味轩", 102.0, 1),
                    TopRestaurant(2, "韩式炸鸡", 81.5, 1),
                    TopRestaurant(3, "老北京炸酱面", 36.12, 1),
                    TopRestaurant(4, "川香麻辣烫", 33.0, 1)
                )
            }

            restaurants.forEach { restaurant ->
                RestaurantRankItem(restaurant, selectedTab == "月账单")
            }
        }
    }
}

@Composable
fun ScenePreferenceSection(selectedTab: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "场景偏好",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (selectedTab == "周账单")
                    "本周的你还在午后午餐,做个么能就能元素靠的午间休憩时光~"
                else
                    "本月的你还在年后午餐,做个么能就能元素靠的午间休憩时光~",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 时段占比圆形图示意
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                // 简化的图表展示
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = null,
                        tint = Color(0xFF00BFFF),
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = if (selectedTab == "周账单") "50%" else "75%",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (selectedTab == "周账单") "午间" else "午间",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("早间", "午间", "下午", "晚间", "夜间").forEach { time ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val percentage = when (time) {
                            "午间" -> if (selectedTab == "周账单") "50%" else "75%"
                            "晚间" -> if (selectedTab == "周账单") "50%" else "0%"
                            else -> "0%"
                        }
                        Text(percentage, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(time, fontSize = 11.sp, color = Color.Gray)
                        val count = when (time) {
                            "午间" -> if (selectedTab == "周账单") "2笔" else "3笔"
                            "晚间" -> if (selectedTab == "周账单") "2笔" else "0笔"
                            else -> "0笔"
                        }
                        Text(count, fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun MoneySavingTipsSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "省钱技巧",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            PromoCard("充值下单更省钱", "使用钱包充值,支付享优惠", "去优惠")
            Spacer(modifier = Modifier.height(12.dp))
            PromoCard("最高28元爆红包", "快来赚更多爆红包~", "赚包包")
            Spacer(modifier = Modifier.height(12.dp))
            PromoCard("双12全服回归礼包", "为了最好的你,还原经的本金", "")
        }
    }
}

@Composable
fun BottomDescriptionSection(selectedTab: String, onSwitchTab: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "饿了么旨在为你提供方便、省力、放心的服务",
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onSwitchTab) {
            Text(
                text = if (selectedTab == "周账单") "查看月账单 >" else "查看周账单 >",
                fontSize = 13.sp,
                color = Color(0xFF00BFFF)
            )
        }
    }
}

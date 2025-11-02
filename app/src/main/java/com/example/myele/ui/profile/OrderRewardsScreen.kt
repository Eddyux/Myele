package com.example.myele.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderRewardsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部导航栏
        TopAppBar(
            title = { Text("下单返") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // 返现统计卡片
            item {
                RewardsSummaryCard()
            }

            // 返现规则说明
            item {
                RewardsRulesCard()
            }

            // 返现记录
            item {
                Text(
                    text = "返现记录",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // 返现记录列表
            items(getSampleRewards()) { reward ->
                RewardItem(reward)
            }
        }
    }
}

@Composable
fun RewardsSummaryCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "累计返现（元）",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "128.50",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF3366)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RewardStatItem("本月返现", "35.20")
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp),
                    color = Color(0xFFE0E0E0)
                )
                RewardStatItem("待入账", "12.80")
            }
        }
    }
}

@Composable
fun RewardStatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun RewardsRulesCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF3E0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "返现规则",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "每笔订单完成后可获得订单金额1-5%的返现",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun RewardItem(reward: RewardRecord) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF3E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 信息
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reward.storeName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reward.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = reward.status,
                    fontSize = 12.sp,
                    color = if (reward.status == "已入账") Color(0xFF4CAF50) else Color(0xFFFF9800)
                )
            }

            // 金额
            Text(
                text = "+¥${reward.amount}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF3366)
            )
        }
    }
}

data class RewardRecord(
    val storeName: String,
    val date: String,
    val amount: String,
    val status: String
)

fun getSampleRewards(): List<RewardRecord> {
    return listOf(
        RewardRecord("麦当劳", "2025-11-01 12:30", "3.50", "已入账"),
        RewardRecord("肯德基", "2025-11-01 18:45", "5.20", "已入账"),
        RewardRecord("星巴克", "2025-10-31 14:20", "2.80", "已入账"),
        RewardRecord("coco都可", "2025-10-31 10:15", "1.50", "已入账"),
        RewardRecord("海底捞", "2025-10-30 19:30", "12.80", "待入账"),
        RewardRecord("必胜客", "2025-10-30 12:00", "6.40", "已入账"),
        RewardRecord("汉堡王", "2025-10-29 13:30", "4.20", "已入账"),
        RewardRecord("喜茶", "2025-10-29 15:45", "3.10", "已入账")
    )
}

package com.example.myele.ui.mybills

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.data.ActionLogger
import androidx.compose.ui.platform.LocalContext
import java.util.*

data class BillCategory(val name: String, val percentage: Int, val amount: Double, val count: Int)
data class TopRestaurant(val rank: Int, val name: String, val amount: Double, val orderCount: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBillsScreen(navController: NavController, repository: DataRepository) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("周账单") }
    val totalExpense = if (selectedTab == "周账单") 113.33 else 41.48

    // 记录进入我的账单页面
    LaunchedEffect(Unit) {
        ActionLogger.logAction(
            context = context,
            action = "enter_mybills_page",
            page = "mybills",
            pageInfo = mapOf(
                "title" to "我的账单",
                "screen_name" to "MyBillsScreen"
            ),
            extraData = mapOf(
                "selected_tab" to "周账单",
                "source" to "profile"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的账单") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Share, contentDescription = "分享")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00BFFF)
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 标签页
            TabRow(
                selectedTabIndex = if (selectedTab == "周账单") 0 else 1,
                containerColor = Color(0xFF00BFFF),
                contentColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == "周账单",
                    onClick = { selectedTab = "周账单" },
                    text = {
                        Text(
                            text = "周账单",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = if (selectedTab == "周账单") FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == "月账单",
                    onClick = {
                        selectedTab = "月账单"
                        // 记录切换到月账单
                        ActionLogger.logAction(
                            context = context,
                            action = "switch_to_monthly_bill",
                            page = "mybills",
                            pageInfo = mapOf(
                                "title" to "我的账单",
                                "screen_name" to "MyBillsScreen"
                            ),
                            extraData = mapOf(
                                "selected_tab" to "月账单",
                                "switched_from" to "周账单"
                            )
                        )
                    },
                    text = {
                        Text(
                            text = "月账单",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = if (selectedTab == "月账单") FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 12.dp)
            ) {
                // 时间选择
                item {
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

                // 总花费卡片
                item {
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

                // 月度对比（仅月账单显示）
                if (selectedTab == "月账单") {
                    item {
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
                }

                // 品类偏好
                item {
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
                                    BillCategory("便当简餐", 35, 40.07, 6),
                                    BillCategory("奶茶果汁", 20, 22.10, 4),
                                    BillCategory("小吃", 16, 17.86, 1),
                                    BillCategory("粥", 16, 17.86, 1),
                                    BillCategory("新茶饮", 13, 14.10, 1)
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

                // 消费排行（仅月账单）或本周下单店铺（周账单）
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (selectedTab == "月账单") "消费排行" else "本周你在这些店下了7次",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            val restaurants = if (selectedTab == "月账单") {
                                listOf(
                                    TopRestaurant(1, "北京鸭梨(华师校园店)", 26.70, 2),
                                    TopRestaurant(2, "家倍连·味秦家(武汉店)", 14.78, 2)
                                )
                            } else {
                                listOf(
                                    TopRestaurant(1, "便当简餐", 40.07, 6),
                                    TopRestaurant(2, "奶茶果汁", 22.10, 4),
                                    TopRestaurant(3, "小吃", 17.86, 1)
                                )
                            }

                            restaurants.forEach { restaurant ->
                                RestaurantRankItem(restaurant, selectedTab == "月账单")
                            }
                        }
                    }
                }

                // 场景偏好
                item {
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
                                        text = if (selectedTab == "周账单") "45%" else "75%",
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
                                            "午间" -> if (selectedTab == "周账单") "45%" else "75%"
                                            "下午" -> if (selectedTab == "周账单") "37%" else "25%"
                                            else -> "0%"
                                        }
                                        Text(percentage, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        Text(time, fontSize = 11.sp, color = Color.Gray)
                                        val count = when (time) {
                                            "午间" -> if (selectedTab == "周账单") "5笔" else "3笔"
                                            "下午" -> if (selectedTab == "周账单") "4笔" else "1笔"
                                            else -> "0笔"
                                        }
                                        Text(count, fontSize = 10.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }

                // 省钱技巧
                item {
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

                // 底部说明
                item {
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
                        TextButton(onClick = { selectedTab = if (selectedTab == "周账单") "月账单" else "周账单" }) {
                            Text(
                                text = if (selectedTab == "周账单") "查看月账单 >" else "查看周账单 >",
                                fontSize = 13.sp,
                                color = Color(0xFF00BFFF)
                            )
                        }
                    }
                }

                // 账单助手
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "账单助手",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantRankItem(restaurant: TopRestaurant, showRestaurantName: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { },
        shape = RoundedCornerShape(8.dp),
        color = if (restaurant.rank == 1) Color(0xFFFFF3E0) else Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (restaurant.rank == 1) Color(0xFFFF6B00) else Color.LightGray,
                            if (showRestaurantName) RoundedCornerShape(4.dp) else CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showRestaurantName) "TOP ${restaurant.rank}" else "${restaurant.rank}",
                        fontSize = if (showRestaurantName) 11.sp else 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = restaurant.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Text(
                        text = "${restaurant.orderCount}${if (showRestaurantName) "笔" else "笔"}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
            Text(
                text = "¥${restaurant.amount}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PromoCard(title: String, subtitle: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        if (actionText.isNotBlank()) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF3366)
                ),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(actionText, fontSize = 12.sp)
            }
        }
    }
}

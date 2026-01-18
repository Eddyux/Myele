package com.example.myele.ui.coupons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.utils.JsonFileWriter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponsScreen(navController: NavController, repository: DataRepository) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("红包") }
    var selectedSort by remember { mutableStateOf("默认排序") }
    val coupons = remember { repository.getCoupons() }

    // 页面进入时写入JSON数据到 /data/data/com.example.myele/files/messages.json
    LaunchedEffect(Unit) {
        val jsonData = JsonFileWriter.createCouponsPageData(
            extraData = mapOf(
                "selected_tab" to "红包",
                "total_coupons" to coupons.size,
                "source" to "profile_menu"
            )
        )
        JsonFileWriter.writeToMessagesJson(context, jsonData)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("红包卡券") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
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
                selectedTabIndex = listOf("红包", "已购券", "卡").indexOf(selectedTab),
                containerColor = Color.White
            ) {
                listOf("红包", "已购券", "卡").forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = tab,
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                                )
                                if (selectedTab == tab && tab == "红包") {
                                    Text(
                                        text = "12",
                                        fontSize = 12.sp,
                                        color = Color(0xFFFF3366)
                                    )
                                }
                            }
                        }
                    )
                }
            }

            // 排序和筛选
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listOf("默认排序", "爆红包", "平台红包", "商品红包")) { sort ->
                    FilterChip(
                        selected = selectedSort == sort,
                        onClick = { selectedSort = sort },
                        label = { Text(sort, fontSize = 13.sp) }
                    )
                }
            }

            // 红包卡券列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 30元超级吃货卡
                item {
                    SuperFoodieCardCoupon()
                }

                // 红包列表
                items(coupons.size.coerceAtMost(10)) { index ->
                    CouponCard(coupons[index])
                }

                // 底部功能按钮
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomActionButton("历史红包")
                        BottomActionButton("兑换码")
                        BottomActionButton("天天爆红包")
                        BottomActionButton("天猫优惠")
                    }
                }
            }
        }
    }
}

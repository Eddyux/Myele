package com.example.myele_sim.ui.customerservice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myele_sim.data.DataRepository

/**
 * 客服页面（服务大厅）
 * 从Profile页面的"我的客服"点击进入
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerServiceScreen(navController: NavController, repository: DataRepository) {
    var selectedTab by remember { mutableStateOf(0) }

    // 获取最近的订单
    val recentOrder = remember {
        repository.getOrders().firstOrNull()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("服务大厅")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        },
        bottomBar = {
            BottomButtons(navController)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding),
            contentPadding = PaddingValues(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 12.dp)
        ) {
            // 智能客服横幅
            item {
                SmartServiceBanner()
            }

            // 订单服务 & 服务进度
            if (recentOrder != null) {
                item {
                    OrderServiceCard(
                        restaurantName = recentOrder.restaurantName,
                        orderStatus = "已送达",
                        orderTime = "2025-10-21 10:52",
                        orderAmount = recentOrder.actualAmount
                    )
                }
            }

            // 夜间配送延迟通知
            item {
                NotificationBar()
            }

            // 功能图标区域
            item {
                ServiceFunctionsGrid()
            }

            // 标签栏
            item {
                ServiceTabs(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            // 热门问题列表
            item {
                HotQuestions()
            }
        }
    }
}

package com.example.myele_sim.ui.mybills

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.myele_sim.data.DataRepository
import com.example.myele_sim.data.ActionLogger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBillsScreen(navController: NavController, repository: DataRepository) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("周账单") }
    val totalExpense = if (selectedTab == "周账单") 252.62 else 41.48

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
                    TimeSelectionSection(selectedTab)
                }

                // 总花费卡片
                item {
                    TotalExpenseCard(selectedTab, totalExpense)
                }

                // 月度对比（仅月账单显示）
                if (selectedTab == "月账单") {
                    item {
                        MonthlyComparisonSection()
                    }
                }

                // 品类偏好
                item {
                    CategoryPreferenceSection(selectedTab)
                }

                // 消费排行（仅月账单）或本周下单店铺（周账单）
                item {
                    ConsumptionRankingSection(selectedTab)
                }

                // 场景偏好
                item {
                    ScenePreferenceSection(selectedTab)
                }

                // 省钱技巧
                item {
                    MoneySavingTipsSection()
                }

                // 底部说明
                item {
                    BottomDescriptionSection(
                        selectedTab = selectedTab,
                        onSwitchTab = { selectedTab = if (selectedTab == "周账单") "月账单" else "周账单" }
                    )
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

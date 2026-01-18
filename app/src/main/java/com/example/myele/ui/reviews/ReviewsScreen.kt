package com.example.myele.ui.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.myele.data.ActionLogger
import com.example.myele.data.DataRepository
import com.example.myele.model.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(navController: NavController, repository: DataRepository) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("待评价") }
    val orders = remember { repository.getOrders() }
    val reviews = remember { repository.getReviews() }

    // 记录进入待评价页面
    LaunchedEffect(Unit) {
        ActionLogger.logAction(
            context = context,
            action = "enter_reviews_page",
            page = "reviews",
            pageInfo = mapOf(
                "title" to "评价中心",
                "screen_name" to "ReviewsScreen"
            ),
            extraData = mapOf(
                "selected_tab" to "待评价",
                "source" to "profile"
            )
        )
    }

    // 筛选待评价订单
    val pendingReviewOrders = orders.filter {
        it.status == OrderStatus.COMPLETED && !it.hasReview
    }

    // 已评价列表
    val completedReviews = reviews.filter { review ->
        orders.any { order -> order.orderId == review.orderId && order.hasReview }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("评价中心") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    TextButton(onClick = { }) {
                        Text("规则", color = Color.Black)
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
            // 用户信息区域
            UserInfoSection()

            // 评价官推广
            ReviewerPromoSection()

            // 标签页
            TabRow(
                selectedTabIndex = if (selectedTab == "待评价") 0 else 1,
                containerColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == "待评价",
                    onClick = { selectedTab = "待评价" },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "待评价",
                                fontSize = 15.sp,
                                fontWeight = if (selectedTab == "待评价") FontWeight.Bold else FontWeight.Normal
                            )
                            if (selectedTab == "待评价") {
                                Text(
                                    text = "(${pendingReviewOrders.size})",
                                    fontSize = 12.sp,
                                    color = Color(0xFF00BFFF)
                                )
                            }
                        }
                    }
                )
                Tab(
                    selected = selectedTab == "已评价",
                    onClick = {
                        selectedTab = "已评价"
                        // 记录切换到已评价
                        ActionLogger.logAction(
                            context = context,
                            action = "switch_to_reviewed",
                            page = "reviews",
                            pageInfo = mapOf(
                                "title" to "评价中心",
                                "screen_name" to "ReviewsScreen"
                            ),
                            extraData = mapOf(
                                "selected_tab" to "已评价",
                                "switched_from" to "待评价"
                            )
                        )
                    },
                    text = {
                        Text(
                            text = "已评价",
                            fontSize = 15.sp,
                            fontWeight = if (selectedTab == "已评价") FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == "已评价") Color(0xFF00BFFF) else Color.Black
                        )
                    }
                )
            }

            // 评价列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedTab == "待评价") {
                    items(pendingReviewOrders.size) { index ->
                        PendingReviewCard(pendingReviewOrders[index])
                    }
                } else {
                    items(completedReviews.size) { index ->
                        val review = completedReviews[index]
                        val order = orders.find { order -> order.orderId == review.orderId }
                        if (order != null) {
                            CompletedReviewCard(review, order, context)
                        }
                    }
                }
            }
        }
    }
}

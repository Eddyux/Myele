package com.example.eleme_sim.ui.orderdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eleme_sim.data.DataRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    repository: DataRepository,
    orderId: String
) {
    // 从repository获取订单详情
    val order = remember { repository.getOrders().find { it.orderId == orderId } }
    var showContactBottomSheet by remember { mutableStateOf(false) }
    var showContactMerchantBottomSheet by remember { mutableStateOf(false) }
    var showCancelOrderSheet by remember { mutableStateOf(false) }
    var showCancelSuccessDialog by remember { mutableStateOf(false) }

    if (order == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("订单不存在")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 订单状态头部
            item {
                OrderStatusHeader(order = order)
            }

            // 操作按钮
            item {
                OrderActionButtons(
                    order = order,
                    onContactMerchant = { showContactMerchantBottomSheet = true },
                    onContactRider = { showContactBottomSheet = true },
                    onCancelOrder = { showCancelOrderSheet = true }
                )
            }

            // 食无忧理赔进入栏（所有订单都显示）
            item {
                FoodInsuranceEntry(navController = navController, orderId = order.orderId)
            }

            // 订单商品信息
            item {
                OrderProductsSection(order = order)
            }

            // 配送信息
            item {
                DeliveryInfoSection(order = order)
            }

            // 订单信息
            item {
                OrderInfoSection(order = order)
            }

            // 联系客服按钮
            item {
                ContactCustomerServiceButton(navController = navController)
            }
        }
    }

    // 联系骑手底部弹窗
    if (showContactBottomSheet) {
        ContactRiderBottomSheet(
            order = order,
            navController = navController,
            onDismiss = { showContactBottomSheet = false }
        )
    }

    // 联系商家底部弹窗
    if (showContactMerchantBottomSheet) {
        ContactMerchantBottomSheet(
            navController = navController,
            onDismiss = { showContactMerchantBottomSheet = false }
        )
    }

    // 取消订单底部弹窗
    if (showCancelOrderSheet) {
        CancelOrderBottomSheet(
            order = order,
            navController = navController,
            onDismiss = { showCancelOrderSheet = false },
            onCancelSuccess = { showCancelSuccessDialog = true }
        )
    }

    // 取消成功弹窗
    if (showCancelSuccessDialog) {
        CancelSuccessDialog(
            onDismiss = { showCancelSuccessDialog = false }
        )
    }
}


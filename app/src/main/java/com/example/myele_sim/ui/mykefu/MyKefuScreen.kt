package com.example.myele_sim.ui.mykefu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.myele_sim.model.Order

@Composable
fun MyKefuScreen(navController: NavController) {
    var showOrderSelector by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    var inputText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // 顶部标题栏
            TopBar(onBackClicked = { navController.popBackStack() })

            // 人工服务繁忙提示
            ServiceBusyTip()

            // 智能客服问候区域
            SmartServiceGreeting()

            // 订单选择区域
            if (selectedOrder != null) {
                SelectedOrderSection(
                    order = selectedOrder!!,
                    onOrderChange = { showOrderSelector = true }
                )
            } else {
                OrderSuggestionSection(onOrderSelect = { showOrderSelector = true })
            }

            // 聊天消息区域
            ChatMessagesSection()

            Spacer(modifier = Modifier.weight(1f))

            // 输入框
            InputSection(
                inputText = inputText,
                onInputChange = { inputText = it }
            )
        }

        // 订单选择弹窗
        if (showOrderSelector) {
            OrderSelectorDialog(
                onDismiss = { showOrderSelector = false },
                onOrderSelected = { order ->
                    selectedOrder = order
                    showOrderSelector = false
                }
            )
        }
    }
}

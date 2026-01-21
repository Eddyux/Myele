package com.example.myele_sim.ui.foodinsurance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInsuranceScreen(
    navController: NavController,
    repository: DataRepository,
    orderId: String
) {
    val context = LocalContext.current
    val order = remember { repository.getOrders().find { it.orderId == orderId } }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // 记录进入食无忧理赔页面
    LaunchedEffect(Unit) {
        ActionLogger.logAction(
            context = context,
            action = "enter_food_insurance_page",
            page = "food_insurance",
            pageInfo = mapOf(
                "title" to "订单保障",
                "screen_name" to "FoodInsuranceScreen"
            ),
            extraData = mapOf(
                "order_id" to orderId,
                "restaurant_name" to (order?.restaurantName ?: ""),
                "source" to "order_detail"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("订单保障") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 订单信息头部
            item {
                OrderHeaderSection(order = order)
            }

            // 超时免单权益
            item {
                OvertimeFreeSection()
            }

            // 食无忧理赔
            item {
                FoodInsuranceSection(
                    context = context,
                    orderId = orderId,
                    order = order,
                    onApplyClaim = { showSuccessDialog = true }
                )
            }

            // 慢必赔
            item {
                SlowCompensationSection()
            }

            // 底部提示
            item {
                BottomTipSection()
            }
        }
    }

    // 申请成功弹窗
    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = { showSuccessDialog = false }
        )
    }
}

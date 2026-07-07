package com.example.eleme_sim.ui.orderdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        containerColor = Color(0xFFF7F7F5),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color(0xFF2F3135)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "更多",
                            tint = Color(0xFF2F3135)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color(0xFF2F3135),
                    actionIconContentColor = Color(0xFF2F3135)
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F7F5)),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                OrderStatusHeader(order = order)
            }
            item {
                OrderActionButtons(
                    order = order,
                    onContactMerchant = { showContactMerchantBottomSheet = true },
                    onContactRider = { showContactBottomSheet = true },
                    onCancelOrder = { showCancelOrderSheet = true }
                )
            }
            item {
                FoodInsuranceEntry(navController = navController, orderId = order.orderId)
            }
            item {
                OrderProductsSection(order = order)
            }
            item {
                DeliveryInfoSection(order = order)
            }
            item {
                OrderInfoSection(order = order)
            }
            item {
                ContactCustomerServiceButton(
                    navController = navController,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }

    if (showContactBottomSheet) {
        ContactRiderBottomSheet(
            order = order,
            navController = navController,
            onDismiss = { showContactBottomSheet = false }
        )
    }

    if (showContactMerchantBottomSheet) {
        ContactMerchantBottomSheet(
            navController = navController,
            onDismiss = { showContactMerchantBottomSheet = false }
        )
    }

    if (showCancelOrderSheet) {
        CancelOrderBottomSheet(
            order = order,
            navController = navController,
            onDismiss = { showCancelOrderSheet = false },
            onCancelSuccess = { showCancelSuccessDialog = true }
        )
    }

    if (showCancelSuccessDialog) {
        CancelSuccessDialog(
            onDismiss = { showCancelSuccessDialog = false }
        )
    }
}

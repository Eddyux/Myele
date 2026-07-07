package com.example.eleme_sim.ui.customerservice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eleme_sim.data.DataRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerServiceScreen(navController: NavController, repository: DataRepository) {
    var selectedTab by remember { mutableStateOf(0) }

    val recentOrder = remember {
        repository.getOrders().firstOrNull()
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "服务大厅",
                        color = Color(0xFF151A21)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color(0xFF151A21)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        },
        bottomBar = {
            BottomButtons(navController)
        },
        containerColor = Color(0xFFF7FBFF)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFBFE6FF),
                            Color(0xFFEAF6FF),
                            Color(0xFFF8FBFF)
                        )
                    )
                )
                .padding(padding),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            item { SmartServiceBanner() }

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

            item { NotificationBar() }
            item { ServiceFunctionsGrid() }
            item {
                ServiceTabs(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
            item { HotQuestions() }
        }
    }
}

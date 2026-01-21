package com.example.myele_sim.ui.myorders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele_sim.model.Order

@Composable
fun OrderTabRow(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    TabRow(
        selectedTabIndex = listOf("全部", "待收货/使用", "待评价", "退款").indexOf(selectedTab),
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        listOf("全部", "待收货/使用", "待评价", "退款").forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab,
                        fontSize = 14.sp,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}

@Composable
fun MonthlyExpenseSection(monthlyExpense: Double) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        color = Color.White,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "10月",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "支出¥",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = String.format("%.2f", monthlyExpense),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun OrderListSection(
    orders: List<Order>,
    navController: NavController,
    context: android.content.Context
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(orders.size) { index ->
            OrderCard(
                order = orders[index],
                orderIndex = index,
                navController = navController,
                context = context
            )
        }
    }
}

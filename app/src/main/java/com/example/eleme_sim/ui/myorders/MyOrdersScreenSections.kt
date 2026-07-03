package com.example.eleme_sim.ui.myorders

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.model.Order

@Composable
fun OrderTabRow(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("全部", "待收货/使用", "待评价", "退款")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF7FAFD)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = selectedTab == tab
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tab,
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFF161B22) else Color(0xFF6D7582),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .width(if (isSelected) 34.dp else 8.dp)
                            .height(5.dp)
                            .background(
                                color = if (isSelected) Color(0xFF2CB9F8) else Color.Transparent,
                                shape = RoundedCornerShape(999.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyExpenseSection(monthlyExpense: Double) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF7FAFD)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "10月",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF151A21)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "支出",
                fontSize = 17.sp,
                color = Color(0xFF3B434F)
            )
            Text(
                text = "¥${String.format("%.2f", monthlyExpense)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF151A21)
            )
        }
    }
}

@Composable
fun OrderListSection(
    orders: List<Order>,
    navController: NavController,
    context: Context
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6FA)),
        contentPadding = PaddingValues(start = 0.dp, end = 0.dp, top = 6.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        itemsIndexed(orders) { index, order ->
            OrderCard(
                order = order,
                orderIndex = index,
                navController = navController,
                context = context
            )
        }
    }
}

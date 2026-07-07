package com.example.eleme_sim.ui.mykefu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSelectorDialog(
    onDismiss: () -> Unit,
    onOrderSelected: (com.example.eleme_sim.model.Order) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFDFEFF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "选择订单",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181C25),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val orders = listOf(
                "金长风荷叶烤鸡(已送达) - ¥10.1",
                "川香麻辣烫(进行中) - ¥35.0",
                "瑞幸咖啡(已完成) - ¥19.9"
            )

            orders.forEach { orderInfo ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            // 这里应创建真实的 Order 对象
                            onDismiss()
                        }
                        .padding(vertical = 4.dp),
                    color = Color(0xFFF5F7FB)
                ) {
                    Text(
                        text = orderInfo,
                        fontSize = 15.sp,
                        color = Color(0xFF181C25),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

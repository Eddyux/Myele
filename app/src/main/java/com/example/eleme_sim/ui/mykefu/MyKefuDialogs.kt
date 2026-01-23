package com.example.eleme_sim.ui.mykefu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "选择订单",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 示例订单列表
            val orders = listOf(
                "金长风荷叶烤鸡 (已送达) - ¥10.1",
                "川香麻辣烫 (进行中) - ¥35.0",
                "瑞幸咖啡 (已完成) - ¥19.9"
            )

            orders.forEachIndexed { index, orderInfo ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            // 这里应该创建真实的Order对象
                            onDismiss()
                        }
                        .padding(vertical = 4.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Text(
                        text = orderInfo,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

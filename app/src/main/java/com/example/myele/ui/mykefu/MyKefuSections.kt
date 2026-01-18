package com.example.myele.ui.mykefu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myele.model.Order

@Composable
fun OrderSuggestionSection(onOrderSelect: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "您是否要咨询以下订单",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 示例订单
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF9F9F9))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "金长风荷叶烤鸡",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "订单已送达",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "¥10.1",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B00)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "其他订单",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
                Button(
                    onClick = onOrderSelect,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFFF)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "是",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedOrderSection(
    order: Order,
    onOrderChange: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOrderChange() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "咨询订单：${order.orderId}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "金长风荷叶烤鸡 - ¥10.1",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "更换订单",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun ChatMessagesSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 这里可以添加聊天消息
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂时没有消息记录",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

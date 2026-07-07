package com.example.eleme_sim.ui.mykefu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eleme_sim.model.Order
import com.example.eleme_sim.ui.components.ProductImage

@Composable
fun OrderSuggestionSection(onOrderSelect: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.98f),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "您是否要咨询以下订单",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181C25)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFF5F7FB)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(78.dp)
                            .background(Color(0xFFFFF2D9), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        ProductImage(
                            productId = "prod_331",
                            productName = "荷叶烤鸡",
                            modifier = Modifier.size(70.dp),
                            size = 70.dp,
                            cornerRadius = 14.dp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "金长风荷叶烤鸡",
                            fontSize = 18.sp,
                            color = Color(0xFF181C25),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "订单已送达",
                            fontSize = 13.sp,
                            color = Color(0xFF99A3B2)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "¥10.1",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666A73)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFD8DDE6)),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Text(
                        text = "其他订单",
                        color = Color(0xFF2A2D33),
                        fontSize = 16.sp
                    )
                }
                Button(
                    onClick = onOrderSelect,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFD8DDE6)),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Text(
                        text = "是",
                        color = Color(0xFF2A2D33),
                        fontSize = 16.sp
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
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.98f),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOrderChange() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "咨询订单：${order.orderId}",
                    fontSize = 15.sp,
                    color = Color(0xFF181C25),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "金长风荷叶烤鸡 - ¥10.1",
                    fontSize = 13.sp,
                    color = Color(0xFF97A0AF)
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "更换订单",
                tint = Color(0xFF97A0AF)
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
                    color = Color(0xFF9CA7B8)
                )
            }
        }
    }
}

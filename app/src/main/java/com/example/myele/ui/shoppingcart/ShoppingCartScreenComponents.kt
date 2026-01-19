package com.example.myele.ui.shoppingcart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myele.ui.components.ProductImage

@Composable
fun TopBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "购物车",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "华中师范大学宝山学...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = { /* 编辑 */ }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "编辑",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun QuickAccessButton(text: String, icon: ImageVector) {
    Surface(
        modifier = Modifier
            .clickable { /* TODO */ },
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF00BFFF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun RestaurantCartCard(
    restaurant: RestaurantCart,
    onRestaurantSelected: (Boolean) -> Unit,
    onItemSelected: (Int, Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // 店铺标题行（带复选框）
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = restaurant.isSelected,
                    onCheckedChange = onRestaurantSelected
                )
                Text(
                    text = restaurant.restaurantName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            // 商品列表
            restaurant.items.forEachIndexed { index, item ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 商品复选框
                    Checkbox(
                        checked = item.isSelected,
                        onCheckedChange = { onItemSelected(index, it) }
                    )

                    // 商品图片
                    ProductImage(
                        productId = item.productId,
                        productName = item.productName,
                        restaurantName = restaurant.restaurantName,
                        modifier = Modifier.size(60.dp),
                        size = 60.dp,
                        cornerRadius = 8.dp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.productName,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "¥${item.price}",
                            fontSize = 16.sp,
                            color = Color(0xFFFF3366),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 数量控制
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "减少",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "${item.quantity}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "增加",
                                tint = Color(0xFF00BFFF)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnavailableItems() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF8E1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFFF6B00),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "37件无法购买商品",
                fontSize = 14.sp,
                color = Color(0xFFFF6B00)
            )
        }
    }
}

@Composable
fun BottomCheckoutBar(
    selectAll: Boolean,
    onSelectAllChanged: (Boolean) -> Unit,
    totalPrice: Double,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectAll,
                    onCheckedChange = onSelectAllChanged
                )
                Text(
                    text = "全选",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "合计:",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "¥${"%.2f".format(totalPrice)}",
                    fontSize = 18.sp,
                    color = Color(0xFFFF3366),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onCheckoutClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3366))
            ) {
                Text("一键结算")
            }
        }
    }
}

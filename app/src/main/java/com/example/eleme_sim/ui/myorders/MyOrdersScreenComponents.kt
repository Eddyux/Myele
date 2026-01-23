package com.example.eleme_sim.ui.myorders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.data.ActionLogger
import com.example.eleme_sim.model.Order
import com.example.eleme_sim.model.OrderStatus
import com.example.eleme_sim.navigation.Screen
import com.example.eleme_sim.ui.components.ProductImage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderCard(
    order: Order,
    orderIndex: Int,
    navController: NavController,
    context: android.content.Context
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                // 记录点击订单进入详情页
                ActionLogger.logAction(
                    context = context,
                    action = "navigate",
                    page = "order_detail",
                    extraData = mapOf(
                        "from_page" to "my_orders",
                        "order_id" to order.orderId,
                        "order_index" to orderIndex,
                        "is_first_order" to (orderIndex == 0),
                        "order_status" to getOrderStatusText(order.status),
                        "restaurant_name" to order.restaurantName,
                        "order_items" to order.items.map { it.productName }
                    )
                )
                navController.navigate(Screen.OrderDetail.createRoute(order.orderId))
            },
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 店铺名称和订单状态
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        // 记录点击商家名称
                        ActionLogger.logAction(
                            context = context,
                            action = "navigate_to_store",
                            page = "store_page",
                            pageInfo = mapOf(
                                "restaurant_name" to order.restaurantName,
                                "restaurant_id" to order.restaurantId
                            ),
                            extraData = mapOf(
                                "from_page" to "my_orders"
                            )
                        )
                        // 点击商家名称跳转到商家详情页
                        navController.navigate(Screen.StorePage.createRoute(order.restaurantId))
                    }
                ) {
                    Text(
                        text = order.restaurantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ">",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }

                Text(
                    text = getOrderStatusText(order.status),
                    fontSize = 14.sp,
                    color = when (order.status) {
                        OrderStatus.COMPLETED -> Color.Gray
                        OrderStatus.CANCELLED -> Color.Red
                        OrderStatus.DELIVERING -> Color(0xFF00BFFF)
                        else -> Color(0xFFFF6B00)
                    }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // 商品列表
            order.items.take(3).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        // 商品图片
                        ProductImage(
                            productId = item.productId,
                            productName = item.productName,
                            restaurantName = order.restaurantName,
                            size = 48.dp,
                            cornerRadius = 4.dp
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = item.productName,
                                fontSize = 14.sp,
                                maxLines = 1
                            )
                            if (!item.specifications.isNullOrBlank()) {
                                Text(
                                    text = item.specifications,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Text(
                        text = "x${item.quantity}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // 特殊标签（食品安全理赔等）
            if (order.canApplyInsurance) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFE3F2FD)
                    ) {
                        Text(
                            text = "食品安全理赔",
                            fontSize = 11.sp,
                            color = Color(0xFF00BFFF),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (order.deliveryTime != null) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFF3E0)
                        ) {
                            Text(
                                text = "慢必赔",
                                fontSize = 11.sp,
                                color = Color(0xFFFF6B00),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // 下单时间和实付价格
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(order.orderTime),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "实付 ¥${String.format("%.2f", order.actualAmount)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // 订单操作按钮
            if (order.status == OrderStatus.COMPLETED && !order.hasReview) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Gray
                        )
                    ) {
                        Text("评价", fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BFFF)
                        )
                    ) {
                        Text("再来一单", fontSize = 13.sp)
                    }
                }
            } else if (order.status == OrderStatus.DELIVERING) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFFF3E0)
                    ) {
                        Text(
                            text = "满1送2元",
                            fontSize = 11.sp,
                            color = Color(0xFFFF6B00),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BFFF)
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text("再来一单", fontSize = 13.sp)
                    }
                }
            }

            // 推荐商家
            if (order.status == OrderStatus.COMPLETED) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Recommend,
                            contentDescription = null,
                            tint = Color(0xFFFF6B00),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "更多推荐",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${order.restaurantName}（中南店）",
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "去看看 >",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

fun getOrderStatusText(status: OrderStatus): String {
    return when (status) {
        OrderStatus.PENDING_ACCEPT -> "待接单"
        OrderStatus.ACCEPTED -> "已接单"
        OrderStatus.PREPARING -> "制作中"
        OrderStatus.DELIVERING -> "配送中"
        OrderStatus.COMPLETED -> "已送达"
        OrderStatus.CANCELLED -> "已取消"
        OrderStatus.PENDING_REVIEW -> "待评价"
    }
}

package com.example.eleme_sim.ui.myorders

import android.content.Context
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.data.ActionLogger
import com.example.eleme_sim.model.Order
import com.example.eleme_sim.model.OrderStatus
import com.example.eleme_sim.navigation.Screen
import com.example.eleme_sim.ui.components.ProductImage
import com.example.eleme_sim.ui.components.RestaurantImage
import java.text.SimpleDateFormat
import java.util.Locale

private val OrdersCardColor = Color(0xFFFFFEFD)
private val OrdersTextPrimary = Color(0xFF1D232B)
private val OrdersTextSecondary = Color(0xFF99A1AD)
private val OrdersTextMuted = Color(0xFFB7BEC8)
private val OrdersAccent = Color(0xFF28B7F6)
private val OrdersAccentSoft = Color(0xFFE9F9FF)
private val OrdersWarning = Color(0xFFFF8A34)
private val OrdersDanger = Color(0xFFFF6257)

@Composable
fun OrderCard(
    order: Order,
    orderIndex: Int,
    navController: NavController,
    context: Context
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 9.dp)
            .clickable {
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
        shape = RoundedCornerShape(28.dp),
        color = OrdersCardColor,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
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
                            navController.navigate(Screen.StorePage.createRoute(order.restaurantId))
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RestaurantImage(
                        restaurantName = order.restaurantName,
                        size = 56.dp,
                        cornerRadius = 18.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = order.restaurantName,
                            fontSize = 16.sp,
                            lineHeight = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrdersTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = getOrderStatusText(order.status),
                        fontSize = 14.sp,
                        color = when (order.status) {
                            OrderStatus.COMPLETED -> OrdersTextSecondary
                            OrderStatus.CANCELLED -> OrdersDanger
                            OrderStatus.DELIVERING -> OrdersAccent
                            else -> OrdersWarning
                        },
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val displayItems = order.items.take(3)
            displayItems.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductImage(
                        productId = item.productId,
                        productName = item.productName,
                        restaurantName = order.restaurantName,
                        size = 108.dp,
                        cornerRadius = 20.dp
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.productName,
                            fontSize = 15.sp,
                            lineHeight = 21.sp,
                            fontWeight = FontWeight.Medium,
                            color = OrdersTextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        if (!item.specifications.isNullOrBlank()) {
                            Text(
                                text = item.specifications,
                                fontSize = 13.sp,
                                color = OrdersTextSecondary
                            )
                        }

                        if (order.canApplyInsurance) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OrderTag(
                                    text = "食品安全理赔",
                                    textColor = OrdersAccent,
                                    backgroundColor = OrdersAccentSoft
                                )
                                if (order.deliveryTime != null) {
                                    OrderTag(
                                        text = "慢必赔",
                                        textColor = OrdersWarning,
                                        backgroundColor = Color(0xFFFFF3E8)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "x${item.quantity}",
                            fontSize = 13.sp,
                            color = OrdersTextSecondary
                        )
                    }
                }

                if (index < displayItems.size - 1) {
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                Text(
                    text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(order.orderTime),
                    fontSize = 12.sp,
                    color = OrdersTextMuted
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "实付",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = " ¥${String.format("%.2f", order.actualAmount)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrdersTextPrimary
                    )
                }
            }

            if (order.status == OrderStatus.COMPLETED) {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 16.dp, bottom = 14.dp),
                    thickness = 1.dp,
                    color = Color(0xFFF1F3F6)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Recommend,
                        contentDescription = null,
                        tint = OrdersWarning,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "更多推荐",
                        fontSize = 13.sp,
                        color = OrdersTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${order.restaurantName}（中南店）",
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp,
                        color = OrdersTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "去看看",
                        fontSize = 13.sp,
                        color = OrdersTextSecondary
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = OrdersTextSecondary
                    )
                }
            }

            when {
                order.status == OrderStatus.COMPLETED && !order.hasReview -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { },
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF707985)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFDCE2E9))
                        ) {
                            Text("评价", fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = OrdersAccent
                            ),
                            border = BorderStroke(1.dp, OrdersAccent)
                        ) {
                            Text("再来一单", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                order.status == OrderStatus.DELIVERING -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OrderTag(
                            text = "满1送2元",
                            textColor = OrdersDanger,
                            backgroundColor = Color(0xFFFFF0EE)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = OrdersAccent
                            ),
                            border = BorderStroke(1.dp, OrdersAccent)
                        ) {
                            Text("再来一单", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderTag(
    text: String,
    textColor: Color,
    backgroundColor: Color
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
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

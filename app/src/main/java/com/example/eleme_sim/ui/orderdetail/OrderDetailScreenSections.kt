package com.example.eleme_sim.ui.orderdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.model.Order
import com.example.eleme_sim.model.OrderStatus
import com.example.eleme_sim.navigation.Screen
import com.example.eleme_sim.ui.components.ProductImage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderStatusHeader(order: Order) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (order.status == OrderStatus.COMPLETED) Color(0xFF4CAF50) else Color(0xFF00BFFF)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = when (order.status) {
                    OrderStatus.COMPLETED -> "订单已送达"
                    OrderStatus.DELIVERING -> "骑手正在配送"
                    OrderStatus.PREPARING -> "商家正在制作"
                    else -> "订单处理中"
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (order.status == OrderStatus.COMPLETED) "感谢信任,期待再次光临" else "预计${order.deliveryTime?.let { SimpleDateFormat("HH:mm", Locale.getDefault()).format(it) } ?: "30分钟"}送达",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun OrderActionButtons(
    order: Order,
    onContactMerchant: () -> Unit,
    onContactRider: () -> Unit,
    onCancelOrder: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // 第一行：申请售后、联系商家、联系骑士
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton("申请售后", Icons.Default.Refresh)
                ActionButton("联系商家", Icons.Default.Phone) {
                    onContactMerchant()
                }
                ActionButton("联系骑士", Icons.Default.DeliveryDining) {
                    onContactRider()
                }
            }

            // 待接单状态显示取消订单按钮
            if (order.status == OrderStatus.PENDING_ACCEPT) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ActionButton("取消订单", Icons.Default.Cancel) {
                        onCancelOrder()
                    }
                }
            }
        }
    }
}

@Composable
fun FoodInsuranceEntry(navController: NavController, orderId: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable {
                navController.navigate(Screen.FoodInsurance.createRoute(orderId))
            },
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color(0xFF00BFFF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "食无忧理赔申请",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "食物变质、存在异物或致病就医可申请",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = {
                    navController.navigate(Screen.FoodInsurance.createRoute(orderId))
                }) {
                    Text("去申请", color = Color(0xFF00BFFF), fontSize = 14.sp)
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun OrderProductsSection(order: Order) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = order.restaurantName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        ProductImage(
                            productId = item.productId,
                            productName = item.productName,
                            restaurantName = order.restaurantName,
                            size = 48.dp,
                            cornerRadius = 4.dp
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = item.productName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (!item.specifications.isNullOrBlank()) {
                                Text(
                                    text = item.specifications,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "×${item.quantity}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "¥${item.price}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (order.discountAmount > 0) {
                            Text(
                                text = "¥${item.price + 2}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                style = LocalTextStyle.current.copy(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                )
                            )
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // 费用明细
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("商品金额", fontSize = 14.sp, color = Color.Gray)
                Text(
                    "¥${String.format("%.2f", order.totalAmount)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            if (order.deliveryFee > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("配送费", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        "¥${String.format("%.2f", order.deliveryFee)}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            if (order.packagingFee > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("打包费", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        "¥${String.format("%.2f", order.packagingFee)}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            if (order.discountAmount > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("已优惠", fontSize = 14.sp, color = Color(0xFFFF3366))
                    Text(
                        "-¥${String.format("%.2f", order.discountAmount)}",
                        fontSize = 14.sp,
                        color = Color(0xFFFF3366),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("小计", fontSize = 14.sp)
                Text(
                    "¥${order.actualAmount}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DeliveryInfoSection(order: Order) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "配送信息",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow("送达时间", order.deliveryTime?.let {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(it)
            } ?: "尽快送达")
            InfoRow("收货地址", order.deliveryAddress.street)
            InfoRow("", "${order.deliveryAddress.receiverName} ${order.deliveryAddress.receiverPhone}")
            InfoRow("配送方式", "蜂鸟准时达")
            InfoRow("配送服务", "蓝骑士专送")
            InfoRow("配送骑手", order.riderPhone?.let { "周丹奎" } ?: "配送中", showArrow = true)
        }
    }
}

@Composable
fun OrderInfoSection(order: Order) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "订单信息",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow("订单号", order.orderId, showCopy = true)
            InfoRow("支付方式", "微信支付")
            InfoRow("下单时间", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(order.orderTime))
            InfoRow("备注", "依据餐量提供餐具")
        }
    }
}

@Composable
fun ContactCustomerServiceButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(Screen.MyKefu.route) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(Icons.Default.Support, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("联系客服", fontSize = 16.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactRiderBottomSheet(
    order: Order,
    navController: NavController,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            TextButton(
                onClick = {
                    onDismiss()
                    // 将订单状态转换为中文字符串
                    val orderStatusText = when (order.status) {
                        OrderStatus.COMPLETED -> "已完成"
                        OrderStatus.DELIVERING -> "配送中"
                        OrderStatus.PREPARING -> "待接单"
                        OrderStatus.PENDING_ACCEPT -> "待接单"
                        else -> "处理中"
                    }
                    navController.navigate(Screen.OnlineChat.createRoute(orderStatusText))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("在线联系", fontSize = 16.sp)
            }
            HorizontalDivider()
            TextButton(
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("电话联系", fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactMerchantBottomSheet(
    navController: NavController,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            TextButton(
                onClick = {
                    onDismiss()
                    navController.navigate(Screen.MerchantChat.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("在线联系", fontSize = 16.sp)
            }
            HorizontalDivider()
            TextButton(
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("电话联系", fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelOrderBottomSheet(
    order: Order,
    navController: NavController,
    onDismiss: () -> Unit,
    onCancelSuccess: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "选择取消原因",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val cancelReasons = listOf(
                "不想等了",
                "点错了",
                "商家不接单",
                "配送时间太长",
                "其他原因"
            )

            cancelReasons.forEach { reason ->
                TextButton(
                    onClick = {
                        // 记录取消订单操作
                        com.example.eleme_sim.utils.ActionLogger.logOrderAction(
                            context = navController.context,
                            action = "cancel_order",
                            orderId = order.orderId,
                            orderStatus = "待接单",
                            cancelReason = reason,
                            extraData = mapOf("show_success_dialog" to true)
                        )

                        onDismiss()
                        onCancelSuccess()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(reason, fontSize = 16.sp, color = Color.Black)
                }
                if (reason != cancelReasons.last()) {
                    HorizontalDivider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("暂不取消", fontSize = 16.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun CancelSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "取消成功",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "订单已成功取消",
                fontSize = 16.sp,
                color = Color.Gray
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("确定", fontSize = 16.sp, color = Color(0xFF00BFFF))
            }
        }
    )
}

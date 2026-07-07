package com.example.eleme_sim.ui.orderdetail

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.model.Order
import com.example.eleme_sim.model.OrderStatus
import com.example.eleme_sim.navigation.Screen
import com.example.eleme_sim.ui.components.ProductImage
import java.text.SimpleDateFormat
import java.util.Locale

private val CardBackground = Color(0xFFFFFFFF)
private val CardStroke = Color(0xFFECE8E1)
private val PrimaryText = Color(0xFF202124)
private val SecondaryText = Color(0xFF8E8A83)
private val AccentBlue = Color(0xFF38B6FF)
private val AccentGold = Color(0xFFFFF3D7)
private val DividerColor = Color(0xFFF1EEE8)

@Composable
fun OrderStatusHeader(order: Order) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = when (order.status) {
                    OrderStatus.COMPLETED -> "订单已送达"
                    OrderStatus.DELIVERING -> "骑手正在配送"
                    OrderStatus.PREPARING -> "商家正在制作"
                    OrderStatus.PENDING_ACCEPT -> "商家待接单"
                    OrderStatus.ACCEPTED -> "商家已接单"
                    OrderStatus.CANCELLED -> "订单已取消"
                    OrderStatus.PENDING_REVIEW -> "订单待评价"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFC8C2B8),
                modifier = Modifier.size(20.dp)
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
        shape = RoundedCornerShape(24.dp),
        color = CardBackground,
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {
            Text(
                text = if (order.status == OrderStatus.COMPLETED) {
                    "感谢信任，期待再次光临"
                } else {
                    "预计${order.deliveryTime?.let { SimpleDateFormat("HH:mm", Locale.getDefault()).format(it) } ?: "30分钟"}送达"
                },
                fontSize = 15.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionButton("申请售后", Icons.Default.Refresh)
                ActionButton("联系商家", Icons.Default.Message, onClick = onContactMerchant)
                ActionButton("联系骑士", Icons.Default.DeliveryDining, onClick = onContactRider)
            }

            if (order.status == OrderStatus.PENDING_ACCEPT) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = DividerColor)
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ActionButton("取消订单", Icons.Default.Cancel, onClick = onCancelOrder)
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
            .clickable { navController.navigate(Screen.FoodInsurance.createRoute(orderId)) },
        shape = RoundedCornerShape(22.dp),
        color = CardBackground,
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AccentGold, Color(0xFFFFFBF1))))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFFEF9D28),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "食无忧理赔申请",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "如有异物变质或引起就医，可申请理赔",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = SecondaryText
                    )
                }
            }
            TextButton(onClick = { navController.navigate(Screen.FoodInsurance.createRoute(orderId)) }) {
                Text(
                    text = "去申请",
                    color = AccentBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFC6C2BB),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun OrderProductsSection(order: Order) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = CardBackground,
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp)) {
            Text(
                text = order.restaurantName,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Spacer(modifier = Modifier.height(14.dp))

            order.items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.Top
                    ) {
                        ProductImage(
                            productId = item.productId,
                            productName = item.productName,
                            restaurantName = order.restaurantName,
                            size = 64.dp,
                            cornerRadius = 12.dp
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.productName,
                                fontSize = 15.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight.Medium,
                                color = PrimaryText
                            )
                            if (!item.specifications.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.specifications,
                                    fontSize = 12.sp,
                                    color = SecondaryText
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "×${item.quantity}",
                                fontSize = 12.sp,
                                color = SecondaryText
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = formatPrice(item.price),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        if (order.discountAmount > 0) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = formatPrice(item.price + 2),
                                fontSize = 12.sp,
                                color = Color(0xFFB5B1AA),
                                style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
                            )
                        }
                    }
                }

                if (index != order.items.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(16.dp))

            PriceDetailRow(label = "商品金额", value = "¥${String.format(Locale.US, "%.2f", order.totalAmount)}")
            if (order.deliveryFee > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                PriceDetailRow(label = "配送费", value = "¥${String.format(Locale.US, "%.2f", order.deliveryFee)}")
            }
            if (order.packagingFee > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                PriceDetailRow(label = "打包费", value = "¥${String.format(Locale.US, "%.2f", order.packagingFee)}")
            }
            if (order.discountAmount > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                PriceDetailRow(
                    label = "已优惠",
                    value = "-¥${String.format(Locale.US, "%.2f", order.discountAmount)}",
                    valueColor = Color(0xFFE87262)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            PriceSummaryLine(
                discountAmount = order.discountAmount,
                actualAmount = order.actualAmount
            )
        }
    }
}

@Composable
fun DeliveryInfoSection(order: Order) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = CardBackground,
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp)) {
            SectionTitle(text = "配送信息")
            Spacer(modifier = Modifier.height(18.dp))

            InfoRow("送达时间", order.deliveryTime?.let {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(it)
            } ?: "尽快送达")
            InfoRow("收货地址", order.deliveryAddress.street)
            InfoRow("", "${order.deliveryAddress.receiverName} ${order.deliveryAddress.receiverPhone}")
            InfoRow("配送方式", "蜂鸟准时达", showArrow = true)
            InfoRow("配送服务", "蓝骑士专送")
            InfoRow("配送骑手", order.riderPhone?.let { "周丹奎" } ?: "配送中", showArrow = true)
        }
    }
}

@Composable
fun OrderInfoSection(order: Order) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = CardBackground,
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, CardStroke)
    ) {
        Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp)) {
            SectionTitle(text = "订单信息")
            Spacer(modifier = Modifier.height(18.dp))

            InfoRow("订单号", order.orderId, showCopy = true)
            InfoRow("支付方式", "微信支付")
            InfoRow("下单时间", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(order.orderTime))
            InfoRow("备注", "依据餐量提供餐具")
        }
    }
}

@Composable
fun ContactCustomerServiceButton(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { navController.navigate(Screen.MyKefu.route) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CardBackground,
            contentColor = PrimaryText
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 18.dp)
    ) {
        Icon(
            imageVector = Icons.Default.HeadsetMic,
            contentDescription = null,
            tint = PrimaryText,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "联系客服",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactRiderBottomSheet(
    order: Order,
    navController: NavController,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        SheetActionGroup(
            title = "联系骑手",
            actions = listOf(
                "在线联系" to {
                    onDismiss()
                    val orderStatusText = when (order.status) {
                        OrderStatus.COMPLETED -> "已完成"
                        OrderStatus.DELIVERING -> "配送中"
                        OrderStatus.PREPARING -> "待接单"
                        OrderStatus.PENDING_ACCEPT -> "待接单"
                        OrderStatus.ACCEPTED -> "已接单"
                        OrderStatus.CANCELLED -> "已取消"
                        OrderStatus.PENDING_REVIEW -> "待评价"
                    }
                    navController.navigate(Screen.OnlineChat.createRoute(orderStatusText))
                },
                "电话联系" to { onDismiss() }
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactMerchantBottomSheet(
    navController: NavController,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        SheetActionGroup(
            title = "联系商家",
            actions = listOf(
                "在线联系" to {
                    onDismiss()
                    navController.navigate(Screen.MerchantChat.route)
                },
                "电话联系" to { onDismiss() }
            )
        )
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
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "选择取消原因",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            val cancelReasons = listOf(
                "不想等了",
                "点错了",
                "商家不接单",
                "配送时间太长",
                "其他原因"
            )

            cancelReasons.forEachIndexed { index, reason ->
                TextButton(
                    onClick = {
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
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 10.dp)
                ) {
                    Text(reason, fontSize = 16.sp, color = PrimaryText)
                }
                if (index != cancelReasons.lastIndex) {
                    HorizontalDivider(color = DividerColor)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("暂不取消", fontSize = 16.sp, color = SecondaryText)
            }
        }
    }
}

@Composable
fun CancelSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardBackground,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF57B970),
                modifier = Modifier.size(44.dp)
            )
        },
        title = {
            Text(
                text = "取消成功",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
        },
        text = {
            Text(
                text = "订单已成功取消",
                fontSize = 15.sp,
                color = SecondaryText
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("确定", fontSize = 15.sp, color = AccentBlue)
            }
        }
    )
}

private fun formatPrice(price: Double): String {
    return if (price % 1.0 == 0.0) {
        "¥${price.toInt()}"
    } else {
        "¥${String.format(Locale.US, "%.2f", price).trimEnd('0').trimEnd('.')}"
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryText
    )
}

@Composable
private fun PriceDetailRow(
    label: String,
    value: String,
    valueColor: Color = SecondaryText
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = SecondaryText
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = valueColor
        )
    }
}

@Composable
private fun SheetActionGroup(
    title: String,
    actions: List<Pair<String, () -> Unit>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        actions.forEachIndexed { index, action ->
            TextButton(
                onClick = action.second,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 10.dp)
            ) {
                Text(action.first, fontSize = 16.sp, color = PrimaryText)
            }
            if (index != actions.lastIndex) {
                HorizontalDivider(color = DividerColor)
            }
        }
    }
}

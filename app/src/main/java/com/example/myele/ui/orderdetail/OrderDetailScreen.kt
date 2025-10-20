package com.example.myele.ui.orderdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.model.Order
import com.example.myele.model.OrderStatus
import com.example.myele.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavController,
    repository: DataRepository,
    orderId: String
) {
    // 从repository获取订单详情
    val order = remember { repository.getOrders().find { it.orderId == orderId } }
    var showContactBottomSheet by remember { mutableStateOf(false) }

    if (order == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("订单不存在")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 订单状态头部
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (order.status == OrderStatus.COMPLETED) Color(0xFF4CAF50) else Color(0xFF00BFFF)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = when (order.status) {
                                OrderStatus.COMPLETED -> "订单已送达"
                                OrderStatus.DELIVERING -> "骑手正在配送"
                                OrderStatus.PREPARING -> "商家正在制作"
                                else -> "订单处理中"
                            },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (order.status == OrderStatus.COMPLETED) "感谢信任,期待再次光临" else "预计${order.deliveryTime?.let { SimpleDateFormat("HH:mm", Locale.getDefault()).format(it) } ?: "30分钟"}送达",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // 操作按钮
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionButton("申请售后", Icons.Default.Refresh)
                        ActionButton("联系商家", Icons.Default.Phone)
                        ActionButton("联系骑士", Icons.Default.DeliveryDining) {
                            showContactBottomSheet = true
                        }
                    }
                }
            }

            // 食无忧理赔
            if (order.canApplyInsurance) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "食无忧理赔申请",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "如有异物质量或引起就医,可申请理赔",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            TextButton(onClick = { }) {
                                Text("去申请", color = Color(0xFF00BFFF))
                            }
                        }
                    }
                }
            }

            // 订单商品信息
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = order.restaurantName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        order.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(modifier = Modifier.weight(1f)) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(Color.LightGray, RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Restaurant,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

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

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        // 费用明细
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("已优惠", fontSize = 14.sp, color = Color(0xFFFF3366))
                            Text(
                                "¥${order.discountAmount}",
                                fontSize = 14.sp,
                                color = Color(0xFFFF3366),
                                fontWeight = FontWeight.Bold
                            )
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

            // 配送信息
            item {
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

            // 订单信息
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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

            // 联系客服按钮
            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
        }
    }

    // 联系骑手底部弹窗
    if (showContactBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showContactBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                TextButton(
                    onClick = {
                        showContactBottomSheet = false
                        navController.navigate(Screen.OnlineChat.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("在线联系", fontSize = 16.sp)
                }
                Divider()
                TextButton(
                    onClick = { showContactBottomSheet = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("电话联系", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun ActionButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color(0xFF00BFFF),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, fontSize = 13.sp, color = Color.Black)
    }
}

@Composable
fun InfoRow(label: String, value: String, showArrow: Boolean = false, showCopy: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.width(80.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(80.dp))
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = if (label == "收货地址") 2 else 1
            )

            if (showCopy) {
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { }) {
                    Text("复制", fontSize = 12.sp, color = Color(0xFF00BFFF))
                }
            }

            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

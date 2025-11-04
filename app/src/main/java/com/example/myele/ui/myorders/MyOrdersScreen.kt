package com.example.myele.ui.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.data.ActionLogger
import com.example.myele.model.Order
import com.example.myele.model.OrderStatus
import com.example.myele.navigation.Screen
import com.example.myele.ui.components.ProductImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(navController: NavController, repository: DataRepository) {
    val context = LocalContext.current
    val presenter = remember { MyOrdersPresenter(repository) }

    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var monthlyExpense by remember { mutableStateOf(0.0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("全部") }

    // 记录进入我的订单-全部页面
    LaunchedEffect(Unit) {
        ActionLogger.logAction(
            context = context,
            action = "enter_orders_page",
            page = "orders",
            pageInfo = mapOf(
                "screen_name" to "MyOrdersScreen"
            ),
            extraData = mapOf(
                "selected_tab" to "全部",
                "source" to "profile"
            )
        )
    }

    val view = remember {
        object : MyOrdersContract.View {
            override fun showOrders(orderList: List<Order>) {
                orders = orderList
            }

            override fun showMonthlyExpense(amount: Double) {
                monthlyExpense = amount
            }

            override fun showLoading() {}
            override fun hideLoading() {}
            override fun showError(message: String) {}
        }
    }

    LaunchedEffect(selectedTab) {
        presenter.attachView(view)
        presenter.loadOrders(selectedTab)
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.detachView()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (it.isNotBlank()) {
                                presenter.searchOrders(it)
                            } else {
                                presenter.loadOrders(selectedTab)
                            }
                        },
                        placeholder = { Text("搜索三年内的订单记录", fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "搜索", modifier = Modifier.size(20.dp))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = Color(0xFF00BFFF)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.WorkOutline, contentDescription = "工具箱")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 订单分类标签
            TabRow(
                selectedTabIndex = listOf("全部", "待收货/使用", "待评价", "退款").indexOf(selectedTab),
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                listOf("全部", "待收货/使用", "待评价", "退款").forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Text(
                                text = tab,
                                fontSize = 14.sp,
                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // 月度支出展示
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "10月",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "支出¥",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = String.format("%.2f", monthlyExpense),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            // 订单列表
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(orders) { order ->
                    OrderCard(order, navController)
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.restaurantName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (order.status == OrderStatus.COMPLETED) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.LightGray
                        ) {
                            Text(
                                text = "本店已休息",
                                fontSize = 10.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
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

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "¥${item.price}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "共${item.quantity}件",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
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

            // 下单时间
            Text(
                text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(order.orderTime),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )

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

package com.example.myele.ui.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.ActionLogger
import com.example.myele.data.DataRepository
import com.example.myele.model.Order

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
                        placeholder = { Text("", fontSize = 14.sp) },
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
            OrderTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // 月度支出展示
            MonthlyExpenseSection(monthlyExpense = monthlyExpense)

            // 订单列表
            OrderListSection(
                orders = orders,
                navController = navController,
                context = context
            )
        }
    }
}

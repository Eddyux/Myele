package com.example.eleme_sim.ui.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.data.ActionLogger
import com.example.eleme_sim.data.DataRepository
import com.example.eleme_sim.model.Order

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
                        placeholder = { Text("搜索三年内的订单记录", fontSize = 14.sp, color = Color(0xFFAAAAAA)) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "搜索", modifier = Modifier.size(20.dp), tint = Color(0xFF999999))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedTextColor = Color(0xFF333333),
                            focusedTextColor = Color(0xFF333333),
                            cursorColor = Color(0xFF0091FF)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 14.sp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.WorkOutline, contentDescription = "工具箱", tint = Color.Black)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF7FAFD)
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF3F6FA))
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

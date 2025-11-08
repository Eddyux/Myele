package com.example.myele.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.data.ActionLogger
import com.example.myele.model.Restaurant
import com.example.myele.model.RestaurantFeature
import com.example.myele.ui.components.RestaurantImage
import com.example.myele.ui.components.HotDealImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { DataRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var allRestaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var displayedRestaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(1) } // 默认选中"推荐"（索引1）
    var isRefreshing by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    var currentFilterOptions by remember { mutableStateOf(HomeFilterOptions()) }
    var currentSortType by remember { mutableStateOf(HomeSortType.COMPREHENSIVE) }
    var showRefreshDialog by remember { mutableStateOf(false) } // 换一换加载弹窗

    // 跟踪哪个功能按钮被选中
    var selectedFunctionButton by remember { mutableStateOf<String?>(null) }

    val tabs = listOf("常点", "推荐")

    // 应用筛选和排序
    fun applyFilterAndSort() {
        var filteredRestaurants = allRestaurants

        // 优惠活动筛选
        if (currentFilterOptions.promotions.isNotEmpty()) {
            filteredRestaurants = filteredRestaurants.filter { restaurant ->
                currentFilterOptions.promotions.any { promotion ->
                    when (promotion) {
                        "首次光顾减" -> restaurant.hasFirstOrderDiscount
                        "满减优惠" -> restaurant.hasFullReduction || restaurant.coupons.isNotEmpty()
                        "下单返红包" -> restaurant.hasRedPacketReward
                        "配送费优惠" -> restaurant.hasFreeDelivery || restaurant.deliveryFee < 3.0
                        "特价商品" -> restaurant.hasSpecialOffer
                        "0元起送" -> restaurant.minDeliveryAmount == 0.0
                        else -> false
                    }
                }
            }
        }

        // 商家特色筛选
        if (currentFilterOptions.features.isNotEmpty()) {
            filteredRestaurants = filteredRestaurants.filter { restaurant ->
                currentFilterOptions.features.any { feature ->
                    when (feature) {
                        "蜂鸟准时达" -> RestaurantFeature.FENGNIAO_DELIVERY in restaurant.features
                        "到店自取" -> RestaurantFeature.SELF_PICKUP in restaurant.features
                        "品牌商家" -> RestaurantFeature.BRAND_MERCHANT in restaurant.features || restaurant.rating >= 4.5
                        "新店" -> RestaurantFeature.NEW_STORE in restaurant.features
                        "食无忧" -> RestaurantFeature.FOOD_SAFETY in restaurant.features
                        "跨天预订" -> RestaurantFeature.CROSS_DAY_BOOKING in restaurant.features
                        "线上开票" -> RestaurantFeature.ONLINE_INVOICE in restaurant.features
                        "慢必赔" -> RestaurantFeature.SLOW_MUST_COMPENSATE in restaurant.features
                        else -> false
                    }
                }
            }
        }

        // 价格筛选
        currentFilterOptions.priceRange?.let { priceRange ->
            val (minPrice, maxPrice) = priceRange
            filteredRestaurants = filteredRestaurants.filter { restaurant ->
                restaurant.averagePrice >= minPrice.toDouble() && restaurant.averagePrice <= maxPrice.toDouble()
            }
        }

        // 应用排序
        val sortedRestaurants = when (currentSortType) {
            HomeSortType.COMPREHENSIVE -> filteredRestaurants
            HomeSortType.PRICE_LOW_TO_HIGH -> filteredRestaurants.sortedBy { it.averagePrice }
            HomeSortType.DISTANCE -> filteredRestaurants.sortedBy { it.distance }
            HomeSortType.RATING -> filteredRestaurants.sortedByDescending { it.rating }
            HomeSortType.SALES -> filteredRestaurants.sortedByDescending { it.salesVolume }
        }

        displayedRestaurants = sortedRestaurants
    }

    // 应用筛选
    fun applyFilter(filterOptions: HomeFilterOptions) {
        currentFilterOptions = filterOptions
        applyFilterAndSort()
    }

    // 应用排序
    fun applySort(sortType: HomeSortType) {
        currentSortType = sortType
        applyFilterAndSort()
    }

    // 刷新函数
    fun refreshData() {
        coroutineScope.launch {
            isRefreshing = true

            // 记录刷新操作
            ActionLogger.logAction(
                context = context,
                action = "refresh_page",
                page = "home",
                pageInfo = mapOf(
                    "screen_name" to "HomeScreen"
                ),
                extraData = mapOf(
                    "refresh_type" to "pull_to_refresh"
                )
            )

            delay(1000)
            allRestaurants = repository.loadRestaurants()
            applyFilterAndSort()
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        isLoading = true
        allRestaurants = repository.loadRestaurants()
        displayedRestaurants = allRestaurants
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标签栏和地址
        TopTabBar(
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onRefresh = { refreshData() },
            isRefreshing = isRefreshing,
            onAddressClick = { navController.navigate(com.example.myele.navigation.Screen.MyAddresses.route) }
        )

        // 搜索栏
        SearchBar(onSearchClick = { navController.navigate(com.example.myele.navigation.Screen.Search.route) })

        // 内容区域 - 根据选中的标签显示不同内容
        when (selectedTab) {
            0 -> {
                // 常点页面
                FrequentlyOrderedPage(
                    restaurants = displayedRestaurants,
                    navController = navController,
                    isLoading = isLoading,
                    isRefreshing = isRefreshing,
                    onRefresh = { refreshData() }
                )
            }
            1 -> {
                // 推荐页面
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { refreshData() },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 服务入口图标
                        item {
                            ServiceIcons(navController)
                        }

                        // 爆品团推荐
                        item {
                            ProductRecommendation(navController)
                        }

                        // 功能按钮行
                        item {
                            FunctionButtons(
                                selectedButton = selectedFunctionButton,
                                onFilterClick = { showFilterDialog = true },
                                onRefreshClick = {
                                    // 换一换：打乱列表
                                    selectedFunctionButton = "换一换"
                                    showRefreshDialog = true
                                    coroutineScope.launch {
                                        delay(800) // 显示加载动画
                                        displayedRestaurants = displayedRestaurants.shuffled()
                                        showRefreshDialog = false
                                    }
                                },
                                onRedPacketClick = {
                                    // 天天爆红包：有返红包的排前面
                                    selectedFunctionButton = "天天爆红包"
                                    currentSortType = HomeSortType.COMPREHENSIVE
                                    displayedRestaurants = displayedRestaurants.sortedByDescending { it.hasRedPacketReward }
                                },
                                onFreeDeliveryClick = {
                                    // 减配送费：配送费从低到高
                                    selectedFunctionButton = "减配送费"
                                    currentSortType = HomeSortType.COMPREHENSIVE
                                    displayedRestaurants = displayedRestaurants.sortedBy { it.deliveryFee }
                                },
                                onStudentClick = {
                                    // 学生特价：价格从低到高
                                    selectedFunctionButton = "学生特价"
                                    currentSortType = HomeSortType.PRICE_LOW_TO_HIGH
                                    applySort(HomeSortType.PRICE_LOW_TO_HIGH)
                                }
                            )
                        }

                        // 排序选项
                        item {
                            HomeSortOptions(
                                selectedSortType = currentSortType,
                                onSortClicked = { showSortDialog = true }
                            )
                        }

                        // 商家列表
                        item {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                RestaurantList(restaurants = displayedRestaurants, navController = navController)
                            }
                        }
                    }
                }
            }
        }

        // 筛选弹窗
        if (showFilterDialog) {
            HomeFilterDialog(
                onDismiss = { showFilterDialog = false },
                onConfirm = { filterOptions ->
                    applyFilter(filterOptions)
                    showFilterDialog = false
                }
            )
        }

        // 排序弹窗
        if (showSortDialog) {
            HomeSortDialog(
                selectedSortType = currentSortType,
                onSortTypeSelected = { sortType ->
                    applySort(sortType)
                    showSortDialog = false
                },
                onDismiss = { showSortDialog = false }
            )
        }

        // 换一换加载弹窗
        if (showRefreshDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF00BFFF)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "换一换中...",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopTabBar(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean = false,
    onAddressClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 标签
                Row {
                    tabs.forEachIndexed { index, tab ->
                        Text(
                            text = tab,
                            fontSize = 16.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) Color.Black else Color.Gray,
                            modifier = Modifier
                                .clickable { onTabSelected(index) }
                                .padding(horizontal = 8.dp)
                        )
                    }
                }

                // 地址显示和刷新按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 刷新按钮
                    IconButton(
                        onClick = onRefresh,
                        enabled = !isRefreshing,
                        modifier = Modifier.size(32.dp)
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Color.Gray
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "刷新",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 地址显示
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onAddressClick() }
                    ) {
                        Text(
                            text = "华中师范大学元宝山...",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(onSearchClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onSearchClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF00BFFF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "coco都可",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "7.5元红包",
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp,
                modifier = Modifier
                    .background(Color(0xFFFFF0F0), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSearchClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text("搜索", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun PromotionBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFF3366)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "爆火好店 必得25元",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("去领取", color = Color(0xFFFF3366), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ServiceIcons(navController: NavController) {
    val services = listOf(
        ServiceItem("美食外卖", Icons.Default.Restaurant, Color(0xFFFF6B6B), com.example.myele.navigation.Screen.Takeout.route),
        ServiceItem("超市便利", Icons.Default.ShoppingBag, Color(0xFF4CAF50), com.example.myele.navigation.Screen.ServicePage.createRoute("超市便利")),
        ServiceItem("学生福利", Icons.Default.School, Color(0xFF2196F3), com.example.myele.navigation.Screen.ServicePage.createRoute("学生福利")),
        ServiceItem("水果买菜", Icons.Default.Eco, Color(0xFF8BC34A), com.example.myele.navigation.Screen.ServicePage.createRoute("水果买菜")),
        ServiceItem("看病买药", Icons.Default.LocalHospital, Color(0xFFFF9800), com.example.myele.navigation.Screen.ServicePage.createRoute("看病买药")),
        ServiceItem("甜品饮品", Icons.Default.Icecream, Color(0xFFE91E63), com.example.myele.navigation.Screen.ServicePage.createRoute("甜品饮品")),
        ServiceItem("天天爆红包", Icons.Default.CardGiftcard, Color(0xFFF44336), com.example.myele.navigation.Screen.ServicePage.createRoute("天天爆红包")),
        ServiceItem("爆品团", Icons.Default.Group, Color(0xFF9C27B0), com.example.myele.navigation.Screen.ServicePage.createRoute("爆品团")),
        ServiceItem("赚吃货豆", Icons.Default.Star, Color(0xFFFF5722), com.example.myele.navigation.Screen.ServicePage.createRoute("赚吃货豆")),
        ServiceItem("0元领水果", Icons.Default.LocalOffer, Color(0xFF00BCD4), com.example.myele.navigation.Screen.ServicePage.createRoute("0元领水果"))
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)  // 足够的高度确保两排图标完全显示，无需滚动
            .background(Color.White)
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(services) { service ->
            ServiceIconItem(service, navController)
        }
    }
}

data class ServiceItem(val name: String, val icon: ImageVector, val color: Color, val route: String? = null)

@Composable
fun ServiceIconItem(service: ServiceItem, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                service.route?.let { navController.navigate(it) }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(service.color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = service.icon,
                contentDescription = service.name,
                tint = service.color,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = service.name,
            fontSize = 11.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProductRecommendation(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "爆品团 免凑单 免运费",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "更多 >",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.clickable {
                    navController.navigate(
                        com.example.myele.navigation.Screen.ServicePage.createRoute("爆品团")
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) { index ->
                ProductCard(index)
            }
        }
    }
}

@Composable
fun ProductCard(imageIndex: Int = 0) {
    Surface(
        modifier = Modifier
            .width(100.dp)
            .clickable { /* TODO */ },
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column {
            HotDealImage(
                index = imageIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                size = 100.dp,
                cornerRadius = 8.dp
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "爆品团商品",
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "¥5.9",
                    fontSize = 14.sp,
                    color = Color(0xFFFF3366),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FunctionButtons(
    selectedButton: String? = null,
    onFilterClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    onRedPacketClick: () -> Unit = {},
    onFreeDeliveryClick: () -> Unit = {},
    onStudentClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FunctionButton("换一换", Icons.Default.Refresh, isSelected = selectedButton == "换一换", onClick = onRefreshClick)
            FunctionButton("天天爆红包", Icons.Default.CardGiftcard, isSelected = selectedButton == "天天爆红包", onClick = onRedPacketClick)
            FunctionButton("减配送费", Icons.Default.LocalShipping, isSelected = selectedButton == "减配送费", onClick = onFreeDeliveryClick)
            FunctionButton("学生特价", Icons.Default.School, isSelected = selectedButton == "学生特价", onClick = onStudentClick)
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "筛选",
                tint = Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onFilterClick() }
            )
        }
    }
}

@Composable
fun FunctionButton(text: String, icon: ImageVector, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFF00BFFF) else Color.Transparent,
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (isSelected) Color.White else Color(0xFF00BFFF),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color.Gray
            )
        }
    }
}

@Composable
fun RestaurantList(restaurants: List<Restaurant>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        restaurants.forEach { restaurant ->
            RestaurantCard(restaurant, navController)
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant, navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable {
                navController.navigate(
                    com.example.myele.navigation.Screen.StorePage.createRoute(restaurant.restaurantId)
                )
            },
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 80.dp,
                    cornerRadius = 8.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = restaurant.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${restaurant.rating}",
                            fontSize = 12.sp,
                            color = Color(0xFFFFB800)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "月售${restaurant.salesVolume}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "${restaurant.deliveryTime}分钟",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${restaurant.distance}km",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "起送¥${restaurant.minDeliveryAmount}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "配送¥${restaurant.deliveryFee}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            if (restaurant.coupons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF8E1), RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFFFF6B00),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "优惠活动",
                        fontSize = 11.sp,
                        color = Color(0xFFFF6B00)
                    )
                }
            }
        }
    }
}

// 首页筛选弹窗
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeFilterDialog(
    onDismiss: () -> Unit,
    onConfirm: (HomeFilterOptions) -> Unit
) {
    var selectedPromotions by remember { mutableStateOf(setOf<String>()) }
    var selectedFeatures by remember { mutableStateOf(setOf<String>()) }
    var sliderPosition by remember { mutableStateOf(0f..120f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .align(Alignment.BottomCenter)
                .clickable(enabled = false) { },
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部标题栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "筛选",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDismiss() }
                    )
                }

                HorizontalDivider()

                // 可滚动内容区域
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
            item {
                // 优惠活动
                Text(
                    text = "优惠活动",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                HomeFlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    listOf("首次光顾减", "满减优惠", "下单返红包", "配送费优惠", "特价商品", "0元起送").forEach { promotion ->
                        HomeFilterChip(
                            text = promotion,
                            isSelected = promotion in selectedPromotions,
                            onClick = {
                                selectedPromotions = if (promotion in selectedPromotions) {
                                    selectedPromotions - promotion
                                } else {
                                    selectedPromotions + promotion
                                }
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                // 商家特色
                Text(
                    text = "商家特色",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                HomeFlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    listOf("蜂鸟准时达", "到店自取", "品牌商家", "新店", "食无忧", "跨天预订", "线上开票", "慢必赔").forEach { feature ->
                        HomeFilterChip(
                            text = feature,
                            isSelected = feature in selectedFeatures,
                            onClick = {
                                selectedFeatures = if (feature in selectedFeatures) {
                                    selectedFeatures - feature
                                } else {
                                    selectedFeatures + feature
                                }
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                // 价格筛选
                Text(
                    text = "价格筛选",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                HomePriceRangeSlider(
                    sliderPosition = sliderPosition,
                    onValueChange = { sliderPosition = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

                // 底部按钮 - 固定在底部
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                selectedPromotions = setOf()
                                selectedFeatures = setOf()
                                sliderPosition = 0f..120f
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE3F2FD)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = "清空",
                                color = Color(0xFF00BFFF),
                                fontSize = 16.sp
                            )
                        }
                        Button(
                            onClick = {
                                val filterOptions = HomeFilterOptions(
                                    promotions = selectedPromotions,
                                    features = selectedFeatures,
                                    priceRange = if (sliderPosition != 0f..120f) {
                                        Pair(sliderPosition.start, sliderPosition.endInclusive)
                                    } else null
                                )
                                onConfirm(filterOptions)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00BFFF)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            val selectedCount = selectedPromotions.size + selectedFeatures.size +
                                if (sliderPosition != 0f..120f) 1 else 0
                            Text(
                                text = "查看(已选$selectedCount)",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFF00BFFF) else Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeFlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePriceRangeSlider(
    sliderPosition: ClosedFloatingPointRange<Float> = 0f..120f,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "¥${sliderPosition.start.toInt()}",
                fontSize = 14.sp,
                color = Color(0xFF00BFFF),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (sliderPosition.endInclusive >= 120f) "¥120+" else "¥${sliderPosition.endInclusive.toInt()}",
                fontSize = 14.sp,
                color = Color(0xFF00BFFF),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = sliderPosition,
            onValueChange = onValueChange,
            valueRange = 0f..120f,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00BFFF),
                activeTrackColor = Color(0xFF00BFFF),
                inactiveTrackColor = Color(0xFFE0E0E0)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// 常点页面
@Composable
fun FrequentlyOrderedPage(
    restaurants: List<Restaurant>,
    navController: NavController,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            item {
                // 常点页面标题
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFFF6B6B),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "常点商家",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "您经常点的商家会显示在这里",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 显示常点的商家（这里显示已收藏的商家）
            item {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val frequentRestaurants = restaurants.filter { it.isFavorite }
                    if (frequentRestaurants.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "暂无常点商家",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            frequentRestaurants.forEach { restaurant ->
                                RestaurantCard(restaurant, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 主页筛选选项数据类
 */
data class HomeFilterOptions(
    val promotions: Set<String> = setOf(),
    val features: Set<String> = setOf(),
    val priceRange: Pair<Float, Float>? = null
)

/**
 * 主页排序类型
 */
enum class HomeSortType {
    COMPREHENSIVE,      // 综合排序
    PRICE_LOW_TO_HIGH, // 人均价低到高
    DISTANCE,          // 距离优先
    RATING,            // 商家好评优先
    SALES              // 销量优先
}

// 主页排序选项
@Composable
fun HomeSortOptions(
    selectedSortType: HomeSortType,
    onSortClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onSortClicked() }
            ) {
                Text(
                    text = when (selectedSortType) {
                        HomeSortType.COMPREHENSIVE -> "综合排序"
                        HomeSortType.PRICE_LOW_TO_HIGH -> "人均价低到高"
                        HomeSortType.DISTANCE -> "距离优先"
                        HomeSortType.RATING -> "商家好评优先"
                        HomeSortType.SALES -> "销量优先"
                    },
                    fontSize = 14.sp,
                    color = Color(0xFF00BFFF),
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF00BFFF),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// 主页排序弹窗
@Composable
fun HomeSortDialog(
    selectedSortType: HomeSortType,
    onSortTypeSelected: (HomeSortType) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 280.dp)
                .clickable(enabled = false) { },
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                HomeSortDialogOption(
                    text = "综合排序",
                    isSelected = selectedSortType == HomeSortType.COMPREHENSIVE,
                    onClick = { onSortTypeSelected(HomeSortType.COMPREHENSIVE) }
                )
                HomeSortDialogOption(
                    text = "人均价低到高",
                    isSelected = selectedSortType == HomeSortType.PRICE_LOW_TO_HIGH,
                    onClick = { onSortTypeSelected(HomeSortType.PRICE_LOW_TO_HIGH) }
                )
                HomeSortDialogOption(
                    text = "距离优先",
                    isSelected = selectedSortType == HomeSortType.DISTANCE,
                    onClick = { onSortTypeSelected(HomeSortType.DISTANCE) }
                )
                HomeSortDialogOption(
                    text = "商家好评优先",
                    isSelected = selectedSortType == HomeSortType.RATING,
                    onClick = { onSortTypeSelected(HomeSortType.RATING) }
                )
                HomeSortDialogOption(
                    text = "销量优先",
                    isSelected = selectedSortType == HomeSortType.SALES,
                    onClick = { onSortTypeSelected(HomeSortType.SALES) }
                )
            }
        }
    }
}

@Composable
fun HomeSortDialogOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (isSelected) Color(0xFF00BFFF) else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF00BFFF)
            )
        }
    }
}


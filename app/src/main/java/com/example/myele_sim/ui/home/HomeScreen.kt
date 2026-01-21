package com.example.myele_sim.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myele_sim.data.DataRepository
import com.example.myele_sim.data.ActionLogger
import com.example.myele_sim.model.Restaurant
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

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
    var currentFilterOptions by remember { mutableStateOf(HomeFilterOptions()) }
    var showRefreshDialog by remember { mutableStateOf(false) } // 换一换加载弹窗

    // 跟踪哪个功能按钮被选中
    var selectedFunctionButton by remember { mutableStateOf<String?>(null) }

    val tabs = listOf("常点", "推荐")

    // 应用筛选
    fun applyFilter() {
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
                        "蜂鸟准时达" -> com.example.myele_sim.model.RestaurantFeature.FENGNIAO_DELIVERY in restaurant.features
                        "到店自取" -> com.example.myele_sim.model.RestaurantFeature.SELF_PICKUP in restaurant.features
                        "品牌商家" -> com.example.myele_sim.model.RestaurantFeature.BRAND_MERCHANT in restaurant.features || restaurant.rating >= 4.5
                        "新店" -> com.example.myele_sim.model.RestaurantFeature.NEW_STORE in restaurant.features
                        "食无忧" -> com.example.myele_sim.model.RestaurantFeature.FOOD_SAFETY in restaurant.features
                        "跨天预订" -> com.example.myele_sim.model.RestaurantFeature.CROSS_DAY_BOOKING in restaurant.features
                        "线上开票" -> com.example.myele_sim.model.RestaurantFeature.ONLINE_INVOICE in restaurant.features
                        "慢必赔" -> com.example.myele_sim.model.RestaurantFeature.SLOW_MUST_COMPENSATE in restaurant.features
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

        displayedRestaurants = filteredRestaurants
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
            applyFilter()
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
            onAddressClick = { navController.navigate(com.example.myele_sim.navigation.Screen.MyAddresses.route) }
        )

        // 搜索栏
        SearchBar(onSearchClick = { navController.navigate(com.example.myele_sim.navigation.Screen.Search.route) })

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
                                    displayedRestaurants = displayedRestaurants.sortedByDescending { it.hasRedPacketReward }
                                },
                                onFreeDeliveryClick = {
                                    // 减配送费：配送费从低到高
                                    selectedFunctionButton = "减配送费"
                                    displayedRestaurants = displayedRestaurants.sortedBy { it.deliveryFee }
                                },
                                onStudentClick = {
                                    // 学生特价：价格从低到高
                                    selectedFunctionButton = "学生特价"
                                    displayedRestaurants = displayedRestaurants.sortedBy { it.averagePrice }
                                }
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
                    currentFilterOptions = filterOptions
                    applyFilter()
                    showFilterDialog = false
                }
            )
        }

        // 换一换加载弹窗
        if (showRefreshDialog) {
            RefreshLoadingDialog()
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

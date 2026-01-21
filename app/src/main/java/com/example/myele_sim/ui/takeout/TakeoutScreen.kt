package com.example.myele_sim.ui.takeout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele_sim.model.Restaurant
import com.example.myele_sim.data.ActionLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TakeoutScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var restaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("精选") }
    var showSortDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedSortType by remember { mutableStateOf(SortType.COMPREHENSIVE) }
    var showShuffleLoading by remember { mutableStateOf(false) }
    var selectedFunctionButton by remember { mutableStateOf<String?>(null) }
    var speedSelected by remember { mutableStateOf(false) }

    val presenter = remember {
        TakeoutPresenter(object : TakeoutContract.View {
            override fun updateRestaurants(newRestaurants: List<Restaurant>) {
                restaurants = newRestaurants
            }

            override fun showLoading() {
                isLoading = true
            }

            override fun hideLoading() {
                isLoading = false
            }
        }, context)
    }

    LaunchedEffect(Unit) {
        presenter.onViewCreated()
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // 顶部标题栏
            TopBar(onBackClicked = { navController.popBackStack() })

            // 整页滚动，精选那一栏固定
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 搜索栏
                    item {
                        SearchBar()
                    }

                    // 分类图标（精选那一栏）- 固定在顶部
                    stickyHeader {
                        CategoryIcons(
                            selectedCategory = selectedCategory,
                            onCategorySelected = {
                                selectedCategory = it
                                presenter.onCategorySelected(it)
                            }
                        )
                    }

                    // 广告横幅
                    item {
                        PromotionBanner()
                    }

                    // 排序和筛选
                    item {
                        SortAndFilter(
                            selectedSortType = selectedSortType,
                            speedSelected = speedSelected,
                            selectedFunctionButton = selectedFunctionButton,
                            onSortClicked = { showSortDialog = true },
                            onFilterClicked = { showFilterDialog = true },
                            onSpeedClicked = {
                                // 速度优先：按配送时间排序
                                speedSelected = !speedSelected
                                selectedFunctionButton = null
                                presenter.sortByDeliveryTime()
                            },
                            onShuffleClicked = {
                                // 换一换：随机打乱餐厅列表
                                selectedFunctionButton = "换一换"
                                speedSelected = false
                                showShuffleLoading = true
                                presenter.shuffleRestaurants()
                                // 延迟隐藏加载弹窗，让用户看到效果
                                coroutineScope.launch {
                                    delay(500)
                                    showShuffleLoading = false
                                }
                            },
                            onRedPacketClicked = {
                                // 天天爆红包：按红包优惠排序
                                selectedFunctionButton = "天天爆红包"
                                speedSelected = false
                                presenter.sortByRedPacket()
                            },
                            onDeliveryFeeClicked = {
                                // 减配送费：按配送费排序
                                selectedFunctionButton = "减配送费"
                                speedSelected = false
                                presenter.sortByDeliveryFee()
                            },
                            onNoThresholdClicked = {
                                // 无门槛红包：按起送价排序
                                selectedFunctionButton = "无门槛红包"
                                speedSelected = false
                                presenter.sortByNoThreshold()
                            }
                        )
                    }

                    // 餐厅列表
                    items(restaurants) { restaurant ->
                        TakeoutRestaurantCard(
                            restaurant = restaurant,
                            navController = navController
                        )
                    }
                }
            }
        }

        // 排序弹窗
        if (showSortDialog) {
            SortDialog(
                selectedSortType = selectedSortType,
                onSortTypeSelected = { sortType ->
                    selectedSortType = sortType
                    presenter.onSortChanged(sortType)
                    showSortDialog = false

                    // 记录排序选择操作
                    val sortOptionName = when (sortType) {
                        SortType.RATING -> "好评优先"
                        SortType.DELIVERY_SPEED -> "配送最快"
                        SortType.COMPREHENSIVE -> "综合排序"
                        SortType.PRICE_LOW_TO_HIGH -> "人均价低到高"
                        SortType.DISTANCE -> "距离优先"
                        SortType.MIN_DELIVERY -> "起送低到高"
                    }

                    ActionLogger.logAction(
                        context = context,
                        action = "select_sort_option",
                        page = "takeout",
                        extraData = mapOf(
                            "sort_option" to sortOptionName,
                            "sort_type" to "综合排序"
                        )
                    )
                },
                onDismiss = { showSortDialog = false }
            )
        }

        // 筛选弹窗
        if (showFilterDialog) {
            FilterDialog(
                onDismiss = { showFilterDialog = false },
                onConfirm = { filterOptions ->
                    presenter.applyFilter(filterOptions)
                    showFilterDialog = false
                }
            )
        }

        // 换一换加载弹窗
        if (showShuffleLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF00BFFF)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "正在换一换...",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

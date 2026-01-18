package com.example.myele.ui.searchresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myele.model.Restaurant

@Composable
fun SearchResultScreen(navController: NavController, keyword: String) {
    val context = LocalContext.current
    var restaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedSortType by remember { mutableStateOf(SortType.COMPREHENSIVE) }
    var showSortDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var salesSelected by remember { mutableStateOf(false) }
    var speedSelected by remember { mutableStateOf(false) }

    val presenter = remember {
        SearchResultPresenter(object : SearchResultContract.View {
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

    LaunchedEffect(keyword) {
        presenter.onViewCreated(keyword)
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
            // 顶部搜索栏
            TopBar(
                keyword = keyword,
                onBackClicked = { navController.popBackStack() },
                navController = navController
            )

            // 排序选项
            SortOptions(
                selectedSortType = selectedSortType,
                salesSelected = salesSelected,
                speedSelected = speedSelected,
                onSortClicked = { showSortDialog = true },
                onFilterClicked = { showFilterDialog = true },
                onSalesClicked = {
                    // 销量优先：按销量排序
                    salesSelected = !salesSelected
                    speedSelected = false
                    selectedSortType = SortType.COMPREHENSIVE
                    presenter.sortBySales()
                },
                onSpeedClicked = {
                    // 速度优先：按配送时间排序
                    speedSelected = !speedSelected
                    salesSelected = false
                    selectedSortType = SortType.COMPREHENSIVE
                    presenter.sortByDeliveryTime()
                }
            )

            // 餐厅列表
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
                    items(restaurants) { restaurant ->
                        SearchResultRestaurantCard(
                            restaurant = restaurant,
                            navController = navController,
                            keyword = keyword
                        )
                    }

                    // 底部红包提示
                    item {
                        CouponBanner()
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
                },
                onDismiss = { showSortDialog = false }
            )
        }

        // 筛选弹窗
        if (showFilterDialog) {
            FilterDialog(
                keyword = keyword,
                onDismiss = { showFilterDialog = false },
                onConfirm = { filterOptions ->
                    presenter.applyFilter(filterOptions)

                    // 记录筛选操作
                    filterOptions.priceRange?.let { (min, max) ->
                        com.example.myele.utils.ActionLogger.logFilter(
                            context = navController.context,
                            page = "search_result",
                            priceMin = min.toInt(),
                            priceMax = max.toInt(),
                            otherFilters = mapOf("keyword" to keyword)
                        )
                    }

                    showFilterDialog = false
                }
            )
        }
    }
}

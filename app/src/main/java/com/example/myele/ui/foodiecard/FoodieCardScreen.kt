package com.example.myele.ui.foodiecard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.model.Coupon
import com.example.myele.model.Restaurant
import com.example.myele.model.SuperFoodieCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodieCardScreen(navController: NavController, repository: DataRepository) {
    val presenter = remember { FoodieCardPresenter(repository) }

    var foodieCards by remember { mutableStateOf<List<SuperFoodieCard>>(emptyList()) }
    var explosivePackages by remember { mutableStateOf<List<Coupon>>(emptyList()) }
    var restaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var selectedTab by remember { mutableStateOf("爆红包商家") }
    var selectedSort by remember { mutableStateOf("综合排序") }

    val view = remember {
        object : FoodieCardContract.View {
            override fun showFoodieCards(cards: List<SuperFoodieCard>) {
                foodieCards = cards
            }

            override fun showExplosivePackages(packages: List<Coupon>) {
                explosivePackages = packages
            }

            override fun showRestaurants(restaurantList: List<Restaurant>) {
                restaurants = restaurantList
            }

            override fun showLoading() {}
            override fun hideLoading() {}
            override fun showError(message: String) {}
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadData()
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.detachView()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("超级吃货卡") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "皇冠",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    TextButton(onClick = { }) {
                        Text("查看全部", fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 购买超级吃货卡板块
            item {
                PurchaseFoodieCardSection(foodieCards = foodieCards)
            }

            // 爆红包套餐列表
            item {
                ExplosivePackagesSection(explosivePackages = explosivePackages)
            }

            // 火车票神券广告
            item {
                TrainTicketAdSection()
            }

            // 商家分类标签
            item {
                MerchantCategoryTabsSection(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        selectedTab = tab
                        presenter.filterRestaurants(if (tab == "爆红包商家") "explosive" else "all")
                    }
                )
            }

            // 排序和筛选选项
            item {
                SortAndFilterSection(
                    selectedSort = selectedSort,
                    onSortSelected = { sort ->
                        selectedSort = sort
                        presenter.sortRestaurants(if (sort == "综合排序") "综合" else "人气")
                    }
                )
            }

            // 筛选标签
            item {
                FilterTagsSection()
            }

            // 餐厅列表
            items(restaurants.size) { index ->
                RestaurantCard(restaurants[index])
            }
        }
    }
}

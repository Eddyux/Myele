package com.example.eleme_sim.ui.foodiecard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.data.DataRepository
import com.example.eleme_sim.model.Coupon
import com.example.eleme_sim.model.Restaurant
import com.example.eleme_sim.model.SuperFoodieCard

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
                title = {
                    Text(
                        text = "超级吃货卡",
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Surface(
                        color = Color(0x26FFFFFF),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "皇冠",
                            tint = Color(0xFFFFD56B),
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .size(16.dp)
                        )
                    }
                    TextButton(onClick = { }) {
                        Text(
                            text = "查看全部",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0x33180F0D)
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        },
        containerColor = Color(0xFFF6F2EC)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F2EC))
                .padding(paddingValues)
        ) {
            FoodieHeroBackdrop(
                modifier = Modifier
                    .fillMaxSize()
                    .height(252.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    PurchaseFoodieCardSection(foodieCards = foodieCards)
                }

                item {
                    ExplosivePackagesSection(explosivePackages = explosivePackages)
                }

                item {
                    TrainTicketAdSection()
                }

                item {
                    MerchantCategoryTabsSection(
                        selectedTab = selectedTab,
                        onTabSelected = { tab ->
                            selectedTab = tab
                            presenter.filterRestaurants(if (tab == "爆红包商家") "explosive" else "all")
                        }
                    )
                }

                item {
                    SortAndFilterSection(
                        selectedSort = selectedSort,
                        onSortSelected = { sort ->
                            selectedSort = sort
                            presenter.sortRestaurants(if (sort == "综合排序") "综合" else "人气")
                        }
                    )
                }

                item {
                    FilterTagsSection()
                }

                items(restaurants.size) { index ->
                    RestaurantCard(restaurants[index])
                }
            }
        }
    }
}

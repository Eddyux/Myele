package com.example.myele.ui.frequentstores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.navigation.Screen

/**
 * 常点的店页面
 * 从Profile页面的"常点的店"点击进入
 * 包含"关注"和"常点"两个标签，当前为"常点"标签页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequentStoresScreen(navController: NavController, repository: DataRepository) {
    var selectedTab by remember { mutableStateOf(1) }
    var selectedFilter by remember { mutableStateOf(0) }

    // 获取常点的店铺（使用isFavorite标记）
    val frequentRestaurants = remember {
        repository.getRestaurants().filter { it.isFavorite }
    }

    // 获取关注的店铺（使用isFollowed标记）
    val followedRestaurants = remember {
        repository.getRestaurants().filter { it.isFollowed }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            modifier = Modifier.width(200.dp),
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = {
                                    navController.navigate(com.example.myele.navigation.Screen.MyFollows.route)
                                },
                                text = {
                                    Text(
                                        "关注",
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = {
                                    Text(
                                        "常点",
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp)
        ) {
            // 筛选栏
            item {
                FilterSection(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
            }

            // 间隔
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 根据选中的标签显示不同的列表
            if (selectedTab == 0) {
                // 关注标签 - 显示关注的店铺列表（类似MyFollows页面）
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFFF8E1)
                    ) {
                        Text(
                            text = "点击筛选可配送店铺",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            color = Color(0xFFFF6B00)
                        )
                    }
                }

                items(followedRestaurants) { restaurant ->
                    FollowedRestaurantCard(restaurant)
                }
            } else {
                // 常点标签 - 显示常点的店铺列表
                items(frequentRestaurants) { restaurant ->
                    FrequentStoreCard(
                        restaurant = restaurant,
                        onClick = {
                            navController.navigate(Screen.StorePage.createRoute(restaurant.restaurantId))
                        }
                    )
                }
            }

            // 满意度调查（插入在中间）
            if (frequentRestaurants.size > 2) {
                item {
                    SatisfactionSurvey()
                }
            }

            // 没有更多了
            item {
                Text(
                    text = "没有更多了",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

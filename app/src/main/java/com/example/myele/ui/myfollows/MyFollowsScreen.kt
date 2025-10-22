package com.example.myele.ui.myfollows

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
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.model.Restaurant
import com.example.myele.ui.components.RestaurantImage

/**
 * 我的关注页面
 * 从Profile页面的"我的关注"点击进入
 * 包含"关注"和"常点"两个标签，当前为"关注"标签页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFollowsScreen(navController: NavController, repository: DataRepository) {
    var selectedTab by remember { mutableStateOf(0) }
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
                                onClick = { selectedTab = 0 },
                                text = {
                                    Text(
                                        "关注",
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = {
                                    navController.navigate(com.example.myele.navigation.Screen.FrequentStores.route)
                                },
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
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
        ) {
            // 筛选提示栏
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
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

            // 关注的店铺列表
            items(followedRestaurants) { restaurant ->
                FollowedRestaurantCard(restaurant)
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

@Composable
fun FollowedRestaurantCard(restaurant: Restaurant) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 店铺图片
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 60.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = restaurant.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // 根据距离显示不同状态
                    if (restaurant.distance > 5.0) {
                        Text(
                            text = "超出配送范围",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    } else {
                        Row {
                            Text(
                                text = "${restaurant.rating}分",
                                fontSize = 12.sp,
                                color = Color(0xFFFF6B00)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "月售${restaurant.salesVolume}+",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "好评率98%",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // 优惠活动
                    if (restaurant.coupons.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Surface(
                                shape = RoundedCornerShape(2.dp),
                                color = Color(0xFFFFE0E0)
                            ) {
                                Text(
                                    text = "满20减5",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF0000),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 删除按钮
            IconButton(onClick = { /* TODO: 删除关注 */ }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = Color.Gray
                )
            }
        }
    }
}

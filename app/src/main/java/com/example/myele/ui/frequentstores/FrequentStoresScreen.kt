package com.example.myele.ui.frequentstores

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.myele.navigation.Screen
import com.example.myele.ui.components.RestaurantImage

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
            // 筛选栏
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 全部分类 - 带下拉箭头
                        FilterButton(
                            text = "全部分类",
                            selected = selectedFilter == 0,
                            hasDropdown = true,
                            onClick = { selectedFilter = 0 }
                        )

                        // 附近点过
                        FilterButton(
                            text = "附近点过",
                            selected = selectedFilter == 1,
                            hasDropdown = false,
                            onClick = { selectedFilter = 1 }
                        )

                        // 点过最多 - 带下拉箭头
                        FilterButton(
                            text = "点过最多",
                            selected = selectedFilter == 2,
                            hasDropdown = true,
                            onClick = { selectedFilter = 2 }
                        )

                        // 可配送 - 蓝色高亮
                        FilterButton(
                            text = "可配送",
                            selected = selectedFilter == 3,
                            hasDropdown = false,
                            isHighlight = true,
                            onClick = { selectedFilter = 3 }
                        )
                    }
                }
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

@Composable
fun FrequentStoreCard(
    restaurant: Restaurant,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 店铺图片
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 80.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = restaurant.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // 品牌点过次数
                    Text(
                        text = "该品牌点过3次",
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B00)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // 配送信息
                    Row {
                        Text(
                            text = "满${restaurant.minDeliveryAmount.toInt()}元",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "配送¥${restaurant.deliveryFee}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "约${restaurant.deliveryTime}分钟",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${restaurant.distance}km",
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
                                    text = "6元无门槛",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF0000),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(2.dp),
                                color = Color(0xFFFFE0E0)
                            ) {
                                Text(
                                    text = "20减6.5",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF0000),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 常点的商品（如果有）
            if (!restaurant.products.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "常点",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(restaurant.products.take(3)) { product ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(80.dp)
                        ) {
                            // 商品图片占位符
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = product.name,
                                fontSize = 11.sp,
                                color = Color.Black,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SatisfactionSurvey() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { /* TODO */ },
        color = Color(0xFFFFF8E1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color(0xFFFF6B00),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "您对常点提供的服务满意吗？",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    hasDropdown: Boolean,
    isHighlight: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isHighlight -> Color(0xFFE3F2FD)
        selected -> Color(0xFFF5F5F5)
        else -> Color.White
    }

    val textColor = when {
        isHighlight -> Color(0xFF2196F3)
        selected -> Color.Black
        else -> Color.Black
    }

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        border = if (!isHighlight) BorderStroke(1.dp, Color(0xFFE0E0E0)) else null,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (selected || isHighlight) FontWeight.Medium else FontWeight.Normal
        )

        if (hasDropdown) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
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
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = Color.Gray
                )
            }
        }
    }
}

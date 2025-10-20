package com.example.myele.ui.searchresult

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.model.Restaurant
import com.example.myele.ui.components.RestaurantImage

@Composable
fun SearchResultScreen(navController: NavController, keyword: String) {
    val context = LocalContext.current
    var restaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedSortType by remember { mutableStateOf(SortType.COMPREHENSIVE) }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部搜索栏
        TopBar(
            keyword = keyword,
            onBackClicked = { navController.popBackStack() }
        )

        // 排序选项
        SortOptions(
            selectedSortType = selectedSortType,
            onSortTypeChanged = {
                selectedSortType = it
                presenter.onSortChanged(it)
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
                    SearchResultRestaurantCard(restaurant = restaurant)
                }

                // 底部红包提示
                item {
                    CouponBanner()
                }
            }
        }
    }
}

@Composable
fun TopBar(keyword: String, onBackClicked: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.Black
                )
            }

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = keyword,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Text(
                text = "切换地址",
                color = Color(0xFF00BFFF),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun SortOptions(
    selectedSortType: SortType,
    onSortTypeChanged: (SortType) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SortOption(
                text = "综合排序",
                selected = selectedSortType == SortType.COMPREHENSIVE,
                onClick = { onSortTypeChanged(SortType.COMPREHENSIVE) },
                showArrow = true
            )
            SortOption(
                text = "销量优先",
                selected = selectedSortType == SortType.SALES,
                onClick = { onSortTypeChanged(SortType.SALES) }
            )
            SortOption(
                text = "速度优先",
                selected = selectedSortType == SortType.SPEED,
                onClick = { onSortTypeChanged(SortType.SPEED) }
            )
            SortOption(
                text = "筛选",
                selected = selectedSortType == SortType.FILTER,
                onClick = { onSortTypeChanged(SortType.FILTER) },
                showIcon = true
            )
        }
    }
}

@Composable
fun SortOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    showArrow: Boolean = false,
    showIcon: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (selected) Color(0xFF00BFFF) else Color.Black,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
        if (showArrow) {
            Icon(
                imageVector = if (selected) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = if (selected) Color(0xFF00BFFF) else Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
        if (showIcon) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = if (selected) Color(0xFF00BFFF) else Color.Gray,
                modifier = Modifier
                    .size(16.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun SearchResultRestaurantCard(restaurant: Restaurant) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { /* TODO */ },
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // 店铺信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 店铺图片
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 60.dp,
                    cornerRadius = 8.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // 店铺名称和标签
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = restaurant.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        // 蓝骑士配送标签
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFE3F2FD)
                        ) {
                            Text(
                                text = "蜂鸟专送",
                                fontSize = 10.sp,
                                color = Color(0xFF00BFFF),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 评分和销量
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "月售${restaurant.salesVolume}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "起送¥${restaurant.minDeliveryAmount}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 评分信息
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${restaurant.rating}",
                            fontSize = 12.sp,
                            color = Color(0xFFFFB800),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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
                    }
                }
            }

            // 优惠活动
            if (restaurant.coupons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(restaurant.coupons.take(3)) { coupon ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFF8E1)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = Color(0xFFFF6B00),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "满减优惠",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFF6B00),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // 商品展示区域
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "7.5元红包门槛",
                fontSize = 12.sp,
                color = Color(0xFFFF3366),
                modifier = Modifier
                    .background(Color(0xFFFFF0F0), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 商品列表
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(3) { _ ->
                    ProductItem()
                }
            }
        }
    }
}

@Composable
fun ProductItem() {
    Column(
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalDrink,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "双拼奶茶",
            fontSize = 12.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "¥14 起",
            fontSize = 12.sp,
            color = Color(0xFFFF6B00),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CouponBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF0F0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFFF3366)
                ) {
                    Text(
                        text = "¥6",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "满20可用 10-22-49后失效",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }

            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3366)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("适用商家", fontSize = 12.sp)
            }
        }
    }
}

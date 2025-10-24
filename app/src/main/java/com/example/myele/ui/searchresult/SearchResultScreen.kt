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
    var showSortDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

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
                onBackClicked = { navController.popBackStack() }
            )

            // 排序选项
            SortOptions(
                selectedSortType = selectedSortType,
                onSortClicked = { showSortDialog = true },
                onFilterClicked = { showFilterDialog = true }
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
                onDismiss = { showFilterDialog = false },
                onConfirm = { showFilterDialog = false }
            )
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
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onSortClicked() }
            ) {
                Text(
                    text = when (selectedSortType) {
                        SortType.COMPREHENSIVE -> "综合排序"
                        SortType.PRICE_LOW_TO_HIGH -> "人均价低到高"
                        SortType.DISTANCE -> "距离优先"
                        SortType.RATING -> "商家好评优先"
                        SortType.MIN_DELIVERY -> "起送低到高"
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
            Text(
                text = "销量优先",
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = "速度优先",
                fontSize = 14.sp,
                color = Color.Black
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onFilterClicked() }
            ) {
                Text(
                    text = "筛选",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 2.dp)
                )
            }
        }
    }
}

// 排序弹窗
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDialog(
    selectedSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            SortDialogOption(
                text = "综合排序",
                isSelected = selectedSortType == SortType.COMPREHENSIVE,
                onClick = { onSortTypeSelected(SortType.COMPREHENSIVE) }
            )
            SortDialogOption(
                text = "人均价低到高",
                isSelected = selectedSortType == SortType.PRICE_LOW_TO_HIGH,
                onClick = { onSortTypeSelected(SortType.PRICE_LOW_TO_HIGH) }
            )
            SortDialogOption(
                text = "距离优先",
                isSelected = selectedSortType == SortType.DISTANCE,
                onClick = { onSortTypeSelected(SortType.DISTANCE) }
            )
            SortDialogOption(
                text = "商家好评优先",
                isSelected = selectedSortType == SortType.RATING,
                onClick = { onSortTypeSelected(SortType.RATING) }
            )
            SortDialogOption(
                text = "起送低到高",
                isSelected = selectedSortType == SortType.MIN_DELIVERY,
                onClick = { onSortTypeSelected(SortType.MIN_DELIVERY) }
            )
        }
    }
}

@Composable
fun SortDialogOption(
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

// 筛选弹窗
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var selectedPromotions by remember { mutableStateOf(setOf<String>()) }
    var selectedFeatures by remember { mutableStateOf(setOf<String>()) }
    var selectedPriceRange by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
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
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    listOf("首次光顾减", "满减优惠", "下单返红包", "配送费优惠", "特价商品", "0元起送").forEach { promotion ->
                        FilterChip(
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
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    listOf("蜂鸟准时达", "到店自取", "品牌商家", "新店", "食无忧", "跨天预订", "线上开票", "慢必赔").forEach { feature ->
                        FilterChip(
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Text(
                            text = "自定义最低价",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                        )
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Text(
                            text = "自定义最高价",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(
                        "0-11元" to "3%选择",
                        "12-18元" to "45%选择",
                        "19-21元" to "38%选择",
                        "22-218元" to "14%选择"
                    ).forEach { (range, percentage) ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedPriceRange = if (selectedPriceRange == range) null else range
                                }
                        ) {
                            Text(
                                text = range,
                                fontSize = 14.sp,
                                color = if (selectedPriceRange == range) Color(0xFF00BFFF) else Color.Black,
                                fontWeight = if (selectedPriceRange == range) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                text = percentage,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                // 底部按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            selectedPromotions = setOf()
                            selectedFeatures = setOf()
                            selectedPriceRange = null
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
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BFFF)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "查看(已选${selectedPromotions.size + selectedFeatures.size + if (selectedPriceRange != null) 1 else 0})",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
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
fun FlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier
    ) {
        content()
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

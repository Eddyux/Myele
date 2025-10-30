package com.example.myele.ui.takeout

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.myele.model.Restaurant
import com.example.myele.ui.components.RestaurantImage

@Composable
fun TakeoutScreen(navController: NavController) {
    val context = LocalContext.current
    var restaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("精选") }
    var showSortDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedSortType by remember { mutableStateOf(SortType.COMPREHENSIVE) }

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

            // 搜索栏
            SearchBar()

            // 分类图标
            CategoryIcons(
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                    presenter.onCategorySelected(it)
                }
            )

            // 广告横幅
            PromotionBanner()

            // 排序和筛选
            SortAndFilter(
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

// 排序类型枚举
enum class SortType {
    COMPREHENSIVE,      // 综合排序
    PRICE_LOW_TO_HIGH, // 人均价低到高
    DISTANCE,          // 距离优先
    RATING,            // 商家好评优先
    MIN_DELIVERY       // 起送低到高
}

@Composable
fun TopBar(onBackClicked: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "美食外卖",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                // 红点提示
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Red, RoundedCornerShape(4.dp))
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SearchBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "coco都可",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CategoryIcons(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        CategoryItem("精选", "⭐"),
        CategoryItem("汉堡薯条", "🍔"),
        CategoryItem("地方菜系", "🍲"),
        CategoryItem("快餐便当", "🍱"),
        CategoryItem("奶茶咖啡", "☕"),
        CategoryItem("全部", "≡")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(categories.size) { index ->
                CategoryIcon(
                    category = categories[index],
                    selected = selectedCategory == categories[index].name,
                    onClick = { onCategorySelected(categories[index].name) }
                )
            }
        }
    }
}

data class CategoryItem(val name: String, val emoji: String)

@Composable
fun CategoryIcon(category: CategoryItem, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = category.emoji,
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            color = if (selected) Color(0xFF00BFFF) else Color.Black,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun PromotionBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFF3366)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "闪爆日 每日特价 2.9元起",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SortAndFilter(
    selectedSortType: SortType,
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column {
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
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "速度",
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
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("换一换", "天天爆红包", "减配送费", "无门槛红包")) { tag ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 12.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

// 排序弹窗
@Composable
fun SortDialog(
    selectedSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 320.dp)
                .clickable(enabled = false) { },
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                SortOption(
                    text = "综合排序",
                    isSelected = selectedSortType == SortType.COMPREHENSIVE,
                    onClick = { onSortTypeSelected(SortType.COMPREHENSIVE) }
                )
                SortOption(
                    text = "人均价低到高",
                    isSelected = selectedSortType == SortType.PRICE_LOW_TO_HIGH,
                    onClick = { onSortTypeSelected(SortType.PRICE_LOW_TO_HIGH) }
                )
                SortOption(
                    text = "距离优先",
                    isSelected = selectedSortType == SortType.DISTANCE,
                    onClick = { onSortTypeSelected(SortType.DISTANCE) }
                )
                SortOption(
                    text = "商家好评优先",
                    isSelected = selectedSortType == SortType.RATING,
                    onClick = { onSortTypeSelected(SortType.RATING) }
                )
                SortOption(
                    text = "起送低到高",
                    isSelected = selectedSortType == SortType.MIN_DELIVERY,
                    onClick = { onSortTypeSelected(SortType.MIN_DELIVERY) }
                )
            }
        }
    }
}

@Composable
fun SortOption(
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
@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var selectedPromotions by remember { mutableStateOf(setOf<String>()) }
    var selectedFeatures by remember { mutableStateOf(setOf<String>()) }
    var selectedPriceRange by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopEnd)
                .padding(top = 180.dp)
                .clickable(enabled = false) { },
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
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
                TakeoutPriceRangeSlider()
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
fun TakeoutRestaurantCard(restaurant: Restaurant, navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                navController.navigate("${com.example.myele.navigation.Screen.StorePage.route}/${restaurant.restaurantId}")
            },
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // 左侧图片
            Box {
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 100.dp,
                    cornerRadius = 8.dp
                )
                // 优享大牌标签
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF8B4513)
                ) {
                    Text(
                        text = "优享大牌",
                        color = Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 右侧信息
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = restaurant.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB800),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${restaurant.rating}分",
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B00),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "月售${restaurant.salesVolume}+",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${restaurant.deliveryTime}分钟",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${restaurant.distance}km",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 优惠活动
                if (restaurant.coupons.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = Color(0xFFFF6B00),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "满减优惠",
                            fontSize = 11.sp,
                            color = Color(0xFFFF6B00),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakeoutPriceRangeSlider() {
    var sliderPosition by remember { mutableStateOf(0f..120f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "¥${sliderPosition.start.toInt()}",
                fontSize = 14.sp,
                color = Color(0xFF00BFFF),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (sliderPosition.endInclusive >= 120f) "¥120+" else "¥${sliderPosition.endInclusive.toInt()}",
                fontSize = 14.sp,
                color = Color(0xFF00BFFF),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..120f,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00BFFF),
                activeTrackColor = Color(0xFF00BFFF),
                inactiveTrackColor = Color(0xFFE0E0E0)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

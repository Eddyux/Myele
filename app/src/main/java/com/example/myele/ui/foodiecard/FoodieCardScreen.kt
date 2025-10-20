package com.example.myele.ui.foodiecard

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
import com.example.myele.model.Coupon
import com.example.myele.model.Restaurant
import com.example.myele.model.SuperFoodieCard
import com.example.myele.ui.components.RestaurantImage

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
                )
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
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "购买超级吃货卡",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "使用范围 ⓘ",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 超级吃货卡促销卡片
                        foodieCards.forEach { card ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "爆红包商家专享",
                                            fontSize = 14.sp,
                                            color = Color(0xFF4CAF50),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(
                                                text = "5",
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFF6B00)
                                            )
                                            Text(
                                                text = "元",
                                                fontSize = 14.sp,
                                                color = Color(0xFFFF6B00)
                                            )
                                            Text(
                                                text = "×",
                                                fontSize = 18.sp,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                            Text(
                                                text = "6",
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFF6B00)
                                            )
                                            Text(
                                                text = "张",
                                                fontSize = 14.sp,
                                                color = Color(0xFFFF6B00)
                                            )
                                        }
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "含1盒Zeal罐头+8元宠物红包",
                                            fontSize = 12.sp,
                                            color = Color(0xFF4CAF50)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(
                                                text = "¥${card.price}",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFF3366)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "¥39",
                                                fontSize = 12.sp,
                                                color = Color.Gray,
                                                style = LocalTextStyle.current.copy(
                                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFFF6B00)
                                            ),
                                            shape = RoundedCornerShape(20.dp),
                                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp)
                                        ) {
                                            Text("抢购", fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 爆红包套餐列表
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(explosivePackages) { pkg ->
                        ExplosivePackageCard(pkg)
                    }
                }
            }

            // 火车票神券广告
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF5E35B1)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "火车票神券",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "开卡得APP最高新客券",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Train,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            // 商家分类标签
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    TabButton(
                        text = "爆红包商家",
                        selected = selectedTab == "爆红包商家",
                        onClick = {
                            selectedTab = "爆红包商家"
                            presenter.filterRestaurants("explosive")
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    TabButton(
                        text = "全平台商家",
                        selected = selectedTab == "全平台商家",
                        onClick = {
                            selectedTab = "全平台商家"
                            presenter.filterRestaurants("all")
                        }
                    )
                }
            }

            // 排序和筛选选项
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        SortButton("综合排序", selectedSort == "综合排序") {
                            selectedSort = "综合排序"
                            presenter.sortRestaurants("综合")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        SortButton("人气优先", selectedSort == "人气优先") {
                            selectedSort = "人气优先"
                            presenter.sortRestaurants("人气")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "商家品类",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clickable { }
                                .padding(8.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 筛选标签
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("减配送费", "品牌商家", "蓝骑士专送")) { tag ->
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = { Text(tag, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // 餐厅列表
            items(restaurants.size) { index ->
                RestaurantCard(restaurants[index])
            }
        }
    }
}

@Composable
fun ExplosivePackageCard(pkg: Coupon) {
    Surface(
        modifier = Modifier
            .width(200.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF3E0)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "爆红包商家专享",
                        fontSize = 12.sp,
                        color = Color(0xFFFF3366)
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "5",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B00)
                        )
                        Text(text = "元×", fontSize = 12.sp)
                        Text(
                            text = "6",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B00)
                        )
                        Text(text = "张", fontSize = 12.sp)
                    }
                    Text(
                        text = "无门槛",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFF3366).copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "爆红包",
                        fontSize = 10.sp,
                        color = Color(0xFFFF3366),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "可免费爆涨",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B00)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("抢购", fontSize = 12.sp)
                }
            }

            Text(
                text = "赠 22元×2张 满58可用",
                fontSize = 10.sp,
                color = Color(0xFFFF3366),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        color = if (selected) Color.Black else Color.Gray,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(bottom = 4.dp)
    )
}

@Composable
fun SortButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = if (selected) Color(0xFFFF3366) else Color.Black,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // 餐厅图片
            RestaurantImage(
                restaurantName = restaurant.name,
                size = 80.dp,
                cornerRadius = 8.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = restaurant.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "约${restaurant.deliveryTime} | ${restaurant.distance}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (restaurant.deliveryFee == 0.0) {
                    Text(
                        text = "蓝骑士准时达",
                        fontSize = 11.sp,
                        color = Color(0xFF00BFFF)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 优惠标签
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(listOf("赚 6元", "29减3", "45减5", "65减8", "85减12")) { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFF3E0)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = Color(0xFFFF6B00),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                if (restaurant.deliveryFee == 0.0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = Color(0xFFFF6B00),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "7.5元无门槛",
                            fontSize = 12.sp,
                            color = Color(0xFFFF6B00),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

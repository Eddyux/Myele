package com.example.myele.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.model.Restaurant
import com.example.myele.ui.components.RestaurantImage
import com.example.myele.ui.components.HotDealImage

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { DataRepository(context) }

    var restaurants by remember { mutableStateOf<List<Restaurant>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("常点", "推荐", "常用")

    LaunchedEffect(Unit) {
        isLoading = true
        restaurants = repository.loadRestaurants()
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标签栏和地址
        TopTabBar(
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        // 搜索栏
        SearchBar(onSearchClick = { navController.navigate(com.example.myele.navigation.Screen.Search.route) })

        // 内容区域
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // 广告横幅
            item {
                PromotionBanner()
            }

            // 服务入口图标
            item {
                ServiceIcons(navController)
            }

            // 爆品团推荐
            item {
                ProductRecommendation()
            }

            // 功能按钮行
            item {
                FunctionButtons()
            }

            // 商家列表
            item {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    RestaurantList(restaurants = restaurants, navController = navController)
                }
            }
        }
    }
}

@Composable
fun TopTabBar(tabs: List<String>, selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 标签
                Row {
                    tabs.forEachIndexed { index, tab ->
                        Text(
                            text = tab,
                            fontSize = 16.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) Color.Black else Color.Gray,
                            modifier = Modifier
                                .clickable { onTabSelected(index) }
                                .padding(horizontal = 8.dp)
                        )
                    }
                }

                // 地址显示
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* 切换地址 */ }
                ) {
                    Text(
                        text = "华中师范大学元宝山...",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(onSearchClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onSearchClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White
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
                tint = Color(0xFF00BFFF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "coco都可",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "7.5元红包",
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp,
                modifier = Modifier
                    .background(Color(0xFFFFF0F0), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSearchClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text("搜索", fontSize = 12.sp)
            }
        }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "爆火好店 必得25元",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("去领取", color = Color(0xFFFF3366), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ServiceIcons(navController: NavController) {
    val services = listOf(
        ServiceItem("美食外卖", Icons.Default.Restaurant, Color(0xFFFF6B6B), com.example.myele.navigation.Screen.Takeout.route),
        ServiceItem("超市便利", Icons.Default.ShoppingBag, Color(0xFF4CAF50)),
        ServiceItem("学生福利", Icons.Default.School, Color(0xFF2196F3)),
        ServiceItem("水果买菜", Icons.Default.Eco, Color(0xFF8BC34A)),
        ServiceItem("看病买药", Icons.Default.LocalHospital, Color(0xFFFF9800)),
        ServiceItem("甜品饮品", Icons.Default.Icecream, Color(0xFFE91E63)),
        ServiceItem("天天爆红包", Icons.Default.CardGiftcard, Color(0xFFF44336)),
        ServiceItem("爆品团", Icons.Default.Group, Color(0xFF9C27B0)),
        ServiceItem("赚吃货豆", Icons.Default.Star, Color(0xFFFF5722)),
        ServiceItem("0元领水果", Icons.Default.LocalOffer, Color(0xFF00BCD4))
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)  // 添加明确的高度约束 (2行 * 80dp/行)
            .background(Color.White)
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(services) { service ->
            ServiceIconItem(service, navController)
        }
    }
}

data class ServiceItem(val name: String, val icon: ImageVector, val color: Color, val route: String? = null)

@Composable
fun ServiceIconItem(service: ServiceItem, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                service.route?.let { navController.navigate(it) }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(service.color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = service.icon,
                contentDescription = service.name,
                tint = service.color,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = service.name,
            fontSize = 11.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProductRecommendation() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "爆品团 免凑单 免运费",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "更多 >",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) { index ->
                ProductCard(index)
            }
        }
    }
}

@Composable
fun ProductCard(imageIndex: Int = 0) {
    Surface(
        modifier = Modifier
            .width(100.dp)
            .clickable { /* TODO */ },
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column {
            HotDealImage(
                index = imageIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                size = 100.dp,
                cornerRadius = 8.dp
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "爆品团商品",
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "¥5.9",
                    fontSize = 14.sp,
                    color = Color(0xFFFF3366),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FunctionButtons() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FunctionButton("换一换", Icons.Default.Refresh)
            FunctionButton("天天爆红包", Icons.Default.CardGiftcard)
            FunctionButton("减配送费", Icons.Default.LocalShipping)
            FunctionButton("学生特价", Icons.Default.School)
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun FunctionButton(text: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { /* TODO */ }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color(0xFF00BFFF),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun RestaurantList(restaurants: List<Restaurant>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        restaurants.forEach { restaurant ->
            RestaurantCard(restaurant, navController)
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant, navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable {
                navController.navigate(
                    com.example.myele.navigation.Screen.StorePage.createRoute(restaurant.restaurantId)
                )
            },
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${restaurant.rating}",
                            fontSize = 12.sp,
                            color = Color(0xFFFFB800)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "月售${restaurant.salesVolume}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "起送¥${restaurant.minDeliveryAmount}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "配送¥${restaurant.deliveryFee}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            if (restaurant.coupons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF8E1), RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFFFF6B00),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "优惠活动",
                        fontSize = 11.sp,
                        color = Color(0xFFFF6B00)
                    )
                }
            }
        }
    }
}

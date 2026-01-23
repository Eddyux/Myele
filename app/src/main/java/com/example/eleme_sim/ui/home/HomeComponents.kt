package com.example.eleme_sim.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.model.Restaurant
import com.example.eleme_sim.ui.components.RestaurantImage
import com.example.eleme_sim.ui.components.HotDealImage

@Composable
fun TopTabBar(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean = false,
    onAddressClick: () -> Unit = {}
) {
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

                // 地址显示和刷新按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 刷新按钮
                    IconButton(
                        onClick = onRefresh,
                        enabled = !isRefreshing,
                        modifier = Modifier.size(32.dp)
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Color.Gray
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "刷新",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 地址显示
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onAddressClick() }
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
        ServiceItem("美食外卖", Icons.Default.Restaurant, Color(0xFFFF6B6B), com.example.eleme_sim.navigation.Screen.Takeout.route),
        ServiceItem("超市便利", Icons.Default.ShoppingBag, Color(0xFF4CAF50), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("超市便利")),
        ServiceItem("学生福利", Icons.Default.School, Color(0xFF2196F3), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("学生福利")),
        ServiceItem("水果买菜", Icons.Default.Eco, Color(0xFF8BC34A), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("水果买菜")),
        ServiceItem("看病买药", Icons.Default.LocalHospital, Color(0xFFFF9800), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("看病买药")),
        ServiceItem("甜品饮品", Icons.Default.Icecream, Color(0xFFE91E63), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("甜品饮品")),
        ServiceItem("天天爆红包", Icons.Default.CardGiftcard, Color(0xFFF44336), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("天天爆红包")),
        ServiceItem("爆品团", Icons.Default.Group, Color(0xFF9C27B0), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("爆品团")),
        ServiceItem("赚吃货豆", Icons.Default.Star, Color(0xFFFF5722), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("赚吃货豆")),
        ServiceItem("0元领水果", Icons.Default.LocalOffer, Color(0xFF00BCD4), com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("0元领水果"))
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)  // 足够的高度确保两排图标完全显示，无需滚动
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
fun ProductRecommendation(navController: NavController) {
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
                color = Color.Gray,
                modifier = Modifier.clickable {
                    navController.navigate(
                        com.example.eleme_sim.navigation.Screen.ServicePage.createRoute("爆品团")
                    )
                }
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
fun FunctionButtons(
    selectedButton: String? = null,
    onFilterClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    onRedPacketClick: () -> Unit = {},
    onFreeDeliveryClick: () -> Unit = {},
    onStudentClick: () -> Unit = {}
) {
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
            FunctionButton("换一换", Icons.Default.Refresh, isSelected = selectedButton == "换一换", onClick = onRefreshClick)
            FunctionButton("天天爆红包", Icons.Default.CardGiftcard, isSelected = selectedButton == "天天爆红包", onClick = onRedPacketClick)
            FunctionButton("减配送费", Icons.Default.LocalShipping, isSelected = selectedButton == "减配送费", onClick = onFreeDeliveryClick)
            FunctionButton("学生特价", Icons.Default.School, isSelected = selectedButton == "学生特价", onClick = onStudentClick)
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "筛选",
                tint = Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onFilterClick() }
            )
        }
    }
}

@Composable
fun FunctionButton(text: String, icon: ImageVector, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFF00BFFF) else Color.Transparent,
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (isSelected) Color.White else Color(0xFF00BFFF),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color.Gray
            )
        }
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
                    com.example.eleme_sim.navigation.Screen.StorePage.createRoute(restaurant.restaurantId)
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

@Composable
fun HomeFilterChip(
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
fun HomeFlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePriceRangeSlider(
    sliderPosition: ClosedFloatingPointRange<Float> = 0f..120f,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit = {}
) {
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
            onValueChange = onValueChange,
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

@Composable
fun HomeSortOptions(
    selectedSortType: HomeSortType,
    onSortClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onSortClicked() }
            ) {
                Text(
                    text = when (selectedSortType) {
                        HomeSortType.COMPREHENSIVE -> "综合排序"
                        HomeSortType.PRICE_LOW_TO_HIGH -> "人均价低到高"
                        HomeSortType.DISTANCE -> "距离优先"
                        HomeSortType.RATING -> "商家好评优先"
                        HomeSortType.SALES -> "销量优先"
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
        }
    }
}

@Composable
fun HomeSortDialogOption(
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

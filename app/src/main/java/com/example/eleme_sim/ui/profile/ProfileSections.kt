package com.example.eleme_sim.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.navigation.Screen
import com.example.eleme_sim.ui.components.RestaurantImage
import com.example.eleme_sim.ui.components.UserAvatar

@Composable
fun UserInfoHeader(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate(Screen.MyInfo.route) }
            ) {
                // 头像
                UserAvatar(
                    size = 60.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "于骁",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "189****1018",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // 设置按钮
            IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "设置",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ClubBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { /* TODO */ },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF4A148C)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "悦享俱乐部",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "去看看 >",
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun UserQuickActions(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionItem("超级吃货卡", Icons.Default.CardGiftcard, Color(0xFFFF6B00)) {
                navController.navigate(Screen.FoodieCard.route)
            }
            QuickActionItem("吃货豆", Icons.Default.Star, Color(0xFFFFD700)) {
                navController.navigate(Screen.Undeveloped.createRoute("吃货豆"))
            }
            QuickActionItem("红包卡券", Icons.Default.LocalOffer, Color(0xFFFF3366)) {
                navController.navigate(Screen.Coupons.route)
            }
        }
    }
}

@Composable
fun MyOrders(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                    text = "我的订单",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.MyOrders.route)
                    }
                ) {
                    Text(
                        text = "全部",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OrderTypeItem("全部", Icons.Default.List) {
                    navController.navigate(Screen.MyOrders.route)
                }
                OrderTypeItem("待收货/使用", Icons.Default.Inventory) {
                    navController.navigate(Screen.MyOrders.route)
                }
                OrderTypeItem("待评价", Icons.Default.RateReview) {
                    navController.navigate(Screen.Reviews.route)
                }
                OrderTypeItem("退款", Icons.Default.MoneyOff) {
                    navController.navigate(Screen.MyOrders.route)
                }
            }
        }
    }
}

@Composable
fun MyWallet(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "我的钱包",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WalletItem("借钱", Icons.Default.Paid) {
                    navController.navigate(Screen.Undeveloped.createRoute("借钱"))
                }
                WalletItem("零钱", Icons.Default.AccountBalanceWallet) {
                    navController.navigate(Screen.Undeveloped.createRoute("零钱"))
                }
                WalletItem("外卖红包", Icons.Default.CardGiftcard) {
                    navController.navigate(Screen.Undeveloped.createRoute("外卖红包"))
                }
                WalletItem("笔笔返现", Icons.Default.MonetizationOn) {
                    navController.navigate(Screen.Undeveloped.createRoute("笔笔返现"))
                }
            }
        }
    }
}

@Composable
fun MyFavorites(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FavoriteItem(
                    title = "我的关注",
                    subtitle = "12家店铺",
                    onClick = { navController.navigate(Screen.MyFollows.route) }
                )
                FavoriteItem(
                    title = "常点的店",
                    subtitle = "8家店铺",
                    onClick = { navController.navigate(Screen.FrequentStores.route) }
                )
            }
        }
    }
}

@Composable
fun MoreFunctions(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "更多功能",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val functions = listOf(
                ProfileMenuItem(Icons.Default.LocationOn, "我的地址", Screen.MyAddresses.route),
                ProfileMenuItem(Icons.Default.Support, "我的客服", Screen.CustomerService.route),
                ProfileMenuItem(Icons.Default.Assessment, "我的账单", Screen.MyBills.route),
                ProfileMenuItem(Icons.Default.AttachMoney, "下单返", Screen.OrderRewards.route)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(120.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(functions) { function ->
                    FunctionItem(function, navController)
                }
            }
        }
    }
}

@Composable
fun RecommendedFood(navController: NavController) {
    val restaurants = listOf(
        RestaurantInfo(
            restaurantId = "rest_023",
            name = "金长风荷叶烤鸡",
            description = "招牌荷叶烤鸡，香嫩入味",
            deliveryTime = "35分钟",
            distance = "2.1km",
            startPrice = "¥28起",
            imageName = "chicken_restaurant"
        ),
        RestaurantInfo(
            restaurantId = "rest_001",
            name = "川香麻辣烫",
            description = "正宗川味，麻辣鲜香",
            deliveryTime = "25分钟",
            distance = "1.2km",
            startPrice = "¥15起",
            imageName = "malatang_restaurant"
        ),
        RestaurantInfo(
            restaurantId = "rest_006",
            name = "瑞幸咖啡",
            description = "精品咖啡，提神醒脑",
            deliveryTime = "20分钟",
            distance = "0.8km",
            startPrice = "¥9.9起",
            imageName = "coffee_restaurant"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "你可能还喜欢",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        restaurants.forEach { restaurant ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        navController.navigate(Screen.StorePage.createRoute(restaurant.restaurantId))
                    },
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
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
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = restaurant.description,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${restaurant.deliveryTime} | ${restaurant.distance}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = restaurant.startPrice,
                            fontSize = 14.sp,
                            color = Color(0xFFFF3366),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

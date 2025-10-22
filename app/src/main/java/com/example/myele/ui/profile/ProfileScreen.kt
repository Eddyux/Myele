package com.example.myele.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.navigation.Screen
import com.example.myele.ui.components.RestaurantImage
import com.example.myele.ui.components.UserAvatar

data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val route: String? = null
)

@Composable
fun ProfileScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部个人信息
        item {
            UserInfoHeader(navController)
        }

        // 悦享俱乐部
        item {
            ClubBanner()
        }

        // 用户快捷功能卡片
        item {
            UserQuickActions(navController)
        }

        // 我的订单
        item {
            MyOrders(navController)
        }

        // 我的钱包
        item {
            MyWallet()
        }

        // 我的关注和常点的店
        item {
            MyFavorites(navController)
        }

        // 更多功能
        item {
            MoreFunctions(navController)
        }

        // 你可能还喜欢
        item {
            RecommendedFood()
        }
    }
}

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
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                // TODO: Navigate to foodie beans page
            }
            QuickActionItem("红包卡券", Icons.Default.LocalOffer, Color(0xFFFF3366)) {
                navController.navigate(Screen.Coupons.route)
            }
        }
    }
}

@Composable
fun QuickActionItem(title: String, icon: ImageVector, color: Color, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Black
        )
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
fun OrderTypeItem(title: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF00BFFF),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MyWallet() {
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
                WalletItem("借钱", Icons.Default.Paid)
                WalletItem("零钱", Icons.Default.AccountBalanceWallet)
                WalletItem("外卖红包", Icons.Default.CardGiftcard)
                WalletItem("笔笔返现", Icons.Default.MonetizationOn)
            }
        }
    }
}

@Composable
fun WalletItem(title: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { /* TODO */ }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFFFF6B00),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Black
        )
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
fun RowScope.FavoriteItem(title: String, subtitle: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = Color.Gray
        )
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
                ProfileMenuItem(Icons.Default.AttachMoney, "下单返", null)
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
fun FunctionItem(item: ProfileMenuItem, navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                item.route?.let { navController.navigate(it) }
            }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = Color(0xFF00BFFF),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.title,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun RecommendedFood() {
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

        repeat(3) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { /* TODO */ },
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    RestaurantImage(
                        restaurantName = "美味餐厅",
                        size = 80.dp,
                        cornerRadius = 8.dp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "美味餐厅",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "30分钟 | 1.5km",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "¥25起",
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

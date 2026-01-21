package com.example.myele_sim.ui.myinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele_sim.navigation.Screen
import com.example.myele_sim.ui.components.UserAvatar

data class InfoItem(
    val title: String,
    val value: String? = null,
    val showAvatar: Boolean = false,
    val showArrow: Boolean = true,
    val route: String? = null
)

/**
 * 个人资料页面
 * 从Settings页面点击用户信息区域进入
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInfoScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("个人资料") },
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
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 12.dp)
        ) {
            // 基础信息
            item {
                CategoryHeader("基础信息")
            }

            val basicInfoItems = listOf(
                InfoItem("头像", showAvatar = true),
                InfoItem("账号", "eleme408250523303706"),
                InfoItem("昵称", ""),
                InfoItem("简介", ""),
                InfoItem("注册时间", "2021-06-20", showArrow = false),
                InfoItem("收货地址", route = Screen.MyAddresses.route)
            )

            items(basicInfoItems) { item ->
                InfoItemRow(
                    item = item,
                    onClick = {
                        item.route?.let { navController.navigate(it) }
                    }
                )
                if (item != basicInfoItems.last()) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }
            }

            // 账号绑定
            item {
                Divider(color = Color(0xFFF0F0F0), thickness = 8.dp)
                CategoryHeader("账号绑定")
            }

            val accountBindingItems = listOf(
                InfoItem("手机", "189****1018", route = Screen.ChangePhone.route),
                InfoItem("淘宝", "t**2"),
                InfoItem("支付宝", "189******18"),
                InfoItem("微信", "已绑定")
            )

            items(accountBindingItems) { item ->
                AccountBindingRow(
                    item = item,
                    onClick = {
                        item.route?.let { navController.navigate(it) }
                    }
                )
                if (item != accountBindingItems.last()) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(title: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF5F5F5)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun InfoItemRow(item: InfoItem, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                color = Color.Black
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.showAvatar) {
                    UserAvatar(size = 40.dp)
                } else if (item.value != null) {
                    Text(
                        text = item.value,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                if (item.showArrow) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun AccountBindingRow(item: InfoItem, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 图标占位符
                Box(
                    modifier = Modifier
                        .size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when (item.title) {
                        "手机" -> Icons.Default.Phone
                        "淘宝" -> Icons.Default.ShoppingBag
                        "支付宝" -> Icons.Default.AccountBalance
                        "微信" -> Icons.Default.Chat
                        else -> Icons.Default.AccountCircle
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = item.title,
                        tint = when (item.title) {
                            "手机" -> Color(0xFF000000)
                            "淘宝" -> Color(0xFFFF6600)
                            "支付宝" -> Color(0xFF1677FF)
                            "微信" -> Color(0xFF07C160)
                            else -> Color.Gray
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.value != null) {
                    Text(
                        text = item.value,
                        fontSize = 14.sp,
                        color = Color(0xFF2196F3)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

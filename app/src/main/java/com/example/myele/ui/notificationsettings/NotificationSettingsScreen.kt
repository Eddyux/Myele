package com.example.myele.ui.notificationsettings

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

data class NotificationItem(
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector? = null,
    val showSwitch: Boolean = false,
    val showArrow: Boolean = true
)

/**
 * 消息通知设置页面
 * 从Settings页面点击"消息通知设置"进入
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(navController: NavController) {
    var systemNotificationEnabled by remember { mutableStateOf(true) }
    var inAppBannerEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
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
            // 饿了么未打开时
            item {
                CategoryHeader("饿了么未打开时")
            }

            item {
                NotificationRow(
                    title = "系统消息通知",
                    subtitle = "已开启",
                    showArrow = false
                )
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            // 饿了么打开时
            item {
                CategoryHeader("饿了么打开时")
            }

            item {
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
                        Text(
                            text = "饿了么内横幅通知",
                            fontSize = 16.sp,
                            color = Color.Black
                        )

                        Switch(
                            checked = inAppBannerEnabled,
                            onCheckedChange = { inAppBannerEnabled = it }
                        )
                    }
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            // 聊天动态通知
            item {
                CategoryHeader("聊天动态通知")
            }

            item {
                NotificationRow(
                    title = "聊天消息",
                    subtitle = "与骑手、商家、药师等对话内容",
                    icon = Icons.Default.Chat
                )
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            item {
                NotificationRow(
                    title = "消息号",
                    subtitle = "平台官方消息号",
                    icon = Icons.Default.Notifications
                )
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            // 短信通知
            item {
                CategoryHeader("短信通知")
            }

            item {
                NotificationRow(
                    title = "短信通知",
                    subtitle = "优惠福利、平台活动等短信通知"
                )
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            // 平台消息通知
            item {
                CategoryHeader("平台消息通知")
            }

            val platformItems = listOf(
                NotificationItem("订单交易", "订单动态、履约信息等", Icons.Default.ShoppingCart),
                NotificationItem("权益通知", "超会权益、红包卡券等", Icons.Default.CreditCard),
                NotificationItem("服务通知", "账户安全、订阅消息等", Icons.Default.Security),
                NotificationItem("活动优惠", "福利促销、互动玩法等", Icons.Default.LocalOffer)
            )

            items(platformItems) { item ->
                NotificationRow(
                    title = item.title,
                    subtitle = item.subtitle,
                    icon = item.icon
                )
                if (item != platformItems.last()) {
                    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                }
            }

            // 消息管理
            item {
                Divider(color = Color(0xFFF0F0F0), thickness = 8.dp)
                CategoryHeader("消息管理")
            }

            item {
                NotificationRow(
                    title = "订阅消息管理",
                    subtitle = null
                )
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            item {
                NotificationRow(
                    title = "清空消息列表",
                    subtitle = null
                )
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
fun NotificationRow(
    title: String,
    subtitle: String?,
    icon: ImageVector? = null,
    showArrow: Boolean = true
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ },
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

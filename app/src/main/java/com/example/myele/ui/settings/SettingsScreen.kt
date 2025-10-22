package com.example.myele.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.navigation.Screen
import com.example.myele.ui.components.UserAvatar

data class SettingItem(
    val title: String,
    val subtitle: String? = null,
    val route: String? = null
)

/**
 * 设置页面
 * 从Profile页面点击设置按钮进入
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
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
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 用户信息区域
            item {
                UserInfoSection(navController)
            }

            // 设置选项列表
            val settingItems = listOf(
                SettingItem("收货地址管理", route = Screen.MyAddresses.route),
                SettingItem("账号与安全"),
                SettingItem("支付设置", "先享后付、免密支付等", Screen.PaymentSettings.route),
                SettingItem("安全中心", "安全问题解决与咨询"),
                SettingItem("消息通知设置", "订单、优惠通知等", Screen.NotificationSettings.route),
                SettingItem("通用设置", "清理缓存、图片质量"),
                SettingItem("桌面小组件", "管理你的桌面小组件"),
                SettingItem("关于饿了么"),
                SettingItem("问题咨询及反馈"),
                SettingItem("个人信息收集清单"),
                SettingItem("第三方信息数据共享")
            )

            items(settingItems) { item ->
                SettingItemRow(
                    item = item,
                    onClick = {
                        item.route?.let { navController.navigate(it) }
                    }
                )
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }
        }
    }
}

@Composable
fun UserInfoSection(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.MyInfo.route) },
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
                UserAvatar(size = 60.dp)

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "饿小宝",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "还没有个性签名哦",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun SettingItemRow(item: SettingItem, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (item.subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

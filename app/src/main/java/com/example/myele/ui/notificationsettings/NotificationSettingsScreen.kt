package com.example.myele.ui.notificationsettings

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import com.example.myele.data.DataRepository
import androidx.activity.compose.BackHandler

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
    val context = LocalContext.current
    val repository = DataRepository(context)
    val prefsManager = repository.getPreferencesManager()

    var systemNotificationEnabled by remember { mutableStateOf(prefsManager.getSystemNotificationEnabled()) }
    var inAppBannerEnabled by remember { mutableStateOf(prefsManager.getInAppBannerEnabled()) }
    var showCloseNotificationDialog by remember { mutableStateOf(false) }
    var showNotificationDetailScreen by remember { mutableStateOf(false) }
    var showBannerDialog by remember { mutableStateOf(false) }
    var bannerDialogMessage by remember { mutableStateOf("") }
    var showGenericScreen by remember { mutableStateOf(false) }
    var genericScreenTitle by remember { mutableStateOf("") }

    // 处理系统返回键
    BackHandler(enabled = showNotificationDetailScreen || showGenericScreen) {
        when {
            showNotificationDetailScreen -> showNotificationDetailScreen = false
            showGenericScreen -> showGenericScreen = false
        }
    }

    // 如果显示内部页面，则完全替换主页面
    if (showNotificationDetailScreen) {
        NotificationDetailScreen(
            systemNotificationEnabled = systemNotificationEnabled,
            onBack = { showNotificationDetailScreen = false },
            onToggle = { enabled ->
                systemNotificationEnabled = enabled
                prefsManager.setSystemNotificationEnabled(enabled)
            }
        )
        return
    }

    if (showGenericScreen) {
        GenericNotificationScreen(
            title = genericScreenTitle,
            onBack = { showGenericScreen = false }
        )
        return
    }

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
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (systemNotificationEnabled) {
                                // 已开启 -> 先弹窗确认
                                showCloseNotificationDialog = true
                            } else {
                                // 已关闭 -> 直接进入详情页
                                showNotificationDetailScreen = true
                            }
                        },
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
                            text = "系统消息通知",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = if (systemNotificationEnabled) "已开启" else "已关闭",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
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
                            onCheckedChange = { enabled ->
                                inAppBannerEnabled = enabled
                                prefsManager.setInAppBannerEnabled(enabled)
                                bannerDialogMessage = if (enabled) "已打开" else "已关闭"
                                showBannerDialog = true
                            }
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
                    icon = Icons.Default.Chat,
                    onClick = {
                        genericScreenTitle = "聊天消息"
                        showGenericScreen = true
                    }
                )
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            item {
                NotificationRow(
                    title = "消息号",
                    subtitle = "平台官方消息号",
                    icon = Icons.Default.Notifications,
                    onClick = {
                        genericScreenTitle = "消息号"
                        showGenericScreen = true
                    }
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
                    subtitle = "优惠福利、平台活动等短信通知",
                    onClick = {
                        genericScreenTitle = "短信通知"
                        showGenericScreen = true
                    }
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
                    icon = item.icon,
                    onClick = {
                        genericScreenTitle = item.title
                        showGenericScreen = true
                    }
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
                    subtitle = null,
                    onClick = {
                        genericScreenTitle = "订阅消息管理"
                        showGenericScreen = true
                    }
                )
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            item {
                NotificationRow(
                    title = "清空消息列表",
                    subtitle = null,
                    onClick = {
                        genericScreenTitle = "清空消息列表"
                        showGenericScreen = true
                    }
                )
            }
        }

        // 关闭通知确认弹窗
        if (showCloseNotificationDialog) {
            CloseNotificationDialog(
                onDismiss = { showCloseNotificationDialog = false },
                onClose = {
                    // 点击"关闭"后进入详情页
                    showCloseNotificationDialog = false
                    showNotificationDetailScreen = true
                },
                onKeep = {
                    // 点击"保留重要通知"不进入
                    showCloseNotificationDialog = false
                }
            )
        }

        // 横幅通知弹窗
        if (showBannerDialog) {
            SimpleMessageDialog(
                message = bannerDialogMessage,
                onDismiss = { showBannerDialog = false }
            )
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
    showArrow: Boolean = true,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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

@Composable
fun CloseNotificationDialog(
    onDismiss: () -> Unit,
    onClose: () -> Unit,
    onKeep: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "是否关闭通知",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = "关闭后将不再接收系统推送通知",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onClose,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("关闭", color = Color.Gray, fontSize = 16.sp)
                    }
                    Button(
                        onClick = onKeep,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("保留重要通知", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        },
        confirmButton = {},
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailScreen(
    systemNotificationEnabled: Boolean,
    onBack: () -> Unit,
    onToggle: (Boolean) -> Unit
) {
    var enabled by remember { mutableStateOf(systemNotificationEnabled) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("系统消息通知") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
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
                        text = "允许通知",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Switch(
                        checked = enabled,
                        onCheckedChange = { newEnabled ->
                            enabled = newEnabled
                            onToggle(newEnabled)
                            dialogMessage = if (newEnabled) "已打开" else "关闭成功"
                            showDialog = true
                        }
                    )
                }
            }
        }

        if (showDialog) {
            SimpleMessageDialog(
                message = dialogMessage,
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun SimpleMessageDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = message,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("确定", color = Color.White, fontSize = 16.sp)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericNotificationScreen(
    title: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "该功能未开发",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }
    }
}

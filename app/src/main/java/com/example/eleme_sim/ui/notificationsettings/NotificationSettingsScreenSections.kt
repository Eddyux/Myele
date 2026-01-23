package com.example.eleme_sim.ui.notificationsettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SystemNotificationSection(
    systemNotificationEnabled: Boolean,
    onClick: () -> Unit
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

@Composable
fun InAppBannerSection(
    inAppBannerEnabled: Boolean,
    onToggle: (Boolean) -> Unit
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
                text = "饿了么内横幅通知",
                fontSize = 16.sp,
                color = Color.Black
            )

            Switch(
                checked = inAppBannerEnabled,
                onCheckedChange = onToggle
            )
        }
    }
    Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailScreen(
    systemNotificationEnabled: Boolean,
    onBack: () -> Unit,
    onToggle: (Boolean) -> Unit
) {
    val context = LocalContext.current
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

                            // 记录设置操作（用于任务13检测）
                            com.example.eleme_sim.utils.ActionLogger.logSettings(
                                context = context,
                                settingType = "系统消息通知",
                                enabled = newEnabled,
                                showDialog = true
                            )
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

package com.example.eleme_sim.ui.merchantchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

data class MerchantChatMessage(
    val id: String,
    val content: String,
    val time: String,
    val isFromMerchant: Boolean
)

/**
 * 商家聊天详情页面
 * 从订单详情页面点击联系商家进入
 * 显示与商家的聊天详情
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantChatScreen(
    navController: NavController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(emptyList<MerchantChatMessage>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("商家") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Phone, contentDescription = "电话")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column {
                    // 快捷功能按钮
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        QuickActionChip("索要发票")
                        QuickActionChip("修改手机号")
                        QuickActionChip("食品异物")
                    }

                    Divider()

                    // 输入框区域
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Mic, contentDescription = "语音")
                        }

                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = { Text("请输入内容...", fontSize = 14.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.LightGray,
                                focusedBorderColor = Color(0xFF00BFFF)
                            )
                        )

                        IconButton(onClick = { }) {
                            Icon(Icons.Default.EmojiEmotions, contentDescription = "表情")
                        }

                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Menu, contentDescription = "菜单")
                        }

                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    val message = messageText
                                    messages = messages + MerchantChatMessage(
                                        id = (messages.size + 1).toString(),
                                        content = message,
                                        time = "刚刚",
                                        isFromMerchant = false
                                    )

                                    // 记录发送消息给商家（用于任务18检测）
                                    com.example.eleme_sim.utils.ActionLogger.logAction(
                                        context = context,
                                        action = "send_message",
                                        page = "chat",
                                        pageInfo = mapOf("chat_type" to "merchant_chat"),
                                        extraData = mapOf(
                                            "recipient_type" to "merchant",
                                            "message" to message
                                        )
                                    )

                                    messageText = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "发送")
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 顶部商家信息
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 商家头像
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF00BFFF), shape = RoundedCornerShape(30.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "商家",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "在线",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 聊天消息列表
            items(messages) { message ->
                MerchantMessageBubble(message)
            }
        }
    }
}

@Composable
fun QuickActionChip(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF0F0F0)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun MerchantMessageBubble(message: MerchantChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromMerchant) Arrangement.Start else Arrangement.End
    ) {
        if (message.isFromMerchant) {
            // 商家头像
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF00BFFF), shape = RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isFromMerchant) Alignment.Start else Alignment.End
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (message.isFromMerchant) Color.White else Color(0xFF00BFFF)
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color = if (message.isFromMerchant) Color.Black else Color.White
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.time,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        if (!message.isFromMerchant) {
            Spacer(modifier = Modifier.width(8.dp))
            // 用户头像占位
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(18.dp))
            )
        }
    }
}

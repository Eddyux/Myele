package com.example.myele_sim.ui.messagedetail

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
import com.example.myele_sim.ui.components.RiderAvatar

data class ChatMessage(
    val id: String,
    val content: String,
    val time: String,
    val isFromRider: Boolean
)

/**
 * 消息详情页面
 * 从Messages页面点击骑手消息进入
 * 显示与骑手的聊天详情
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageDetailScreen(
    navController: NavController,
    riderName: String = "张三"
) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("1", "您的订单已送达", "15:30", true),
                ChatMessage("2", "好的，谢谢", "15:31", false),
                ChatMessage("3", "祝您用餐愉快", "15:31", true)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("骑手$riderName") },
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
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
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
                                    messages = messages + ChatMessage(
                                        id = (messages.size + 1).toString(),
                                        content = messageText,
                                        time = "刚刚",
                                        isFromRider = false
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
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 顶部骑手信息
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RiderAvatar(size = 60.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "骑手$riderName",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "配送中",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 聊天消息列表
            items(messages) { message ->
                MessageBubble(message)
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
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromRider) Arrangement.Start else Arrangement.End
    ) {
        if (message.isFromRider) {
            RiderAvatar(size = 36.dp)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isFromRider) Alignment.Start else Alignment.End
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (message.isFromRider) Color.White else Color(0xFF00BFFF)
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color = if (message.isFromRider) Color.Black else Color.White
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.time,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        if (!message.isFromRider) {
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

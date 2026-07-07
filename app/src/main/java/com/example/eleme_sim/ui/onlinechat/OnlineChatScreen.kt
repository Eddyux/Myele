package com.example.eleme_sim.ui.onlinechat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.R
import com.example.eleme_sim.ui.components.RiderAvatar
import com.example.eleme_sim.ui.components.UserAvatar

data class ChatMsg(
    val id: String,
    val content: String,
    val time: String,
    val isFromRider: Boolean
)

private val ChatPageBg = Color(0xFFF3F5FA)
private val ChatCardBg = Color(0xFFFFFFFF)
private val ChatTextPrimary = Color(0xFF202531)
private val ChatTextSecondary = Color(0xFF7C879A)
private val ChatAccent = Color(0xFF46B7FF)
private val ChatOutgoing = Color(0xFF5CC5FF)
private val ChatQuickChipBg = Color(0xFFFFFFFF)

@Composable
fun OnlineChatScreen(
    navController: NavController,
    riderName: String = "周丹奎",
    orderStatus: String
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var messageText by remember { mutableStateOf("") }
    val chatScenario = remember(orderStatus) { buildChatScenario(orderStatus) }
    var messages by remember(orderStatus, riderName) {
        mutableStateOf(
            listOf(
                ChatMsg("1", "您好，我是骑手$riderName", "14:30", true),
                ChatMsg("2", chatScenario.followUpMessage, "14:31", true)
            )
        )
    }

    Scaffold(
        containerColor = ChatPageBg,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            ChatTopBar(
                riderName = riderName,
                onBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            ChatBottomBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSend = {
                    if (messageText.isNotBlank()) {
                        val message = messageText
                        messages = messages + ChatMsg(
                            id = (messages.size + 1).toString(),
                            content = message,
                            time = "刚刚",
                            isFromRider = false
                        )

                        com.example.eleme_sim.utils.ActionLogger.logAction(
                            context = context,
                            action = "send_message",
                            page = "chat",
                            pageInfo = mapOf("chat_type" to "rider_chat"),
                            extraData = mapOf(
                                "recipient_type" to "rider",
                                "message" to message,
                                "order_status" to orderStatus
                            )
                        )

                        messageText = ""
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ChatPageBg),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                OrderStatusCard(
                    orderStatus = orderStatus,
                    riderName = riderName
                )
            }

            if (chatScenario.showProofImage) {
                item {
                    RiderImageMessageCard(
                        riderName = riderName,
                        orderStatus = orderStatus
                    )
                }
            }

            items(messages, key = { it.id }) { message ->
                MessageBubble(message)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(
    riderName: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "骑手$riderName",
                    color = ChatTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "当前订单",
                    color = ChatTextSecondary,
                    fontSize = 12.sp
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回", tint = ChatTextPrimary)
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Phone, contentDescription = "电话", tint = ChatTextPrimary)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreHoriz, contentDescription = "更多", tint = ChatTextPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ChatPageBg
        ),
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    )
}

@Composable
private fun OrderStatusCard(
    orderStatus: String,
    riderName: String
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ChatCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF8E6A5)),
                contentAlignment = Alignment.Center
            ) {
                RiderAvatar(size = 52.dp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = orderStatus,
                    color = ChatTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "骑手$riderName 正在为您服务",
                    color = ChatTextSecondary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ChatTextSecondary
            )
        }
    }
}

@Composable
private fun RiderImageMessageCard(
    riderName: String,
    orderStatus: String
) {
    val scenario = remember(orderStatus) { buildChatScenario(orderStatus) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        RiderAvatar(size = 54.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Card(
            modifier = Modifier.width(260.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = ChatCardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = scenario.cardTitle,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChatTextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color(0xFFE9EEF5))
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = scenario.cardMessage,
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        color = ChatTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = scenario.captionText.replace("{riderName}", riderName),
                        fontSize = 12.sp,
                        color = ChatTextSecondary
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.customerservice_songdatu),
                    contentDescription = "订单图片",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(330.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

private data class ChatScenario(
    val cardTitle: String,
    val cardMessage: String,
    val captionText: String,
    val followUpMessage: String,
    val showProofImage: Boolean
)

private fun buildChatScenario(orderStatus: String): ChatScenario {
    return when {
        orderStatus.contains("送达") || orderStatus.contains("完成") -> ChatScenario(
            cardTitle = "订单已送达",
            cardMessage = "您好，货品已按照您的要求送达指定地点，请及时取件。",
            captionText = "骑手{riderName} 已为本次配送拍照留存",
            followUpMessage = "您的订单已送达，请及时取件",
            showProofImage = true
        )
        orderStatus.contains("配送") -> ChatScenario(
            cardTitle = "订单配送中",
            cardMessage = "您好，订单正在配送途中，骑手会尽快送达，请保持电话畅通。",
            captionText = "骑手{riderName} 正在为您配送",
            followUpMessage = "已在配送途中，请您稍等",
            showProofImage = false
        )
        orderStatus.contains("待接单") || orderStatus.contains("接单") || orderStatus.contains("制作") -> ChatScenario(
            cardTitle = orderStatus,
            cardMessage = "您好，订单当前还在处理中，请您耐心等待，状态更新后会第一时间通知您。",
            captionText = "当前订单状态：$orderStatus",
            followUpMessage = "订单当前状态：$orderStatus，请您耐心等待",
            showProofImage = false
        )
        else -> ChatScenario(
            cardTitle = orderStatus,
            cardMessage = "您好，订单当前状态为$orderStatus，如有变化会及时通知您。",
            captionText = "当前订单状态：$orderStatus",
            followUpMessage = "订单当前状态：$orderStatus",
            showProofImage = false
        )
    }
}

@Composable
private fun ChatBottomBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = ChatPageBg,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionChip("索要发票")
                QuickActionChip("修改手机号")
                QuickActionChip("食品异物")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChatRoundIconButton(icon = Icons.Default.Mic, contentDescription = "语音")
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = messageText,
                    onValueChange = onMessageChange,
                    placeholder = {
                        Text(
                            text = "请输入内容...",
                            fontSize = 15.sp,
                            color = ChatTextSecondary
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = ChatAccent
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                ChatRoundIconButton(icon = Icons.Default.EmojiEmotions, contentDescription = "表情")
                Spacer(modifier = Modifier.width(6.dp))
                ChatRoundIconButton(icon = Icons.Default.Menu, contentDescription = "菜单")
                Spacer(modifier = Modifier.width(6.dp))
                Button(
                    onClick = onSend,
                    modifier = Modifier.size(46.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChatAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "发送",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatRoundIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = ChatTextPrimary
        )
    }
}

@Composable
fun QuickActionChip(text: String) {
    Surface(
        modifier = Modifier.clickable { },
        shape = RoundedCornerShape(20.dp),
        color = ChatQuickChipBg,
        shadowElevation = 1.dp
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = ChatTextPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
        )
    }
}

@Composable
fun MessageBubble(message: ChatMsg) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = if (message.isFromRider) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        if (message.isFromRider) {
            RiderAvatar(size = 40.dp)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (message.isFromRider) Alignment.Start else Alignment.End
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 22.dp,
                    topEnd = 22.dp,
                    bottomStart = if (message.isFromRider) 8.dp else 22.dp,
                    bottomEnd = if (message.isFromRider) 22.dp else 8.dp
                ),
                color = if (message.isFromRider) Color.White else ChatOutgoing,
                shadowElevation = 1.dp
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = if (message.isFromRider) ChatTextPrimary else Color.White
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = message.time,
                fontSize = 11.sp,
                color = ChatTextSecondary
            )
        }

        if (!message.isFromRider) {
            Spacer(modifier = Modifier.width(8.dp))
            UserAvatar(size = 40.dp)
        }
    }
}

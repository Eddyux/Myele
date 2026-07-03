package com.example.eleme_sim.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.navigation.Screen
import com.example.eleme_sim.ui.components.RiderAvatar

private val MessagesPageBackground = Color(0xFFF4F7FB)
private val MessagesCardColor = Color(0xFFFFFEFD)
private val MessagesTextPrimary = Color(0xFF171C24)
private val MessagesTextSecondary = Color(0xFF9BA3AF)

data class MessageItem(
    val id: String,
    val title: String,
    val content: String,
    val time: String,
    val isRider: Boolean = false
)

@Composable
fun MessagesScreen(navController: NavController) {
    var messages by remember {
        mutableStateOf(
            listOf(
                MessageItem("1", "平台消息", "先领取25元红包再下单!", "昨天", false),
                MessageItem("2", "骑手·张三", "您的订单已送达", "15:30", true),
                MessageItem("3", "骑手·李四", "正在配送中...", "14:20", true),
                MessageItem("4", "骑手·王五", "已到达取餐点", "13:45", true)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MessagesPageBackground)
    ) {
        MessagesTopBar()

        PlatformSection(
            platformMessages = messages.filter { !it.isRider }
        )

        Text(
            text = "聊天动态",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MessagesTextPrimary,
            modifier = Modifier.padding(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 12.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(28.dp),
            color = MessagesCardColor,
            shadowElevation = 6.dp
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                items(messages.filter { it.isRider }) { message ->
                    MessageCard(
                        message = message,
                        onClick = {
                            val riderName = message.title.removePrefix("骑手·")
                            navController.navigate(Screen.MessageDetail.createRoute(riderName))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MessagesTopBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MessagesPageBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "消息中心",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MessagesTextPrimary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TopActionIcon(
                    icon = Icons.Default.WorkOutline,
                    contentDescription = "工具箱"
                )
                TopActionIcon(
                    icon = Icons.Default.Settings,
                    contentDescription = "设置"
                )
            }
        }
    }
}

@Composable
private fun TopActionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    Surface(
        modifier = Modifier.size(44.dp),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MessagesTextPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun PlatformSection(platformMessages: List<MessageItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "平台消息",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MessagesTextPrimary
            )
            Text(
                text = "查看全部",
                fontSize = 14.sp,
                color = MessagesTextSecondary
            )
        }

        platformMessages.forEach { message ->
            PlatformMessageCard(message = message)
        }
    }
}

@Composable
private fun PlatformMessageCard(
    message: MessageItem
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(24.dp),
        color = MessagesCardColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFFFF3E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color(0xFFFFA726),
                    modifier = Modifier.size(30.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF604B))
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = message.title,
                        modifier = Modifier.weight(1f),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MessagesTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = message.time,
                        fontSize = 13.sp,
                        color = MessagesTextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = MessagesTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun MessageCard(
    message: MessageItem,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box {
                RiderAvatar(size = 56.dp)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF604B))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = message.title,
                        modifier = Modifier.weight(1f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MessagesTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = message.time,
                        fontSize = 13.sp,
                        color = MessagesTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = MessagesTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(start = 66.dp),
            thickness = 1.dp,
            color = Color(0xFFF1F3F6)
        )
    }
}

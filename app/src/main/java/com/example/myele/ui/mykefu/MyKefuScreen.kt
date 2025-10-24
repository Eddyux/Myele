package com.example.myele.ui.mykefu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.model.Order

@Composable
fun MyKefuScreen(navController: NavController) {
    var showOrderSelector by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    var inputText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // 顶部标题栏
            TopBar(onBackClicked = { navController.popBackStack() })

            // 人工服务繁忙提示
            ServiceBusyTip()

            // 智能客服问候区域
            SmartServiceGreeting()

            // 订单选择区域
            if (selectedOrder != null) {
                SelectedOrderSection(
                    order = selectedOrder!!,
                    onOrderChange = { showOrderSelector = true }
                )
            } else {
                OrderSuggestionSection(onOrderSelect = { showOrderSelector = true })
            }

            // 聊天消息区域
            ChatMessagesSection()

            Spacer(modifier = Modifier.weight(1f))

            // 输入框
            InputSection(
                inputText = inputText,
                onInputChange = { inputText = it }
            )
        }

        // 订单选择弹窗
        if (showOrderSelector) {
            OrderSelectorDialog(
                onDismiss = { showOrderSelector = false },
                onOrderSelected = { order ->
                    selectedOrder = order
                    showOrderSelector = false
                }
            )
        }
    }
}

@Composable
fun TopBar(onBackClicked: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.Black
                )
            }
            Text(
                text = "我的客服",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ServiceBusyTip() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF8E1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "人工服务繁忙",
                fontSize = 14.sp,
                color = Color(0xFFFF6B00)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "详情",
                    fontSize = 14.sp,
                    color = Color(0xFF00BFFF),
                    modifier = Modifier.clickable { /* TODO */ }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SmartServiceGreeting() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 客服头像
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(30.dp),
                color = Color(0xFFE3F2FD)
            ) {
                Icon(
                    imageVector = Icons.Default.SupportAgent,
                    contentDescription = "客服",
                    tint = Color(0xFF00BFFF),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "您好，饿了么智能客服为您服务",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OrderSuggestionSection(onOrderSelect: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "您是否要咨询以下订单",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 示例订单
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF9F9F9))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "金长风荷叶烤鸡",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "订单已送达",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "¥10.1",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B00)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "其他订单",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
                Button(
                    onClick = onOrderSelect,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFFF)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "是",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedOrderSection(
    order: Order,
    onOrderChange: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOrderChange() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "咨询订单：${order.orderId}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "金长风荷叶烤鸡 - ¥10.1",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "更换订单",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun ChatMessagesSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 这里可以添加聊天消息
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂时没有消息记录",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun InputSection(
    inputText: String,
    onInputChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "语音输入",
                    tint = Color.Gray
                )
            }

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF5F5F5)
            ) {
                TextField(
                    value = inputText,
                    onValueChange = onInputChange,
                    placeholder = {
                        Text(
                            text = "请输入您的问题，建议不要分句哦～",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.SentimentSatisfied,
                    contentDescription = "表情",
                    tint = Color.Gray
                )
            }

            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "更多",
                    tint = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSelectorDialog(
    onDismiss: () -> Unit,
    onOrderSelected: (com.example.myele.model.Order) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "选择订单",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 示例订单列表
            val orders = listOf(
                "金长风荷叶烤鸡 (已送达) - ¥10.1",
                "川香麻辣烫 (进行中) - ¥35.0",
                "瑞幸咖啡 (已完成) - ¥19.9"
            )

            orders.forEachIndexed { index, orderInfo ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            // 这里应该创建真实的Order对象
                            onDismiss()
                        }
                        .padding(vertical = 4.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Text(
                        text = orderInfo,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
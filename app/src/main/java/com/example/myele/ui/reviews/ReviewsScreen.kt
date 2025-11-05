package com.example.myele.ui.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.data.ActionLogger
import com.example.myele.model.Order
import com.example.myele.model.OrderStatus
import com.example.myele.model.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(navController: NavController, repository: DataRepository) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("待评价") }
    val orders = remember { repository.getOrders() }
    val reviews = remember { repository.getReviews() }

    // 记录进入待评价页面
    LaunchedEffect(Unit) {
        ActionLogger.logAction(
            context = context,
            action = "enter_reviews_page",
            page = "reviews",
            pageInfo = mapOf(
                "title" to "评价中心",
                "screen_name" to "ReviewsScreen"
            ),
            extraData = mapOf(
                "selected_tab" to "待评价",
                "source" to "profile"
            )
        )
    }

    // 筛选待评价订单
    val pendingReviewOrders = orders.filter {
        it.status == OrderStatus.COMPLETED && !it.hasReview
    }

    // 已评价列表
    val completedReviews = reviews.filter { review ->
        orders.any { order -> order.orderId == review.orderId && order.hasReview }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("评价中心") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    TextButton(onClick = { }) {
                        Text("规则", color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 用户信息区域
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 头像
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "头像",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "eleme40825052330370  68",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "累计评价 4",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 统计数据
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("0", "已获红包")
                        StatItem("15", "已获吃货豆")
                        StatItem("3", "被浏览")
                        StatItem("0", "被点赞")
                    }
                }
            }

            // 评价官推广
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF7E57C2)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "解锁评价官,赢专属头衔",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "写2条 20字+1图 评价即可解锁",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("去评价", color = Color(0xFF7E57C2))
                    }
                }
            }

            // 标签页
            TabRow(
                selectedTabIndex = if (selectedTab == "待评价") 0 else 1,
                containerColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == "待评价",
                    onClick = { selectedTab = "待评价" },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "待评价",
                                fontSize = 15.sp,
                                fontWeight = if (selectedTab == "待评价") FontWeight.Bold else FontWeight.Normal
                            )
                            if (selectedTab == "待评价") {
                                Text(
                                    text = "(${pendingReviewOrders.size})",
                                    fontSize = 12.sp,
                                    color = Color(0xFF00BFFF)
                                )
                            }
                        }
                    }
                )
                Tab(
                    selected = selectedTab == "已评价",
                    onClick = {
                        selectedTab = "已评价"
                        // 记录切换到已评价
                        ActionLogger.logAction(
                            context = context,
                            action = "switch_to_reviewed",
                            page = "reviews",
                            pageInfo = mapOf(
                                "title" to "评价中心",
                                "screen_name" to "ReviewsScreen"
                            ),
                            extraData = mapOf(
                                "selected_tab" to "已评价",
                                "switched_from" to "待评价"
                            )
                        )
                    },
                    text = {
                        Text(
                            text = "已评价",
                            fontSize = 15.sp,
                            fontWeight = if (selectedTab == "已评价") FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == "已评价") Color(0xFF00BFFF) else Color.Black
                        )
                    }
                )
            }

            // 评价列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedTab == "待评价") {
                    items(pendingReviewOrders.size) { index ->
                        PendingReviewCard(pendingReviewOrders[index])
                    }
                } else {
                    items(completedReviews.size) { index ->
                        val review = completedReviews[index]
                        val order = orders.find { order -> order.orderId == review.orderId }
                        if (order != null) {
                            CompletedReviewCard(review, order, context)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun PendingReviewCard(order: Order) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.restaurantName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "2025-10-19",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 商品信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    // 商品图片
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = order.items.firstOrNull()?.productName ?: "",
                            fontSize = 14.sp,
                            maxLines = 2
                        )
                        if (order.items.size > 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "等${order.items.size}件商品",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "共${order.items.sumOf { it.quantity }}件",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 评价奖励
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = Color(0xFFFF6B00),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "评价得1元红包",
                        fontSize = 13.sp,
                        color = Color(0xFFFF6B00)
                    )
                }

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFFF)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 6.dp)
                ) {
                    Text("评价", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun CompletedReviewCard(review: Review, order: Order, context: android.content.Context) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeleteSuccess by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFFF6B00), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.restaurantName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "2025-10-18",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 满意度星级
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "满意度",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                repeat(review.rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "味道5星  包装5星",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // 评价内容
            if (review.comment != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = review.comment,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            // 评价图片
            if (review.images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(review.images.take(3)) { _ ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.LightGray, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // 点赞商品
            if (review.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("点赞:", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(review.tags) { tag ->
                            Text(
                                text = "【$tag】",
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // 商家回复
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row {
                        Text(
                            text = "商家回复",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "蟹蟹亲的好评，看到亲的表扬，心里暖暖的，您的认可就是我们继续努力的动力，希望有机会为亲再次服务哦~",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // 操作按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (review.canDelete) {
                    TextButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("删除", fontSize = 13.sp)
                    }
                }
            }
        }
    }

    // 删除确认弹窗
    if (showDeleteDialog) {
        DeleteReviewDialog(
            onDismiss = { showDeleteDialog = false },
            onDelete = {
                // 记录删除评价操作
                ActionLogger.logAction(
                    context = context,
                    action = "delete_review",
                    page = "reviews",
                    pageInfo = mapOf(
                        "title" to "评价中心",
                        "screen_name" to "ReviewsScreen"
                    ),
                    extraData = mapOf(
                        "review_id" to review.reviewId,
                        "order_id" to review.orderId,
                        "restaurant_name" to order.restaurantName,
                        "deleted_successfully" to true
                    )
                )
                showDeleteDialog = false
                showDeleteSuccess = true
            },
            onAnonymous = {
                // 记录设为匿名操作
                ActionLogger.logAction(
                    context = context,
                    action = "set_review_anonymous",
                    page = "reviews",
                    pageInfo = mapOf(
                        "title" to "评价中心",
                        "screen_name" to "ReviewsScreen"
                    ),
                    extraData = mapOf(
                        "review_id" to review.reviewId,
                        "order_id" to review.orderId
                    )
                )
                showDeleteDialog = false
            }
        )
    }

    // 删除成功弹窗
    if (showDeleteSuccess) {
        DeleteSuccessDialog(
            onDismiss = { showDeleteSuccess = false }
        )
    }
}

@Composable
fun DeleteReviewDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onAnonymous: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // 主弹窗卡片
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "删除评价不可恢复",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "不想让别人看到可以设为匿名哦",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 删除按钮
                        OutlinedButton(
                            onClick = onDelete,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Black
                            )
                        ) {
                            Text("删除", fontSize = 16.sp)
                        }

                        // 设为匿名按钮
                        Button(
                            onClick = onAnonymous,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00BFFF)
                            )
                        ) {
                            Text("设为匿名", fontSize = 16.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 关闭按钮
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Transparent, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "关闭",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun DeleteSuccessDialog(onDismiss: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1500)
        onDismiss()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "删除成功",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

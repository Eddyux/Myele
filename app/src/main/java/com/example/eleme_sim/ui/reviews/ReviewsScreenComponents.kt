package com.example.eleme_sim.ui.reviews

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.eleme_sim.R
import com.example.eleme_sim.data.ActionLogger
import com.example.eleme_sim.model.Order
import com.example.eleme_sim.model.Review
import com.example.eleme_sim.ui.components.ProductImage
import com.example.eleme_sim.utils.ImageUtils
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

private val reviewDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA)

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
                    text = formatReviewDate(order.orderTime),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    ProductImage(
                        productId = order.items.firstOrNull()?.productId.orEmpty(),
                        productName = order.items.firstOrNull()?.productName.orEmpty(),
                        restaurantName = order.restaurantName,
                        size = 64.dp,
                        cornerRadius = 8.dp
                    )

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
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
fun CompletedReviewCard(review: Review, order: Order, context: Context) {
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
                    Image(
                        painter = painterResource(id = ImageUtils.getRestaurantImage(order.restaurantName)),
                        contentDescription = order.restaurantName,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.restaurantName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = formatReviewDate(review.createTime),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                        tint = Color(0xFFFF8A00),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = merchantMetricsText(order.restaurantName),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            if (review.comment != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = review.comment,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            val reviewImages = reviewImageResources(review.reviewId, order.restaurantName)
            if (reviewImages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(reviewImages) { resId ->
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = "评价图片",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            } else if (review.images.isNotEmpty()) {
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

    if (showDeleteDialog) {
        DeleteReviewDialog(
            onDismiss = { showDeleteDialog = false },
            onDelete = {
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
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
        delay(1500)
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

private fun reviewImageResources(reviewId: String, restaurantName: String): List<Int> {
    return when {
        restaurantName.contains("湘味轩") -> listOf(
            R.drawable.reviews_pingjia_3
        )
        restaurantName.contains("川香麻辣烫") -> listOf(
            R.drawable.reviews_pingjia_1,
            R.drawable.reviews_pingjia_2
        )
        else -> emptyList()
    }
}

private fun merchantMetricsText(restaurantName: String): String {
    return when {
        restaurantName.contains("川香麻辣烫") -> "味道5星  包装5星"
        restaurantName.contains("湘味轩") -> "味道5星  包装5星"
        restaurantName.contains("老北京炸酱面") -> "味道5星  包装5星"
        restaurantName.contains("韩式炸鸡") -> "味道3星  包装4星"
        else -> "味道5星  包装5星"
    }
}

private fun formatReviewDate(date: Date): String {
    return Instant.ofEpochMilli(date.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(reviewDateFormatter)
}

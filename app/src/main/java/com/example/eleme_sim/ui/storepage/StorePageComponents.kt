package com.example.eleme_sim.ui.storepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.model.Product
import com.example.eleme_sim.model.ProductCategory
import com.example.eleme_sim.model.Restaurant
import com.example.eleme_sim.ui.components.RestaurantImage
import com.example.eleme_sim.ui.components.ProductImage

// 商家详情页配色（暖色调，贴近参考图）
internal val StoreBg = Color(0xFFF6F4F0)
internal val StoreCardBg = Color(0xFFFFFCF6)
internal val StoreBrand = Color(0xFF0AA5F0)
internal val StorePrice = Color(0xFFFF3B30)
internal val StoreTextPrimary = Color(0xFF1F1F1F)
internal val StoreTextSecondary = Color(0xFF9A958C)
internal val StoreDivider = Color(0xFFE6DFD4)
internal val StoreWarm = Color(0xFFA85C1B)
internal val StoreWarmChip = Color(0xFFFFF0D8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePageTopBar(
    navController: NavController,
    isFollowed: Boolean,
    onFollowClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            CircleIconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回", tint = StoreTextPrimary)
            }
        },
        actions = {
            CircleIconButton(onClick = { /* Search */ }) {
                Icon(Icons.Default.Search, contentDescription = "搜索", tint = StoreTextPrimary)
            }
            CircleIconButton(onClick = { /* Chat */ }) {
                Icon(Icons.Default.Chat, contentDescription = "聊天", tint = StoreTextPrimary)
            }
            CircleIconButton(onClick = onFollowClick) {
                Icon(
                    imageVector = if (isFollowed) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "收藏",
                    tint = if (isFollowed) StorePrice else StoreTextPrimary
                )
            }
            CircleIconButton(onClick = onMoreClick) {
                Icon(Icons.Default.MoreVert, contentDescription = "更多", tint = StoreTextPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

@Composable
private fun CircleIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .size(34.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.92f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.size(20.dp)) { content() }
    }
}

@Composable
fun StoreHeader(restaurant: Restaurant) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 64.dp,
                    cornerRadius = 14.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = restaurant.name,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StoreTextPrimary
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "${restaurant.rating}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF8A00),
                            modifier = Modifier.padding(start = 3.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "月售${restaurant.salesVolume}+",
                            fontSize = 12.sp,
                            color = StoreTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "${restaurant.deliveryTime}分钟 | ${restaurant.distance}km",
                        fontSize = 12.sp,
                        color = StoreTextSecondary
                    )
                }
            }

            // Delivery options
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(19.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StoreBrand)
                ) {
                    Text("外送", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(19.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = BorderStroke(1.dp, StoreDivider),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StoreTextPrimary)
                ) {
                    Text("自取", fontSize = 14.sp)
                }
            }

            // Coupons
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StoreWarmChip, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFFE8630A),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "满减优惠 | 新用户立减",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFC24E06)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFFC24E06),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: ProductCategory?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val categoryName = when (category) {
        null -> "全部"
        ProductCategory.HOT_SALE -> "热销"
        ProductCategory.SIGNATURE -> "招牌"
        ProductCategory.NEW_ARRIVAL -> "新品"
        ProductCategory.SPECIAL -> "优惠"
        ProductCategory.COMBO -> "套餐"
        ProductCategory.BEVERAGE -> "饮品"
        ProductCategory.MAIN_DISH -> "主食"
        ProductCategory.SIDE_DISH -> "小菜"
        ProductCategory.DESSERT -> "甜品"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) Color(0xFFFFFCF6) else Color.Transparent)
            .padding(vertical = 15.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(3.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(StoreBrand)
            )
        }
        Text(
            text = categoryName,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) StoreTextPrimary else StoreTextSecondary
        )
    }
}

@Composable
fun SignatureProductCard(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(120.dp)
            .height(178.dp),
        shape = RoundedCornerShape(14.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProductImage(
                productId = product.productId,
                productName = product.name,
                restaurantName = product.restaurantName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                size = 100.dp,
                cornerRadius = 14.dp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    color = StoreTextPrimary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "¥${product.price}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StorePrice
                        )
                        product.originalPrice?.let { originalPrice ->
                            Text(
                                text = "¥$originalPrice",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    // Quantity controls
                    if (quantity > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                onClick = onRemove,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "减少",
                                    tint = StoreBrand,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "$quantity",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = onAdd,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "添加",
                                    tint = StoreBrand,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else {
                        IconButton(
                            onClick = onAdd,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "添加",
                                tint = StoreBrand,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        ProductImage(
            productId = product.productId,
            productName = product.name,
            restaurantName = product.restaurantName,
            modifier = Modifier.size(84.dp),
            size = 84.dp,
            cornerRadius = 12.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = StoreTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (product.monthSales > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "月售${product.monthSales}",
                    fontSize = 12.sp,
                    color = StoreTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "¥${product.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = StorePrice
                    )
                    product.originalPrice?.let { originalPrice ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "¥$originalPrice",
                            fontSize = 12.sp,
                            color = StoreTextSecondary,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // Quantity controls
                if (quantity > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onRemove,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "减少",
                                tint = StoreBrand
                            )
                        }
                        Text(
                            text = "$quantity",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = onAdd,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "添加",
                                tint = StoreBrand
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(StoreBrand)
                            .clickable { onAdd() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "添加",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomCheckoutBar(
    totalQuantity: Int,
    totalPrice: Double,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF2C2C2C),
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(StoreBrand),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color.White
                    )
                    if (totalQuantity > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.TopEnd)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Red),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$totalQuantity",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "¥%.2f".format(totalPrice),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "配送费另计",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            Button(
                onClick = onCheckout,
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StoreBrand),
                modifier = Modifier.height(44.dp)
            ) {
                Text("去结算", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StoreTabRow(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 4.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            val selected = index == selectedTab
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = if (selected) 16.sp else 15.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) StoreTextPrimary else StoreTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(22.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (selected) StoreBrand else Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun PlaceholderContent(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title 内容待开发",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

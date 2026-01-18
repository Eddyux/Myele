package com.example.myele.ui.searchresult

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.model.Restaurant

@Composable
fun TopBar(keyword: String, onBackClicked: () -> Unit, navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.Black
                )
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navController.popBackStack()
                        navController.navigate(com.example.myele.navigation.Screen.Search.route)
                    },
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = keyword,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Text(
                text = "切换地址",
                color = Color(0xFF00BFFF),
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun SortOptions(
    selectedSortType: SortType,
    salesSelected: Boolean = false,
    speedSelected: Boolean = false,
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onSalesClicked: () -> Unit = {},
    onSpeedClicked: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onSortClicked() }
            ) {
                Text(
                    text = when (selectedSortType) {
                        SortType.COMPREHENSIVE -> "综合排序"
                        SortType.PRICE_LOW_TO_HIGH -> "人均价低到高"
                        SortType.DISTANCE -> "距离优先"
                        SortType.RATING -> "商家好评优先"
                        SortType.MIN_DELIVERY -> "起送低到高"
                    },
                    fontSize = 14.sp,
                    color = Color(0xFF00BFFF),
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF00BFFF),
                    modifier = Modifier.size(16.dp)
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (salesSelected) Color(0xFF00BFFF) else Color.Transparent,
                modifier = Modifier.clickable { onSalesClicked() }
            ) {
                Text(
                    text = "销量优先",
                    fontSize = 14.sp,
                    color = if (salesSelected) Color.White else Color.Black,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (speedSelected) Color(0xFF00BFFF) else Color.Transparent,
                modifier = Modifier.clickable { onSpeedClicked() }
            ) {
                Text(
                    text = "速度优先",
                    fontSize = 14.sp,
                    color = if (speedSelected) Color.White else Color.Black,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onFilterClicked() }
            ) {
                Text(
                    text = "筛选",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 2.dp)
                )
            }
        }
    }
}

@Composable
fun SortDialogOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (isSelected) Color(0xFF00BFFF) else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF00BFFF)
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFF00BFFF) else Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun SearchResultRestaurantCard(restaurant: Restaurant, navController: NavController, keyword: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                navController.navigate(com.example.myele.navigation.Screen.StorePage.createRoute(restaurant.restaurantId, keyword))
            },
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // 店铺信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 店铺图片
                com.example.myele.ui.components.RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 60.dp,
                    cornerRadius = 8.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // 店铺名称和标签
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = restaurant.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        // 蓝骑士配送标签
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFE3F2FD)
                        ) {
                            Text(
                                text = "蜂鸟专送",
                                fontSize = 10.sp,
                                color = Color(0xFF00BFFF),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 评分和销量
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "月售${restaurant.salesVolume}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "起送¥${restaurant.minDeliveryAmount}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 评分信息
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${restaurant.rating}",
                            fontSize = 12.sp,
                            color = Color(0xFFFFB800),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${restaurant.deliveryTime}分钟",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${restaurant.distance}km",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 优惠活动
            if (restaurant.coupons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(restaurant.coupons.take(3)) { coupon ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFF8E1)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = Color(0xFFFF6B00),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "满减优惠",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFF6B00),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // 商品展示区域
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "7.5元红包门槛",
                fontSize = 12.sp,
                color = Color(0xFFFF3366),
                modifier = Modifier
                    .background(Color(0xFFFFF0F0), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 商品列表
            if (restaurant.products.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(restaurant.products.take(3)) { product ->
                        ProductItem(product)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: com.example.myele.model.Product) {
    Column(
        modifier = Modifier.width(80.dp)
    ) {
        com.example.myele.ui.components.ProductImage(
            productId = product.productId,
            productName = product.name,
            size = 80.dp,
            cornerRadius = 8.dp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = product.name,
            fontSize = 12.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = if (product.originalPrice != null && product.originalPrice > product.price) {
                "¥${product.price}"
            } else {
                "¥${product.price} 起"
            },
            fontSize = 12.sp,
            color = Color(0xFFFF6B00),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CouponBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF0F0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFFF3366)
                ) {
                    Text(
                        text = "¥6",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "满20可用 10-22-49后失效",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }

            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3366)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("适用商家", fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSlider(
    sliderPosition: ClosedFloatingPointRange<Float> = 0f..120f,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "¥${sliderPosition.start.toInt()}",
                fontSize = 14.sp,
                color = Color(0xFF00BFFF),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (sliderPosition.endInclusive >= 120f) "¥120+" else "¥${sliderPosition.endInclusive.toInt()}",
                fontSize = 14.sp,
                color = Color(0xFF00BFFF),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = sliderPosition,
            onValueChange = onValueChange,
            valueRange = 0f..120f,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00BFFF),
                activeTrackColor = Color(0xFF00BFFF),
                inactiveTrackColor = Color(0xFFE0E0E0)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

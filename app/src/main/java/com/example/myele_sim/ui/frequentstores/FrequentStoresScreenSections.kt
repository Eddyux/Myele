package com.example.myele_sim.ui.frequentstores

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myele_sim.model.Restaurant
import com.example.myele_sim.ui.components.RestaurantImage

@Composable
fun FilterSection(
    selectedFilter: Int,
    onFilterSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 全部分类 - 带下拉箭头
            FilterButton(
                text = "全部分类",
                selected = selectedFilter == 0,
                hasDropdown = true,
                onClick = { onFilterSelected(0) }
            )

            // 附近点过
            FilterButton(
                text = "附近点过",
                selected = selectedFilter == 1,
                hasDropdown = false,
                onClick = { onFilterSelected(1) }
            )

            // 点过最多 - 带下拉箭头
            FilterButton(
                text = "点过最多",
                selected = selectedFilter == 2,
                hasDropdown = true,
                onClick = { onFilterSelected(2) }
            )

            // 可配送 - 蓝色高亮
            FilterButton(
                text = "可配送",
                selected = selectedFilter == 3,
                hasDropdown = false,
                isHighlight = true,
                onClick = { onFilterSelected(3) }
            )
        }
    }
}

@Composable
fun FrequentStoreCard(
    restaurant: Restaurant,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 店铺图片
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 80.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = restaurant.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // 品牌点过次数
                    Text(
                        text = "该品牌点过3次",
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B00)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // 配送信息
                    Row {
                        Text(
                            text = "满${restaurant.minDeliveryAmount.toInt()}元",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "配送¥${restaurant.deliveryFee}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "约${restaurant.deliveryTime}分钟",
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

                    // 优惠活动
                    if (restaurant.coupons.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Surface(
                                shape = RoundedCornerShape(2.dp),
                                color = Color(0xFFFFE0E0)
                            ) {
                                Text(
                                    text = "6元无门槛",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF0000),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = RoundedCornerShape(2.dp),
                                color = Color(0xFFFFE0E0)
                            ) {
                                Text(
                                    text = "20减6.5",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF0000),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 常点的商品（如果有）
            if (!restaurant.products.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "常点",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(restaurant.products.take(3)) { product ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(80.dp)
                        ) {
                            // 商品图片占位符
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = product.name,
                                fontSize = 11.sp,
                                color = Color.Black,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SatisfactionSurvey() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { /* TODO */ },
        color = Color(0xFFFFF8E1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color(0xFFFF6B00),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "您对常点提供的服务满意吗？",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun FollowedRestaurantCard(restaurant: Restaurant) {
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
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 60.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = restaurant.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (restaurant.distance > 5.0) {
                        Text(
                            text = "超出配送范围",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    } else {
                        Row {
                            Text(
                                text = "${restaurant.rating}分",
                                fontSize = 12.sp,
                                color = Color(0xFFFF6B00)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "月售${restaurant.salesVolume}+",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "好评率98%",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    if (restaurant.coupons.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Surface(
                                shape = RoundedCornerShape(2.dp),
                                color = Color(0xFFFFE0E0)
                            ) {
                                Text(
                                    text = "满20减5",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF0000),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = Color.Gray
                )
            }
        }
    }
}

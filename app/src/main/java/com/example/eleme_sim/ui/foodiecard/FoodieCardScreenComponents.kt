package com.example.eleme_sim.ui.foodiecard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eleme_sim.model.Coupon
import com.example.eleme_sim.model.Restaurant
import com.example.eleme_sim.ui.components.RestaurantImage

@Composable
fun ExplosivePackageCard(pkg: Coupon) {
    Surface(
        modifier = Modifier
            .width(200.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF3E0)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "爆红包商家专享",
                        fontSize = 12.sp,
                        color = Color(0xFFFF3366)
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "5",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B00)
                        )
                        Text(text = "元×", fontSize = 12.sp)
                        Text(
                            text = "6",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B00)
                        )
                        Text(text = "张", fontSize = 12.sp)
                    }
                    Text(
                        text = "无门槛",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFF3366).copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "爆红包",
                        fontSize = 10.sp,
                        color = Color(0xFFFF3366),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "可免费爆涨",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B00)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("抢购", fontSize = 12.sp)
                }
            }

            Text(
                text = "赠 22元×2张 满58可用",
                fontSize = 10.sp,
                color = Color(0xFFFF3366),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        color = if (selected) Color.Black else Color.Gray,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(bottom = 4.dp)
    )
}

@Composable
fun SortButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = if (selected) Color(0xFFFF3366) else Color.Black,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // 餐厅图片
            RestaurantImage(
                restaurantName = restaurant.name,
                size = 80.dp,
                cornerRadius = 8.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = restaurant.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "约${restaurant.deliveryTime} | ${restaurant.distance}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (restaurant.deliveryFee == 0.0) {
                    Text(
                        text = "蓝骑士准时达",
                        fontSize = 11.sp,
                        color = Color(0xFF00BFFF)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 优惠标签
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(listOf("赚 6元", "29减3", "45减5", "65减8", "85减12")) { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFF3E0)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = Color(0xFFFF6B00),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                if (restaurant.deliveryFee == 0.0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = Color(0xFFFF6B00),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "7.5元无门槛",
                            fontSize = 12.sp,
                            color = Color(0xFFFF6B00),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

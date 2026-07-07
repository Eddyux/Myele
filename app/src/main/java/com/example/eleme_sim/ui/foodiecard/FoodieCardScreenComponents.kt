package com.example.eleme_sim.ui.foodiecard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eleme_sim.model.Coupon
import com.example.eleme_sim.model.Restaurant
import com.example.eleme_sim.ui.components.RestaurantImage

@Composable
fun ExplosivePackageCard(pkg: Coupon) {
    Surface(
        modifier = Modifier
            .width(206.dp)
            .clickable { },
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9EF),
                        Color(0xFFFFF2D8)
                    )
                )
            )
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp),
                color = Color(0xFFFF7D6B),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp)
            ) {
                Text(
                    text = "爆红包",
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "爆红包商家专享",
                    fontSize = 13.sp,
                    color = Color(0xFFFF7D57),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "5",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFB56A13)
                    )
                    Text(
                        text = "元×",
                        fontSize = 14.sp,
                        color = Color(0xFFB56A13),
                        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                    )
                    Text(
                        text = "6",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFB56A13)
                    )
                    Text(
                        text = "张",
                        fontSize = 14.sp,
                        color = Color(0xFFB56A13),
                        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "无门槛",
                    fontSize = 13.sp,
                    color = Color(0xFF8E7660)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "可免费爆涨",
                    fontSize = 14.sp,
                    color = Color(0xFFB56A13),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "赠 22元×2张 满58可用",
                        fontSize = 11.sp,
                        color = Color(0xFFFF7D6B),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF59A38)
                        ),
                        shape = RoundedCornerShape(18.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp),
                        modifier = Modifier
                            .width(76.dp)
                            .height(34.dp)
                    ) {
                        Text(
                            text = "抢购",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color(0xFF18120D) else Color(0xFF7D756C)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(if (selected) 34.dp else 10.dp)
                .height(4.dp)
                .background(
                    if (selected) Color(0xFFF59A38) else Color.Transparent,
                    RoundedCornerShape(4.dp)
                )
        )
    }
}

@Composable
fun SortButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) Color(0xFFFFF3E3) else Color.Transparent,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color(0xFFDD5A3C) else Color(0xFF3E3228),
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            RestaurantImage(
                restaurantName = restaurant.name,
                size = 90.dp,
                cornerRadius = 18.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = restaurant.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF18120D),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${restaurant.rating}分",
                        fontSize = 13.sp,
                        color = Color(0xFFF28D2E),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "月售${restaurant.salesVolume}+",
                        fontSize = 12.sp,
                        color = Color(0xFF7E786F)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "约${restaurant.deliveryTime} | ${restaurant.distance}",
                    fontSize = 12.sp,
                    color = Color(0xFF7E786F)
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (restaurant.deliveryFee == 0.0) {
                    Surface(
                        color = Color(0xFFF1FBFF),
                        border = BorderStroke(1.dp, Color(0xFFD7F3FF)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "蓝骑士准时达",
                            fontSize = 11.sp,
                            color = Color(0xFF21A8D9),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf("赚 6元", "29减3", "45减5", "65减8", "85减12")) { tag ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF5E6)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = Color(0xFFDA6A2D),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (restaurant.deliveryFee == 0.0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF2DA)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = Color(0xFFF28D2E),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "7.5元无门槛",
                                    fontSize = 12.sp,
                                    color = Color(0xFFF28D2E),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.eleme_sim.ui.foodiecard

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eleme_sim.model.Coupon
import com.example.eleme_sim.model.SuperFoodieCard

@Composable
fun FoodieHeroBackdrop(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF734C47),
                    Color(0xFFB77856),
                    Color(0xFFD69E63),
                    Color(0xFF6C4357)
                )
            )
        )
    ) {
        Surface(
            modifier = Modifier
                .size(180.dp)
                .offset(x = 190.dp, y = 18.dp),
            shape = CircleShape,
            color = Color(0x22FFF3D2)
        ) {}
        Surface(
            modifier = Modifier
                .size(90.dp)
                .offset(x = 24.dp, y = 120.dp),
            shape = CircleShape,
            color = Color(0x12FFF8E8)
        ) {}
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFC931),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 34.dp, end = 42.dp)
                .size(92.dp)
                .graphicsLayer(rotationZ = 15f)
        )
    }
}

@Composable
fun PurchaseFoodieCardSection(foodieCards: List<SuperFoodieCard>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "购买超级吃货卡",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF17110C)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "使用范围",
                        fontSize = 13.sp,
                        color = Color(0xFF90867D)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFB9B0A6),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            foodieCards.forEach { card ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFA8CE58),
                                        Color(0xFFD9F0A6)
                                    )
                                ),
                                shape = RoundedCornerShape(26.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.width(136.dp),
                                shape = RoundedCornerShape(22.dp),
                                color = Color.White
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "爆红包商家专享",
                                        fontSize = 13.sp,
                                        color = Color(0xFF8E8C63),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = "5",
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF6F8D11)
                                        )
                                        Text(
                                            text = "元",
                                            fontSize = 16.sp,
                                            color = Color(0xFF6F8D11),
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        Text(
                                            text = "×",
                                            fontSize = 16.sp,
                                            color = Color(0xFF9AA55F),
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp)
                                                .padding(bottom = 4.dp)
                                        )
                                        Text(
                                            text = "6",
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF6F8D11)
                                        )
                                        Text(
                                            text = "张",
                                            fontSize = 16.sp,
                                            color = Color(0xFF6F8D11),
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = Color.White.copy(alpha = 0.95f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(start = 16.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "¥${card.price}",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF6E8D11)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "¥39",
                                        fontSize = 12.sp,
                                        color = Color(0xFFA0A69B),
                                        style = LocalTextStyle.current.copy(
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Button(
                                        onClick = { },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFF2AE32)
                                        ),
                                        shape = RoundedCornerShape(18.dp),
                                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp),
                                        modifier = Modifier.height(34.dp)
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
            }
        }
    }
}

@Composable
fun ExplosivePackagesSection(explosivePackages: List<Coupon>) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        contentPadding = PaddingValues(end = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(explosivePackages) { pkg ->
            ExplosivePackageCard(pkg)
        }
    }
}

@Composable
fun TrainTicketAdSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF6B44BA),
                        Color(0xFF4C2D8F)
                    )
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "火车票神券",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "开卡得APP最高新客券",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.82f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF6EE57E)
                ) {
                    Icon(
                        imageVector = Icons.Default.Train,
                        contentDescription = null,
                        tint = Color(0xFF235F2A),
                        modifier = Modifier
                            .padding(16.dp)
                            .size(34.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MerchantCategoryTabsSection(selectedTab: String, onTabSelected: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 10.dp)
        ) {
            TabButton(
                text = "爆红包商家",
                selected = selectedTab == "爆红包商家",
                onClick = { onTabSelected("爆红包商家") }
            )
            Spacer(modifier = Modifier.width(24.dp))
            TabButton(
                text = "全平台商家",
                selected = selectedTab == "全平台商家",
                onClick = { onTabSelected("全平台商家") }
            )
        }
    }
}

@Composable
fun SortAndFilterSection(selectedSort: String, onSortSelected: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SortButton("综合排序", selectedSort == "综合排序") {
                    onSortSelected("综合排序")
                }
                Spacer(modifier = Modifier.width(8.dp))
                SortButton("人气优先", selectedSort == "人气优先") {
                    onSortSelected("人气优先")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "商家品类",
                    fontSize = 14.sp,
                    color = Color(0xFF3E3228),
                    modifier = Modifier
                        .clickable { }
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8F4EE)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = Color(0xFF786C61),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .size(18.dp)
                )
            }
        }
    }
}

@Composable
fun FilterTagsSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp),
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
        color = Color.White
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(listOf("减配送费", "品牌商家", "蓝骑士专送")) { tag ->
                FilterChip(
                    selected = false,
                    onClick = { },
                    label = {
                        Text(
                            text = tag,
                            fontSize = 12.sp,
                            color = Color(0xFF6D645D)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFFF8F4EE),
                        labelColor = Color(0xFF6D645D)
                    ),
                    border = null
                )
            }
        }
    }
}

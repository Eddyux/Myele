package com.example.myele_sim.ui.foodiecard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myele_sim.model.Coupon
import com.example.myele_sim.model.SuperFoodieCard

@Composable
fun PurchaseFoodieCardSection(foodieCards: List<SuperFoodieCard>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "购买超级吃货卡",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "使用范围 ⓘ",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 超级吃货卡促销卡片
            foodieCards.forEach { card ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "爆红包商家专享",
                                fontSize = 14.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "5",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF6B00)
                                )
                                Text(
                                    text = "元",
                                    fontSize = 14.sp,
                                    color = Color(0xFFFF6B00)
                                )
                                Text(
                                    text = "×",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Text(
                                    text = "6",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF6B00)
                                )
                                Text(
                                    text = "张",
                                    fontSize = 14.sp,
                                    color = Color(0xFFFF6B00)
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "含1盒Zeal罐头+8元宠物红包",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "¥${card.price}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF3366)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "¥39",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    style = LocalTextStyle.current.copy(
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF6B00)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp)
                            ) {
                                Text("抢购", fontSize = 14.sp)
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
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF5E35B1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "火车票神券",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "开卡得APP最高新客券",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            Icon(
                imageVector = Icons.Default.Train,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun MerchantCategoryTabsSection(selectedTab: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        TabButton(
            text = "爆红包商家",
            selected = selectedTab == "爆红包商家",
            onClick = { onTabSelected("爆红包商家") }
        )
        Spacer(modifier = Modifier.width(16.dp))
        TabButton(
            text = "全平台商家",
            selected = selectedTab == "全平台商家",
            onClick = { onTabSelected("全平台商家") }
        )
    }
}

@Composable
fun SortAndFilterSection(selectedSort: String, onSortSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            SortButton("综合排序", selectedSort == "综合排序") {
                onSortSelected("综合排序")
            }
            Spacer(modifier = Modifier.width(12.dp))
            SortButton("人气优先", selectedSort == "人气优先") {
                onSortSelected("人气优先")
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "商家品类",
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { }
                    .padding(8.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜索",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun FilterTagsSection() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listOf("减配送费", "品牌商家", "蓝骑士专送")) { tag ->
            FilterChip(
                selected = false,
                onClick = { },
                label = { Text(tag, fontSize = 12.sp) }
            )
        }
    }
}

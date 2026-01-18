package com.example.myele.ui.takeout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryIcons(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        CategoryItem("精选", "⭐"),
        CategoryItem("汉堡薯条", "🍔"),
        CategoryItem("地方菜系", "🍲"),
        CategoryItem("快餐便当", "🍱"),
        CategoryItem("奶茶咖啡", "☕"),
        CategoryItem("全部", "≡")
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(categories.size) { index ->
                CategoryIcon(
                    category = categories[index],
                    selected = selectedCategory == categories[index].name,
                    onClick = { onCategorySelected(categories[index].name) }
                )
            }
        }
    }
}

@Composable
fun SortAndFilter(
    selectedSortType: SortType,
    speedSelected: Boolean = false,
    selectedFunctionButton: String? = null,
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onSpeedClicked: () -> Unit = {},
    onShuffleClicked: () -> Unit = {},
    onRedPacketClicked: () -> Unit = {},
    onDeliveryFeeClicked: () -> Unit = {},
    onNoThresholdClicked: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column {
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
                            SortType.DELIVERY_SPEED -> "配送最快"
                        },
                        fontSize = 14.sp,
                        color = Color(0xFF00BFFF),
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color(0xFF00BFFF),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (speedSelected) Color(0xFF00BFFF) else Color.Transparent,
                    modifier = Modifier.clickable { onSpeedClicked() }
                ) {
                    Text(
                        text = "速度",
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
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedFunctionButton == "换一换") Color(0xFF00BFFF) else Color(0xFFF5F5F5),
                        modifier = Modifier.clickable { onShuffleClicked() }
                    ) {
                        Text(
                            text = "换一换",
                            fontSize = 12.sp,
                            color = if (selectedFunctionButton == "换一换") Color.White else Color.Black,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedFunctionButton == "天天爆红包") Color(0xFF00BFFF) else Color(0xFFF5F5F5),
                        modifier = Modifier.clickable { onRedPacketClicked() }
                    ) {
                        Text(
                            text = "天天爆红包",
                            fontSize = 12.sp,
                            color = if (selectedFunctionButton == "天天爆红包") Color.White else Color.Black,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedFunctionButton == "减配送费") Color(0xFF00BFFF) else Color(0xFFF5F5F5),
                        modifier = Modifier.clickable { onDeliveryFeeClicked() }
                    ) {
                        Text(
                            text = "减配送费",
                            fontSize = 12.sp,
                            color = if (selectedFunctionButton == "减配送费") Color.White else Color.Black,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedFunctionButton == "无门槛红包") Color(0xFF00BFFF) else Color(0xFFF5F5F5),
                        modifier = Modifier.clickable { onNoThresholdClicked() }
                    ) {
                        Text(
                            text = "无门槛红包",
                            fontSize = 12.sp,
                            color = if (selectedFunctionButton == "无门槛红包") Color.White else Color.Black,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

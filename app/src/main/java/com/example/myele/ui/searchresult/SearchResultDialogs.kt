package com.example.myele.ui.searchresult

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 排序弹窗 - 从综合排序按钮向下展开
@Composable
fun SortDialog(
    selectedSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 100.dp)
                .clickable(enabled = false) { },
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                SortDialogOption(
                    text = "综合排序",
                    isSelected = selectedSortType == SortType.COMPREHENSIVE,
                    onClick = { onSortTypeSelected(SortType.COMPREHENSIVE) }
                )
                SortDialogOption(
                    text = "人均价低到高",
                    isSelected = selectedSortType == SortType.PRICE_LOW_TO_HIGH,
                    onClick = { onSortTypeSelected(SortType.PRICE_LOW_TO_HIGH) }
                )
                SortDialogOption(
                    text = "距离优先",
                    isSelected = selectedSortType == SortType.DISTANCE,
                    onClick = { onSortTypeSelected(SortType.DISTANCE) }
                )
                SortDialogOption(
                    text = "商家好评优先",
                    isSelected = selectedSortType == SortType.RATING,
                    onClick = { onSortTypeSelected(SortType.RATING) }
                )
                SortDialogOption(
                    text = "起送低到高",
                    isSelected = selectedSortType == SortType.MIN_DELIVERY,
                    onClick = { onSortTypeSelected(SortType.MIN_DELIVERY) }
                )
            }
        }
    }
}

// 筛选弹窗
@Composable
fun FilterDialog(
    keyword: String = "",
    onDismiss: () -> Unit,
    onConfirm: (FilterOptions) -> Unit
) {
    var selectedPromotions by remember { mutableStateOf(setOf<String>()) }
    var selectedFeatures by remember { mutableStateOf(setOf<String>()) }
    var sliderPosition by remember { mutableStateOf(0f..120f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopEnd)
                .padding(top = 120.dp)
                .clickable(enabled = false) { },
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
            ) {
            item {
                // 优惠活动
                Text(
                    text = "优惠活动",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    listOf("首次光顾减", "满减优惠", "下单返红包", "配送费优惠", "特价商品", "0元起送").forEach { promotion ->
                        FilterChip(
                            text = promotion,
                            isSelected = promotion in selectedPromotions,
                            onClick = {
                                selectedPromotions = if (promotion in selectedPromotions) {
                                    selectedPromotions - promotion
                                } else {
                                    selectedPromotions + promotion
                                }
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                // 商家特色
                Text(
                    text = "商家特色",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    listOf("蜂鸟准时达", "到店自取", "品牌商家", "新店", "食无忧", "跨天预订", "线上开票", "慢必赔").forEach { feature ->
                        FilterChip(
                            text = feature,
                            isSelected = feature in selectedFeatures,
                            onClick = {
                                selectedFeatures = if (feature in selectedFeatures) {
                                    selectedFeatures - feature
                                } else {
                                    selectedFeatures + feature
                                }
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                // 价格筛选
                Text(
                    text = "价格筛选",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item {
                PriceRangeSlider(
                    sliderPosition = sliderPosition,
                    onValueChange = { sliderPosition = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                // 底部按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            selectedPromotions = setOf()
                            selectedFeatures = setOf()
                            sliderPosition = 0f..120f
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE3F2FD)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "清空",
                            color = Color(0xFF00BFFF),
                            fontSize = 16.sp
                        )
                    }
                    Button(
                        onClick = {
                            val filterOptions = FilterOptions(
                                promotions = selectedPromotions,
                                features = selectedFeatures,
                                priceRange = if (sliderPosition != 0f..120f) {
                                    Pair(sliderPosition.start, sliderPosition.endInclusive)
                                } else null
                            )
                            onConfirm(filterOptions)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BFFF)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        val selectedCount = selectedPromotions.size + selectedFeatures.size +
                            if (sliderPosition != 0f..120f) 1 else 0
                        Text(
                            text = "查看(已选$selectedCount)",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            }
        }
    }
}

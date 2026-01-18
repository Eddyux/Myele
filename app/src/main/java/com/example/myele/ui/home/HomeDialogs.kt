package com.example.myele.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeFilterDialog(
    onDismiss: () -> Unit,
    onConfirm: (HomeFilterOptions) -> Unit
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
                .fillMaxHeight(0.7f)
                .align(Alignment.BottomCenter)
                .clickable(enabled = false) { },
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部标题栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "筛选",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDismiss() }
                    )
                }

                HorizontalDivider()

                // 可滚动内容区域
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
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
                HomeFlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    listOf("首次光顾减", "满减优惠", "下单返红包", "配送费优惠", "特价商品", "0元起送").forEach { promotion ->
                        HomeFilterChip(
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
                HomeFlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    listOf("蜂鸟准时达", "到店自取", "品牌商家", "新店", "食无忧", "跨天预订", "线上开票", "慢必赔").forEach { feature ->
                        HomeFilterChip(
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
                HomePriceRangeSlider(
                    sliderPosition = sliderPosition,
                    onValueChange = { sliderPosition = it }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

                // 底部按钮 - 固定在底部
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
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
                                val filterOptions = HomeFilterOptions(
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

@Composable
fun HomeSortDialog(
    selectedSortType: HomeSortType,
    onSortTypeSelected: (HomeSortType) -> Unit,
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
                .padding(top = 280.dp)
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
                HomeSortDialogOption(
                    text = "人均价低到高",
                    isSelected = selectedSortType == HomeSortType.PRICE_LOW_TO_HIGH,
                    onClick = { onSortTypeSelected(HomeSortType.PRICE_LOW_TO_HIGH) }
                )
                HomeSortDialogOption(
                    text = "距离优先",
                    isSelected = selectedSortType == HomeSortType.DISTANCE,
                    onClick = { onSortTypeSelected(HomeSortType.DISTANCE) }
                )
                HomeSortDialogOption(
                    text = "商家好评优先",
                    isSelected = selectedSortType == HomeSortType.RATING,
                    onClick = { onSortTypeSelected(HomeSortType.RATING) }
                )
                HomeSortDialogOption(
                    text = "销量优先",
                    isSelected = selectedSortType == HomeSortType.SALES,
                    onClick = { onSortTypeSelected(HomeSortType.SALES) }
                )
            }
        }
    }
}

@Composable
fun RefreshLoadingDialog() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier.padding(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF00BFFF)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "换一换中...",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

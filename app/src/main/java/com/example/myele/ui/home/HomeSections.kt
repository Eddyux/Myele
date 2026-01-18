package com.example.myele.ui.home

import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import com.example.myele.model.Restaurant
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun FrequentlyOrderedPage(
    restaurants: List<Restaurant>,
    navController: NavController,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            item {
                // 常点页面标题
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFFF6B6B),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "常点商家",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "您经常点的商家会显示在这里",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 显示常点的商家（这里显示已收藏的商家）
            item {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val frequentRestaurants = restaurants.filter { it.isFavorite }
                    if (frequentRestaurants.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "暂无常点商家",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            frequentRestaurants.forEach { restaurant ->
                                RestaurantCard(restaurant, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

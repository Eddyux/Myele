package com.example.eleme_sim.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部个人信息
        item {
            UserInfoHeader(navController)
        }

        // 悦享俱乐部
        item {
            ClubBanner()
        }

        // 用户快捷功能卡片
        item {
            UserQuickActions(navController)
        }

        // 我的订单
        item {
            MyOrders(navController)
        }

        // 我的钱包
        item {
            MyWallet(navController)
        }

        // 我的关注和常点的店
        item {
            MyFavorites(navController)
        }

        // 更多功能
        item {
            MoreFunctions(navController)
        }

        // 你可能还喜欢
        item {
            RecommendedFood(navController)
        }
    }
}

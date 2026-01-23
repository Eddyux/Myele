package com.example.eleme_sim.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eleme_sim.navigation.Screen

/**
 * 主屏幕，包含底部导航栏
 */
@Composable
fun MainScreen(navController: NavController, content: @Composable (PaddingValues) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // 根据当前路由更新选中的标签
    LaunchedEffect(currentRoute) {
        selectedTab = when (currentRoute) {
            Screen.Home.route -> 0
            Screen.Messages.route -> 1
            Screen.ShoppingCart.route -> 2
            Screen.Profile.route -> 3
            else -> 0
        }
    }

    // 定义需要显示底部导航栏的路由
    val routesWithBottomBar = setOf(
        Screen.Home.route,
        Screen.Messages.route,
        Screen.ShoppingCart.route,
        Screen.Profile.route
    )

    // 判断当前路由是否需要显示底部导航栏
    val showBottomBar = currentRoute in routesWithBottomBar

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { index ->
                        selectedTab = index
                        when (index) {
                            0 -> navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                            1 -> navController.navigate(Screen.Messages.route) {
                                popUpTo(Screen.Home.route)
                            }
                            2 -> navController.navigate(Screen.ShoppingCart.route) {
                                popUpTo(Screen.Home.route)
                            }
                            3 -> navController.navigate(Screen.Profile.route) {
                                popUpTo(Screen.Home.route)
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "首页",
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) }
            )

            BottomNavItem(
                icon = Icons.Default.Email,
                label = "消息",
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                badge = 3
            )

            BottomNavItem(
                icon = Icons.Default.ShoppingCart,
                label = "购物车",
                selected = selectedTab == 2,
                onClick = { onTabSelected(2) }
            )

            BottomNavItem(
                icon = Icons.Default.Person,
                label = "我的",
                selected = selectedTab == 3,
                onClick = { onTabSelected(3) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    badge: Int? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .padding(4.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) Color(0xFF00BFFF) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            if (badge != null && badge > 0) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (badge > 99) "99+" else badge.toString(),
                        color = Color.White,
                        fontSize = 8.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (selected) Color(0xFF00BFFF) else Color.Gray
        )
    }
}

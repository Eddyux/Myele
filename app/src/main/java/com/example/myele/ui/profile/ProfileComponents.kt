package com.example.myele.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val route: String? = null
)

@Composable
fun QuickActionItem(title: String, icon: ImageVector, color: Color, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun OrderTypeItem(title: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF00BFFF),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun WalletItem(title: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFFFF6B00),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Black
        )
    }
}

@Composable
fun RowScope.FavoriteItem(title: String, subtitle: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun FunctionItem(item: ProfileMenuItem, navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                item.route?.let { navController.navigate(it) }
            }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = Color(0xFF00BFFF),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.title,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

data class RestaurantInfo(
    val restaurantId: String,
    val name: String,
    val description: String,
    val deliveryTime: String,
    val distance: String,
    val startPrice: String,
    val imageName: String
)

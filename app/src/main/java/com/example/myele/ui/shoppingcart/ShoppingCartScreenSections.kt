package com.example.myele.ui.shoppingcart

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuickAccess() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickAccessButton("我常买", Icons.Default.Favorite)
            QuickAccessButton("全能超市", Icons.Default.ShoppingBag)
        }
    }
}

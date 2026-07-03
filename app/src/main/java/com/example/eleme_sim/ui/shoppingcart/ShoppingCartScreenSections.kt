package com.example.eleme_sim.ui.shoppingcart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuickAccess() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickAccessButton(
            text = "我常买",
            iconTint = Color(0xFF24C5F6),
            modifier = Modifier.weight(1f)
        )
        QuickAccessButton(
            text = "全能超市",
            iconTint = Color(0xFF3DB4FF),
            modifier = Modifier.weight(1f)
        )
    }
}

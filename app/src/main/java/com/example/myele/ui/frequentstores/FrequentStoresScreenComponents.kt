package com.example.myele.ui.frequentstores

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    hasDropdown: Boolean,
    isHighlight: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isHighlight -> Color(0xFFE3F2FD)
        selected -> Color(0xFFF5F5F5)
        else -> Color.White
    }

    val textColor = when {
        isHighlight -> Color(0xFF2196F3)
        selected -> Color.Black
        else -> Color.Black
    }

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        border = if (!isHighlight) BorderStroke(1.dp, Color(0xFFE0E0E0)) else null,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (selected || isHighlight) FontWeight.Medium else FontWeight.Normal
        )

        if (hasDropdown) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

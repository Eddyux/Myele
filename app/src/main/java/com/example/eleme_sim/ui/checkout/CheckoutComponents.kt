package com.example.eleme_sim.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(onBackClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF7FBFF), Color(0xFFF3F5F7))
                )
            )
            .padding(start = 8.dp, end = 14.dp, top = 10.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回",
                        tint = Color(0xFF101828)
                    )
                }
            }
            Text(
                text = "确认订单",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF101828),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun FeeItem(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 15.sp, color = Color(0xFF667085))
        Text(value, fontSize = 15.sp, color = valueColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun OptionItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 15.sp, color = Color(0xFF101828), fontWeight = FontWeight.SemiBold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, fontSize = 14.sp, color = Color(0xFF98A2B3))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFC0C5CC),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun BottomSubmitBar(total: Double, onSubmit: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 14.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("合计:", fontSize = 12.sp, color = Color(0xFF98A2B3))
                Text(
                    text = "¥%.1f".format(total),
                    fontSize = 26.sp,
                    color = Color(0xFF101828),
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16B8F3)),
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.height(52.dp)
            ) {
                Text("提交订单", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

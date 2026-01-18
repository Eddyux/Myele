package com.example.myele.ui.notificationsettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryHeader(title: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF5F5F5)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun NotificationRow(
    title: String,
    subtitle: String?,
    icon: ImageVector? = null,
    showArrow: Boolean = true,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun CloseNotificationDialog(
    onDismiss: () -> Unit,
    onClose: () -> Unit,
    onKeep: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "是否关闭通知",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = "关闭后将不再接收系统推送通知",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onClose,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("关闭", color = Color.Gray, fontSize = 16.sp)
                    }
                    Button(
                        onClick = onKeep,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("保留重要通知", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        },
        confirmButton = {},
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun SimpleMessageDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = message,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("确定", color = Color.White, fontSize = 16.sp)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

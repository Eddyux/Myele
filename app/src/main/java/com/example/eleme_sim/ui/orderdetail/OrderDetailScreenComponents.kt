package com.example.eleme_sim.ui.orderdetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ActionIconBg = Color(0xFFF4F6F8)
private val ActionIconTint = Color(0xFF2F3135)
private val LabelColor = Color(0xFFB0ABA3)
private val ValueColor = Color(0xFF272A2F)
private val CopyColor = Color(0xFF38B6FF)
private val DividerColor = Color(0xFFF1EEE8)
private val DiscountColor = Color(0xFFE87262)

@Composable
fun ActionButton(text: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(ActionIconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = ActionIconTint,
                modifier = Modifier.size(23.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = ValueColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun InfoRow(label: String, value: String, showArrow: Boolean = false, showCopy: Boolean = false) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = LabelColor,
                modifier = Modifier.width(82.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(82.dp))
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = ValueColor,
                textAlign = TextAlign.End,
                maxLines = if (label == "收货地址") 3 else 2,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (showCopy) {
                Spacer(modifier = Modifier.width(6.dp))
                TextButton(
                    onClick = { copyText(context, value) },
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("复制", fontSize = 12.sp, color = CopyColor)
                }
            }

            if (showArrow) {
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = LabelColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun PriceSummaryLine(discountAmount: Double, actualAmount: Double) {
    val actualText = formatCompactPrice(actualAmount)
    val text = if (discountAmount > 0) {
        buildAnnotatedString {
            append("已优惠")
            pushStyle(SpanStyle(color = DiscountColor))
            append(formatCompactPrice(discountAmount))
            pop()
            append("  小计 ")
            pushStyle(SpanStyle(color = ValueColor, fontWeight = FontWeight.Bold))
            append(actualText)
            pop()
        }
    } else {
        buildAnnotatedString {
            append("小计 ")
            pushStyle(SpanStyle(color = ValueColor, fontWeight = FontWeight.Bold))
            append(actualText)
            pop()
        }
    }

    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 15.sp,
        lineHeight = 22.sp,
        color = ValueColor,
        textAlign = TextAlign.End
    )
}

private fun copyText(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    clipboard?.setPrimaryClip(ClipData.newPlainText("order_detail", text))
}

private fun formatCompactPrice(price: Double): String {
    return if (price % 1.0 == 0.0) {
        "¥${price.toInt()}"
    } else {
        "¥${String.format(java.util.Locale.US, "%.2f", price).trimEnd('0').trimEnd('.')}"
    }
}

package com.example.eleme_sim.ui.shoppingcart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eleme_sim.ui.components.ProductImage

@Composable
fun TopBar(title: String, address: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF2F7FC), Color(0xFFF7FAFD))
                )
            )
            .padding(start = 18.dp, end = 18.dp, top = 20.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF101828)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = address,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                color = Color(0xFF2B3340),
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "编辑地址",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF1D2939)
            )
        }
    }
}

@Composable
fun QuickAccessButton(
    text: String,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconTint.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF182230)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF98A2B3)
            )
        }
    }
}

@Composable
fun SuggestedSectionHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 14.dp),
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
            bottomStart = 18.dp,
            bottomEnd = 18.dp
        ),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFE9F9FF), Color(0xFFF8FCFF))
                    )
                )
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "购买清单及更多",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF15B8F8)
                )
            }
        }
    }
}

@Composable
fun RestaurantCartCard(
    restaurant: RestaurantCart,
    modifier: Modifier = Modifier,
    onRestaurantSelected: (Boolean) -> Unit,
    onItemSelected: (Int, Boolean) -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectionCircle(
                    selected = restaurant.isSelected,
                    enabled = true,
                    onClick = { onRestaurantSelected(!restaurant.isSelected) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = restaurant.restaurantName,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF182230)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFFB1B8C0)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "删除失效商品",
                    tint = Color(0xFFC0C5CC),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))

            restaurant.items.forEachIndexed { index, item ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF0F2F5), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                CartItemRow(
                    item = item,
                    onSelectedChange = { onItemSelected(index, it) }
                )
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onSelectedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectionCircle(
            selected = item.isSelected,
            enabled = true,
            onClick = { onSelectedChange(!item.isSelected) }
        )
        Spacer(modifier = Modifier.width(12.dp))
        ProductImage(
            productId = item.productId,
            productName = item.productName,
            restaurantName = item.restaurantName,
            modifier = Modifier.size(96.dp),
            size = 96.dp,
            cornerRadius = 16.dp
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = item.productName,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF101828)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = formatPrice(item.price),
                    modifier = Modifier.weight(1f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF101828)
                )

                QuantityPill(quantity = item.quantity)
            }
        }
    }
}

@Composable
fun UnavailableItems(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "37件无法购买商品",
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF344054)
                )
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "全部删除",
                    tint = Color(0xFF98A2B3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "全部删除",
                    fontSize = 15.sp,
                    color = Color(0xFF667085)
                )
            }
        }
    }
}

@Composable
fun BottomCheckoutBar(
    selectAll: Boolean,
    onSelectAllChanged: (Boolean) -> Unit,
    totalPrice: Double,
    onCheckoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectionCircle(
                selected = selectAll,
                enabled = true,
                onClick = { onSelectAllChanged(!selectAll) }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "全选",
                fontSize = 16.sp,
                color = Color(0xFF101828),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(18.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "合计",
                    fontSize = 13.sp,
                    color = Color(0xFF98A2B3)
                )
                Text(
                    text = formatPrice(totalPrice),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (totalPrice > 0) Color(0xFF101828) else Color(0xFFB8C0CC)
                )
            }
            Button(
                onClick = onCheckoutClick,
                enabled = totalPrice > 0,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF12B7F5),
                    disabledContainerColor = Color(0xFFA9DFF4)
                )
            ) {
                Text(
                    text = "一键结算",
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun QuantityPill(quantity: Int) {
    Box(
        modifier = Modifier
            .border(1.dp, Color(0xFFD8DEE6), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "x$quantity",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101828)
        )
    }
}

@Composable
private fun SelectionCircle(
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        !enabled -> Color(0xFFD9DEE5)
        selected -> Color(0xFF14B8F5)
        else -> Color(0xFFD0D5DD)
    }
    val backgroundColor = if (selected) Color(0xFF14B8F5) else Color.White

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.5.dp, borderColor, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

private fun formatPrice(price: Double): String {
    return if (price % 1.0 == 0.0) {
        "¥${price.toInt()}"
    } else {
        "¥%.2f".format(price)
    }
}

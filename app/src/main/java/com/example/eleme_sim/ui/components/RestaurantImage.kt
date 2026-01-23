package com.example.eleme_sim.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.eleme_sim.utils.ImageUtils

/**
 * 餐厅图片组件
 */
@Composable
fun RestaurantImage(
    restaurantName: String,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    cornerRadius: Dp = 8.dp
) {
    val imageRes = ImageUtils.getRestaurantImage(restaurantName)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (imageRes != 0) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = restaurantName,
                modifier = Modifier.size(size),
                contentScale = ContentScale.Crop
            )
        } else {
            // 如果没有图片，显示默认图标
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = restaurantName,
                tint = Color.White,
                modifier = Modifier.size(size / 2.5f)
            )
        }
    }
}

/**
 * 用户头像组件
 */
@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 60.dp
) {
    val imageRes = ImageUtils.getUserAvatar()

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (imageRes != 0) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "用户头像",
                modifier = Modifier
                    .size(size)
                    .clip(androidx.compose.foundation.shape.CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * 骑手头像组件
 */
@Composable
fun RiderAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    val imageRes = ImageUtils.getRiderAvatar()

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (imageRes != 0) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "骑手头像",
                modifier = Modifier
                    .size(size)
                    .clip(androidx.compose.foundation.shape.CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            // 如果没有图片，显示默认图标
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "骑手头像",
                tint = Color.White,
                modifier = Modifier.size(size / 2f)
            )
        }
    }
}

/**
 * 爆品团图片组件
 */
@Composable
fun HotDealImage(
    index: Int = 0,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    cornerRadius: Dp = 8.dp
) {
    val imageRes = ImageUtils.getHotDealImage(index)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (imageRes != 0) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "爆品团",
                modifier = Modifier.size(size),
                contentScale = ContentScale.Crop
            )
        } else {
            // 如果没有图片，显示默认图标
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "爆品团",
                tint = Color.White,
                modifier = Modifier.size(size / 2.5f)
            )
        }
    }
}

/**
 * 商品图片组件
 */
@Composable
fun ProductImage(
    productId: String,
    productName: String,
    restaurantName: String = "",
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    cornerRadius: Dp = 8.dp
) {
    val imageRes = ImageUtils.getProductImage(productId)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (imageRes != 0) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = productName,
                modifier = Modifier.size(size),
                contentScale = ContentScale.Crop
            )
        } else {
            // 如果没有图片，显示默认图标
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = productName,
                tint = Color.White,
                modifier = Modifier.size(size / 2.5f)
            )
        }
    }
}

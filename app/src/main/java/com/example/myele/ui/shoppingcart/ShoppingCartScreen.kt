package com.example.myele.ui.shoppingcart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.myele.data.DataRepository
import com.example.myele.data.CartManager
import com.example.myele.ui.components.ProductImage

data class CartItem(
    val productId: String,
    val restaurantName: String,
    val productName: String,
    val price: Double,
    val quantity: Int,
    var isSelected: Boolean = false
)

data class RestaurantCart(
    val restaurantName: String,
    val items: List<CartItem>,
    var isSelected: Boolean = false
)

@Composable
fun ShoppingCartScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { DataRepository(context) }

    // Load products and create shopping cart items
    var restaurantCarts by remember {
        mutableStateOf<List<RestaurantCart>>(emptyList())
    }

    LaunchedEffect(Unit) {
        val allProducts = repository.loadProducts()
        // Initialize CartManager with all products
        CartManager.setProducts(allProducts)

        // Create sample shopping cart with products from different restaurants
        // 从川香麻辣烫选1个商品
        val product1 = allProducts.find { it.productId == "prod_001" } // 麻辣烫
        // 从老北京炸酱面选2个商品
        val product2 = allProducts.find { it.productId == "prod_002" } // 炸酱面
        val product3 = allProducts.find { it.productId == "prod_022" } // 京酱肉丝
        // 从湘味轩选1个商品
        val product4 = allProducts.find { it.productId == "prod_026" } // 口味虾

        val carts = mutableListOf<RestaurantCart>()

        // 川香麻辣烫
        if (product1 != null) {
            carts.add(
                RestaurantCart(
                    product1.restaurantName,
                    listOf(
                        CartItem(
                            product1.productId,
                            product1.restaurantName,
                            product1.name,
                            product1.price,
                            1,
                            false
                        )
                    ),
                    false
                )
            )
        }

        // 老北京炸酱面
        val laobeijingItems = mutableListOf<CartItem>()
        if (product2 != null) {
            laobeijingItems.add(
                CartItem(
                    product2.productId,
                    product2.restaurantName,
                    product2.name,
                    product2.price,
                    1,
                    false
                )
            )
        }
        if (product3 != null) {
            laobeijingItems.add(
                CartItem(
                    product3.productId,
                    product3.restaurantName,
                    product3.name,
                    product3.price,
                    2,
                    false
                )
            )
        }
        if (laobeijingItems.isNotEmpty()) {
            carts.add(
                RestaurantCart(
                    laobeijingItems.first().restaurantName,
                    laobeijingItems,
                    false
                )
            )
        }

        // 湘味轩
        if (product4 != null) {
            carts.add(
                RestaurantCart(
                    product4.restaurantName,
                    listOf(
                        CartItem(
                            product4.productId,
                            product4.restaurantName,
                            product4.name,
                            product4.price,
                            1,
                            false
                        )
                    ),
                    false
                )
            )
        }

        restaurantCarts = carts
    }

    var selectAll by remember { mutableStateOf(false) }

    val totalPrice = restaurantCarts.flatMap { it.items }
        .filter { it.isSelected }
        .sumOf { it.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标题栏
        TopBar()

        // 常买和全能超市
        QuickAccess()

        // 购物车内容
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            item {
                Text(
                    text = "想点就马上下单吧",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp, 12.dp)
                )
            }

            items(restaurantCarts.size) { index ->
                val restaurant = restaurantCarts[index]
                RestaurantCartCard(
                    restaurant = restaurant,
                    onRestaurantSelected = { selected ->
                        val updated = restaurantCarts.toMutableList()
                        updated[index] = restaurant.copy(
                            isSelected = selected,
                            items = restaurant.items.map { it.copy(isSelected = selected) }
                        )
                        restaurantCarts = updated
                    },
                    onItemSelected = { itemIndex, selected ->
                        val updated = restaurantCarts.toMutableList()
                        val updatedItems = restaurant.items.toMutableList()
                        updatedItems[itemIndex] = updatedItems[itemIndex].copy(isSelected = selected)
                        updated[index] = restaurant.copy(
                            items = updatedItems,
                            isSelected = updatedItems.all { it.isSelected }
                        )
                        restaurantCarts = updated
                    }
                )
            }

            item {
                UnavailableItems()
            }
        }

        // 底部结算栏
        BottomCheckoutBar(
            selectAll = selectAll,
            onSelectAllChanged = { selected ->
                selectAll = selected
                restaurantCarts = restaurantCarts.map { restaurant ->
                    restaurant.copy(
                        isSelected = selected,
                        items = restaurant.items.map { it.copy(isSelected = selected) }
                    )
                }
            },
            totalPrice = totalPrice,
            onCheckoutClick = {
                // Collect selected items and pass to CartManager
                val selectedItems = mutableMapOf<String, Int>()
                restaurantCarts.forEach { restaurant ->
                    restaurant.items.forEach { item ->
                        if (item.isSelected) {
                            selectedItems[item.productId] = item.quantity
                        }
                    }
                }
                CartManager.setCheckoutItems(selectedItems, selectAll)
                navController.navigate(com.example.myele.navigation.Screen.Checkout.route)
            }
        )
    }
}

@Composable
fun TopBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "购物车",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "华中师范大学宝山学...",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = { /* 编辑 */ }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "编辑",
                    tint = Color.Gray
                )
            }
        }
    }
}

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

@Composable
fun QuickAccessButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        modifier = Modifier
            .clickable { /* TODO */ },
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF00BFFF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun RestaurantCartCard(
    restaurant: RestaurantCart,
    onRestaurantSelected: (Boolean) -> Unit,
    onItemSelected: (Int, Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // 店铺标题行（带复选框）
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = restaurant.isSelected,
                    onCheckedChange = onRestaurantSelected
                )
                Text(
                    text = restaurant.restaurantName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            // 商品列表
            restaurant.items.forEachIndexed { index, item ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 商品复选框
                    Checkbox(
                        checked = item.isSelected,
                        onCheckedChange = { onItemSelected(index, it) }
                    )

                    // 商品图片
                    ProductImage(
                        productId = item.productId,
                        productName = item.productName,
                        modifier = Modifier.size(60.dp),
                        size = 60.dp,
                        cornerRadius = 8.dp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.productName,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "¥${item.price}",
                            fontSize = 16.sp,
                            color = Color(0xFFFF3366),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 数量控制
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "减少",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "${item.quantity}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "增加",
                                tint = Color(0xFF00BFFF)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnavailableItems() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF8E1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFFF6B00),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "37件无法购买商品",
                fontSize = 14.sp,
                color = Color(0xFFFF6B00)
            )
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
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectAll,
                    onCheckedChange = onSelectAllChanged
                )
                Text(
                    text = "全选",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "合计:",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "¥${"%.2f".format(totalPrice)}",
                    fontSize = 18.sp,
                    color = Color(0xFFFF3366),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onCheckoutClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3366))
            ) {
                Text("一键结算")
            }
        }
    }
}

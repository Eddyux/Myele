package com.example.myele_sim.ui.shoppingcart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele_sim.data.DataRepository
import com.example.myele_sim.data.CartManager

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
                navController.navigate(com.example.myele_sim.navigation.Screen.Checkout.route)
            }
        )
    }
}

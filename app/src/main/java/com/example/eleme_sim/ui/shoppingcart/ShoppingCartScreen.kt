package com.example.eleme_sim.ui.shoppingcart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eleme_sim.data.CartManager
import com.example.eleme_sim.data.DataRepository
import com.example.eleme_sim.navigation.Screen

data class CartItem(
    val productId: String,
    val restaurantName: String,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val isSelected: Boolean = false
)

data class RestaurantCart(
    val restaurantName: String,
    val items: List<CartItem>,
    val isSelected: Boolean = false
)

@Composable
fun ShoppingCartScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember { DataRepository(context) }

    var restaurantCarts by remember { mutableStateOf<List<RestaurantCart>>(emptyList()) }

    LaunchedEffect(Unit) {
        val allProducts = repository.loadProducts()
        CartManager.setProducts(allProducts)

        val product1 = allProducts.find { it.productId == "prod_001" }
        val product2 = allProducts.find { it.productId == "prod_002" }
        val product3 = allProducts.find { it.productId == "prod_022" }
        val product4 = allProducts.find { it.productId == "prod_026" }

        restaurantCarts = buildList {
            product1?.let { product ->
                add(
                    RestaurantCart(
                        restaurantName = product.restaurantName,
                        items = listOf(
                            CartItem(
                                productId = product.productId,
                                restaurantName = product.restaurantName,
                                productName = product.name,
                                price = product.price,
                                quantity = 1
                            )
                        )
                    )
                )
            }

            if (product2 != null || product3 != null) {
                val items = buildList {
                    product2?.let { product ->
                        add(
                            CartItem(
                                productId = product.productId,
                                restaurantName = product.restaurantName,
                                productName = product.name,
                                price = product.price,
                                quantity = 1
                            )
                        )
                    }
                    product3?.let { product ->
                        add(
                            CartItem(
                                productId = product.productId,
                                restaurantName = product.restaurantName,
                                productName = product.name,
                                price = product.price,
                                quantity = 2
                            )
                        )
                    }
                }

                if (items.isNotEmpty()) {
                    add(
                        RestaurantCart(
                            restaurantName = items.first().restaurantName,
                            items = items
                        )
                    )
                }
            }

            product4?.let { product ->
                add(
                    RestaurantCart(
                        restaurantName = product.restaurantName,
                        items = listOf(
                            CartItem(
                                productId = product.productId,
                                restaurantName = product.restaurantName,
                                productName = product.name,
                                price = product.price,
                                quantity = 1
                            )
                        )
                    )
                )
            }
        }
    }

    var selectAll by remember { mutableStateOf(false) }
    val totalPrice by remember(restaurantCarts) {
        derivedStateOf {
            restaurantCarts
                .flatMap { it.items }
                .filter { it.isSelected }
                .sumOf { it.price * it.quantity }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
    ) {
        TopBar(
            title = "购物车",
            address = "华中师范大学元宝山学..."
        )

        QuickAccess()

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 18.dp
            )
        ) {
            item {
                SuggestedSectionHeader()
            }

            itemsIndexed(restaurantCarts) { index, restaurant ->
                RestaurantCartCard(
                    restaurant = restaurant,
                    modifier = Modifier.padding(bottom = 14.dp),
                    onRestaurantSelected = { selected ->
                        restaurantCarts = restaurantCarts.toMutableList().also { carts ->
                            carts[index] = restaurant.copy(
                                isSelected = selected,
                                items = restaurant.items.map { it.copy(isSelected = selected) }
                            )
                        }
                    },
                    onItemSelected = { itemIndex, selected ->
                        restaurantCarts = restaurantCarts.toMutableList().also { carts ->
                            val updatedItems = restaurant.items.toMutableList().also { items ->
                                items[itemIndex] = items[itemIndex].copy(isSelected = selected)
                            }
                            carts[index] = restaurant.copy(
                                items = updatedItems,
                                isSelected = updatedItems.all { it.isSelected }
                            )
                        }
                    }
                )
            }

            item {
                UnavailableItems(
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

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
                val selectedItems = buildMap<String, Int> {
                    restaurantCarts.forEach { restaurant ->
                        restaurant.items.forEach { item ->
                            if (item.isSelected) {
                                put(item.productId, item.quantity)
                            }
                        }
                    }
                }

                if (selectedItems.isNotEmpty()) {
                    CartManager.setCheckoutItems(selectedItems, selectAll)
                    navController.navigate(Screen.Checkout.route)
                }
            }
        )
    }
}

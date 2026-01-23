package com.example.eleme_sim.ui.storepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eleme_sim.data.DataRepository
import com.example.eleme_sim.data.CartManager
import com.example.eleme_sim.model.Product
import com.example.eleme_sim.model.Restaurant
import com.example.eleme_sim.model.ProductCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePageScreen(navController: NavController, restaurantId: String, searchKey: String? = null) {
    val context = LocalContext.current
    val repository = remember { DataRepository(context) }

    var restaurant by remember { mutableStateOf<Restaurant?>(null) }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }
    var isFollowed by remember { mutableStateOf(false) }

    // Cart state - store product ID to quantity map
    var cartItems by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    // More menu state
    var showMoreMenu by remember { mutableStateOf(false) }
    var showShareMenu by remember { mutableStateOf(false) }
    var showShareSuccessDialog by remember { mutableStateOf(false) }
    var sharedPlatform by remember { mutableStateOf("") }

    LaunchedEffect(restaurantId) {
        val restaurants = repository.loadRestaurants()
        restaurant = restaurants.find { it.restaurantId == restaurantId }
        isFollowed = restaurant?.isFollowed ?: false

        // Load products for this restaurant
        val allProducts = repository.loadProducts()
        products = allProducts.filter { it.restaurantId == restaurantId }

        // Initialize CartManager with all products
        CartManager.setProducts(allProducts)

        // 只在从搜索页面进入时保存searchKey
        CartManager.setSearchKeyword(searchKey)
    }

    val tabs = listOf("点餐", "评价", "商家", "好友拼单")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // Top bar
            StorePageTopBar(
                navController = navController,
                isFollowed = isFollowed,
                onFollowClick = { isFollowed = !isFollowed },
                onMoreClick = { showMoreMenu = true }
            )

            restaurant?.let { store ->
                // Store header
                StoreHeader(store)

                // Tab bar
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }

                // Content based on selected tab
                when (selectedTab) {
                    0 -> {
                        // Menu tab
                        Row(modifier = Modifier.fillMaxSize()) {
                            // Left side category list
                            CategoryList(
                                products = products,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { selectedCategory = it },
                                modifier = Modifier.width(100.dp)
                            )

                            // Right side product list
                            ProductList(
                                products = products.filter {
                                    selectedCategory == null || it.category == selectedCategory
                                },
                                cartItems = cartItems,
                                onAddToCart = { productId ->
                                    cartItems = cartItems.toMutableMap().apply {
                                        this[productId] = (this[productId] ?: 0) + 1
                                    }
                                },
                                onRemoveFromCart = { productId ->
                                    cartItems = cartItems.toMutableMap().apply {
                                        val currentQty = this[productId] ?: 0
                                        if (currentQty > 1) {
                                            this[productId] = currentQty - 1
                                        } else {
                                            remove(productId)
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    1 -> PlaceholderContent("评价")
                    2 -> PlaceholderContent("商家")
                    3 -> PlaceholderContent("好友拼单")
                }
            }
        }

        // Bottom checkout bar
        if (cartItems.isNotEmpty()) {
            val totalQuantity = cartItems.values.sum()
            val totalPrice = cartItems.entries.sumOf { (productId, qty) ->
                val product = products.find { it.productId == productId }
                (product?.price ?: 0.0) * qty
            }

            BottomCheckoutBar(
                totalQuantity = totalQuantity,
                totalPrice = totalPrice,
                onCheckout = {
                    // Set cart items to CartManager
                    CartManager.setCheckoutItems(cartItems)
                    // Navigate to checkout
                    navController.navigate(com.example.eleme_sim.navigation.Screen.Checkout.route)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // 更多菜单弹窗
        MoreMenuDialog(
            showMoreMenu = showMoreMenu,
            onDismissRequest = { showMoreMenu = false },
            onShareClick = { showShareMenu = true },
            navController = navController
        )

        // 分享平台选择菜单
        ShareMenuDialog(
            showShareMenu = showShareMenu,
            onDismissRequest = { showShareMenu = false },
            restaurantId = restaurantId,
            restaurant = restaurant,
            onPlatformSelected = { platform ->
                sharedPlatform = platform
                showShareSuccessDialog = true
            }
        )

        // 分享成功弹窗
        ShareSuccessDialog(
            showDialog = showShareSuccessDialog,
            sharedPlatform = sharedPlatform,
            onDismissRequest = { showShareSuccessDialog = false }
        )
    }
}

package com.example.myele.ui.storepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.data.CartManager
import com.example.myele.model.Product
import com.example.myele.model.Restaurant
import com.example.myele.model.ProductCategory
import com.example.myele.ui.components.RestaurantImage
import com.example.myele.ui.components.ProductImage
import com.example.myele.navigation.Screen

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
                    navController.navigate(com.example.myele.navigation.Screen.Checkout.route)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // 更多菜单弹窗
        if (showMoreMenu) {
            ModalBottomSheet(
                onDismissRequest = { showMoreMenu = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    // 分享按钮
                    TextButton(
                        onClick = {
                            showMoreMenu = false
                            showShareMenu = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("分享", fontSize = 16.sp)
                        }
                    }
                    Divider()

                    // 举报商家按钮
                    TextButton(
                        onClick = {
                            showMoreMenu = false
                            navController.navigate(Screen.Undeveloped.createRoute("举报商家"))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Report, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("举报商家", fontSize = 16.sp)
                        }
                    }
                    Divider()

                    // 智能客服按钮
                    TextButton(
                        onClick = {
                            showMoreMenu = false
                            navController.navigate(Screen.MyKefu.route)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Support, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("智能客服", fontSize = 16.sp)
                        }
                    }
                    Divider()

                    // 取消按钮
                    TextButton(
                        onClick = { showMoreMenu = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("取消", fontSize = 16.sp, color = Color.Gray)
                    }
                }
            }
        }

        // 分享平台选择菜单
        if (showShareMenu) {
            ModalBottomSheet(
                onDismissRequest = { showShareMenu = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "分享到",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // 分享平台网格
                    val platforms = listOf(
                        "微信" to Icons.Default.Chat,
                        "朋友圈" to Icons.Default.Group,
                        "微博" to Icons.Default.Public,
                        "QQ" to Icons.Default.Message,
                        "QQ空间" to Icons.Default.Photo,
                        "钉钉" to Icons.Default.Work
                    )

                    platforms.chunked(4).forEach { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { (platform, icon) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            // 记录分享操作
                                            com.example.myele.utils.ActionLogger.logAction(
                                                context = context,
                                                action = "share_store",
                                                page = "store_page",
                                                pageInfo = mapOf(
                                                    "restaurant_name" to (restaurant?.name ?: ""),
                                                    "restaurant_id" to restaurantId
                                                ),
                                                extraData = mapOf(
                                                    "platform" to platform
                                                )
                                            )

                                            sharedPlatform = platform
                                            showShareMenu = false
                                            showShareSuccessDialog = true
                                        }
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = platform,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .padding(8.dp),
                                        tint = Color(0xFF00BFFF)
                                    )
                                    Text(
                                        text = platform,
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 取消按钮
                    TextButton(
                        onClick = { showShareMenu = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("取消", fontSize = 16.sp, color = Color.Gray)
                    }
                }
            }
        }

        // 分享成功弹窗
        if (showShareSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showShareSuccessDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = {
                    Text(
                        text = "分享成功",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "已分享到$sharedPlatform",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { showShareSuccessDialog = false }
                    ) {
                        Text("确定", fontSize = 16.sp, color = Color(0xFF00BFFF))
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorePageTopBar(
    navController: NavController,
    isFollowed: Boolean,
    onFollowClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回")
            }
        },
        actions = {
            IconButton(onClick = { /* Search */ }) {
                Icon(Icons.Default.Search, contentDescription = "搜索")
            }
            IconButton(onClick = { /* Chat */ }) {
                Icon(Icons.Default.Chat, contentDescription = "聊天")
            }
            IconButton(onClick = onFollowClick) {
                Icon(
                    imageVector = if (isFollowed) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "收藏",
                    tint = if (isFollowed) Color.Red else Color.Gray
                )
            }
            IconButton(onClick = onMoreClick) {
                Icon(Icons.Default.MoreVert, contentDescription = "更多")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun StoreHeader(restaurant: Restaurant) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RestaurantImage(
                    restaurantName = restaurant.name,
                    size = 60.dp,
                    cornerRadius = 8.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = restaurant.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${restaurant.rating}",
                            fontSize = 14.sp,
                            color = Color(0xFFFFB800),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "月售${restaurant.salesVolume}+",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${restaurant.deliveryTime}分钟 | ${restaurant.distance}km",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Delivery options
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
                ) {
                    Text("外送")
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("自取")
                }
            }

            // Coupons
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF8E1), RoundedCornerShape(4.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFFFF6B00),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "满减优惠 | 新用户立减",
                        fontSize = 12.sp,
                        color = Color(0xFFFF6B00)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryList(
    products: List<Product>,
    selectedCategory: ProductCategory?,
    onCategorySelected: (ProductCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(null) + products.mapNotNull { it.category }.distinct()

    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: ProductCategory?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val categoryName = when (category) {
        null -> "全部"
        ProductCategory.HOT_SALE -> "热销"
        ProductCategory.SIGNATURE -> "招牌"
        ProductCategory.NEW_ARRIVAL -> "新品"
        ProductCategory.SPECIAL -> "优惠"
        ProductCategory.COMBO -> "套餐"
        ProductCategory.BEVERAGE -> "饮品"
        ProductCategory.MAIN_DISH -> "主食"
        ProductCategory.SIDE_DISH -> "小菜"
        ProductCategory.DESSERT -> "甜品"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) Color(0xFFF5F5F5) else Color.White)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = categoryName,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF00BFFF) else Color.Black
        )
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    cartItems: Map<String, Int>,
    onAddToCart: (String) -> Unit,
    onRemoveFromCart: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(bottom = 80.dp) // Space for bottom bar
    ) {
        // Signature dishes section
        val signatureProducts = products.filter { it.category == ProductCategory.SIGNATURE }
        if (signatureProducts.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "本店招牌拿手菜",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(signatureProducts.take(3)) { product ->
                            SignatureProductCard(
                                product = product,
                                quantity = cartItems[product.productId] ?: 0,
                                onAdd = { onAddToCart(product.productId) }
                            )
                        }
                    }
                }
            }
        }

        // Regular products
        items(products) { product ->
            ProductItem(
                product = product,
                quantity = cartItems[product.productId] ?: 0,
                onAdd = { onAddToCart(product.productId) },
                onRemove = { onRemoveFromCart(product.productId) }
            )
        }
    }
}

@Composable
fun SignatureProductCard(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(120.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF8E1)
    ) {
        Column {
            ProductImage(
                productId = product.productId,
                productName = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                size = 100.dp,
                cornerRadius = 8.dp
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "¥${product.price}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF3366)
                        )
                        product.originalPrice?.let { originalPrice ->
                            Text(
                                text = "¥$originalPrice",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }
                    IconButton(
                        onClick = onAdd,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "添加",
                            tint = Color(0xFF00BFFF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        ProductImage(
            productId = product.productId,
            productName = product.name,
            modifier = Modifier.size(80.dp),
            size = 80.dp,
            cornerRadius = 8.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (product.monthSales > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "月售${product.monthSales}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "¥${product.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF3366)
                    )
                    product.originalPrice?.let { originalPrice ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "¥$originalPrice",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // Quantity controls
                if (quantity > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onRemove,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "减少",
                                tint = Color(0xFF00BFFF)
                            )
                        }
                        Text(
                            text = "$quantity",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = onAdd,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "添加",
                                tint = Color(0xFF00BFFF)
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = onAdd,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "添加",
                            tint = Color(0xFF00BFFF)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomCheckoutBar(
    totalQuantity: Int,
    totalPrice: Double,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        color = Color(0xFF2C2C2C),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF00BFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color.White
                    )
                    if (totalQuantity > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.TopEnd)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Red),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$totalQuantity",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "¥%.2f".format(totalPrice),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "配送费另计",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            Button(
                onClick = onCheckout,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                modifier = Modifier.height(40.dp)
            ) {
                Text("去结算", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PlaceholderContent(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title 内容待开发",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

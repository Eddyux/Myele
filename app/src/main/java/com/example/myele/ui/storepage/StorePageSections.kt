package com.example.myele.ui.storepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myele.model.Product
import com.example.myele.model.ProductCategory

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
                SignatureProductsSection(
                    products = signatureProducts,
                    cartItems = cartItems,
                    onAddToCart = onAddToCart
                )
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
fun SignatureProductsSection(
    products: List<Product>,
    cartItems: Map<String, Int>,
    onAddToCart: (String) -> Unit
) {
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
            items(products.take(3)) { product ->
                SignatureProductCard(
                    product = product,
                    quantity = cartItems[product.productId] ?: 0,
                    onAdd = { onAddToCart(product.productId) }
                )
            }
        }
    }
}

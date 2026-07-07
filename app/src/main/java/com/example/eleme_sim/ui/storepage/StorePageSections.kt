package com.example.eleme_sim.ui.storepage

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
import com.example.eleme_sim.model.Product
import com.example.eleme_sim.model.ProductCategory

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
            .background(Color(0xFFF1EFEB))
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
            .background(Color(0xFFFFFCF6))
            .padding(bottom = 90.dp) // Space for bottom bar
    ) {
        // Signature dishes section
        val signatureProducts = products.filter { it.category == ProductCategory.SIGNATURE }
        if (signatureProducts.isNotEmpty()) {
            item {
                SignatureProductsSection(
                    products = signatureProducts,
                    cartItems = cartItems,
                    onAddToCart = onAddToCart,
                    onRemoveFromCart = onRemoveFromCart
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
    onAddToCart: (String) -> Unit,
    onRemoveFromCart: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 6.dp)
            .background(
                Color(0xFFA85C1B),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = "本店招牌拿手菜",
            fontSize = 17.sp,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products.take(3)) { product ->
                SignatureProductCard(
                    product = product,
                    quantity = cartItems[product.productId] ?: 0,
                    onAdd = { onAddToCart(product.productId) },
                    onRemove = { onRemoveFromCart(product.productId) }
                )
            }
        }
    }
}

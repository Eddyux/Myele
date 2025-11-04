package com.example.myele.ui.search

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.myele.ui.components.RestaurantImage
import com.example.myele.data.ActionLogger

@Composable
fun SearchScreen(navController: NavController) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }
    var recommendedStores by remember { mutableStateOf<List<StoreRecommendation>>(emptyList()) }

    val presenter = remember {
        SearchPresenter(object : SearchContract.View {
            override fun updateSearchHistory(history: List<String>) {
                searchHistory = history
            }

            override fun updateSearchSuggestions(suggestions: List<String>) {
                searchSuggestions = suggestions
            }

            override fun updateRecommendedStores(stores: List<StoreRecommendation>) {
                recommendedStores = stores
            }
        })
    }

    LaunchedEffect(Unit) {
        presenter.onViewCreated()
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部搜索栏
        TopSearchBar(
            searchText = searchText,
            onSearchTextChanged = {
                searchText = it
                presenter.onSearchTextChanged(it)
            },
            onSearchClicked = {
                if (searchText.isNotBlank()) {
                    presenter.onSearchClicked(searchText)

                    // 记录搜索操作
                    ActionLogger.logAction(
                        context = context,
                        action = "perform_search",
                        page = "home",
                        extraData = mapOf(
                            "search_query" to searchText,
                            "search_triggered" to true
                        )
                    )

                    navController.navigate(com.example.myele.navigation.Screen.SearchResult.createRoute(searchText))
                }
            },
            onBackClicked = {
                navController.popBackStack()
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 搜索推荐
            if (searchSuggestions.isNotEmpty()) {
                item {
                    SearchSuggestions(
                        suggestions = searchSuggestions,
                        onSuggestionClick = { suggestion ->
                            searchText = suggestion
                            presenter.onSearchClicked(suggestion)
                            navController.navigate(com.example.myele.navigation.Screen.SearchResult.createRoute(suggestion))
                        }
                    )
                }
            }

            // 历史搜索
            if (searchHistory.isNotEmpty()) {
                item {
                    SearchHistory(
                        history = searchHistory,
                        onHistoryItemClick = { keyword ->
                            searchText = keyword
                            presenter.onHistoryItemClicked(keyword)
                            navController.navigate(com.example.myele.navigation.Screen.SearchResult.createRoute(keyword))
                        },
                        onClearClick = {
                            presenter.onClearHistoryClicked()
                        }
                    )
                }
            }

            // 搜索发现
            item {
                SearchDiscovery(
                    stores = recommendedStores,
                    onStoreClick = { storeName ->
                        searchText = storeName
                        presenter.onSearchClicked(storeName)
                        navController.navigate(com.example.myele.navigation.Screen.SearchResult.createRoute(storeName))
                    }
                )
            }

            // 专属好店
            if (recommendedStores.isNotEmpty()) {
                item {
                    RecommendedStores(
                        stores = recommendedStores,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun TopSearchBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.Black
                )
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = onSearchTextChanged,
                        placeholder = {
                            Text(
                                text = "coco都可 7.5元无门槛",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        singleLine = true
                    )
                }
            }

            Text(
                text = "搜索",
                color = Color(0xFF00BFFF),
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { onSearchClicked() }
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun SearchSuggestions(suggestions: List<String>, onSuggestionClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        suggestions.forEach { suggestion ->
            Text(
                text = suggestion,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSuggestionClick(suggestion) }
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun SearchHistory(
    history: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onClearClick: () -> Unit
) {
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "历史搜索",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(onClick = { showClearDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "清空",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(history) { item ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F5F5),
                    modifier = Modifier.clickable { onHistoryItemClick(item) }
                ) {
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }

    // 清除历史确认弹窗
    if (showClearDialog) {
        ClearHistoryDialog(
            onDismiss = { showClearDialog = false },
            onConfirm = {
                onClearClick()
                showClearDialog = false
            }
        )
    }
}

@Composable
fun ClearHistoryDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "确认删除全部历史记录？",
                fontSize = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("删除", color = Color(0xFF00BFFF), fontSize = 16.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消", color = Color.Gray, fontSize = 16.sp)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun SearchDiscovery(
    stores: List<StoreRecommendation>,
    onStoreClick: (String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "搜索发现",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "刷新",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 显示店铺名称
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stores) { store ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F5F5),
                    modifier = Modifier.clickable {
                        // 记录快速搜索
                        com.example.myele.utils.ActionLogger.logAction(
                            context = context,
                            action = "search",
                            page = "search",
                            pageInfo = mapOf("search_query" to store.name)
                        )
                        onStoreClick(store.name)
                    }
                ) {
                    Text(
                        text = store.name,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedStores(stores: List<StoreRecommendation>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "专属好店",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00BFFF),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        stores.forEachIndexed { index, store ->
            RecommendedStoreItem(
                store = store,
                rank = index + 1,
                navController = navController
            )
            if (index < stores.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun RecommendedStoreItem(store: StoreRecommendation, rank: Int, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // 点击跳转到商家页面
                navController.navigate(
                    com.example.myele.navigation.Screen.StorePage.createRoute(store.id)
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 排名徽章
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    when (rank) {
                        1 -> Color(0xFFFFD700)
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> Color.Gray
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 店铺图片
        RestaurantImage(
            restaurantName = store.name,
            size = 60.dp,
            cornerRadius = 8.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = store.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "推荐系数 ${store.recommendCount}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

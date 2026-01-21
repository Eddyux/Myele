package com.example.myele_sim.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.myele_sim.data.ActionLogger

@Composable
fun SearchScreen(navController: NavController) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }
    var recommendedStores by remember { mutableStateOf<List<StoreRecommendation>>(emptyList()) }

    // 记录进入搜索页面
    LaunchedEffect(Unit) {
        ActionLogger.logAction(
            context = context,
            action = "navigate",
            page = "search",
            extraData = mapOf(
                "from_page" to "home"
            )
        )
    }

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

                    navController.navigate(com.example.myele_sim.navigation.Screen.SearchResult.createRoute(searchText))
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

                            // 记录搜索推荐点击
                            ActionLogger.logAction(
                                context = context,
                                action = "perform_search",
                                page = "home",
                                extraData = mapOf(
                                    "search_query" to suggestion,
                                    "search_triggered" to true
                                )
                            )

                            navController.navigate(com.example.myele_sim.navigation.Screen.SearchResult.createRoute(suggestion))
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

                            // 记录历史搜索点击
                            ActionLogger.logAction(
                                context = context,
                                action = "perform_search",
                                page = "home",
                                extraData = mapOf(
                                    "search_query" to keyword,
                                    "search_triggered" to true
                                )
                            )

                            navController.navigate(com.example.myele_sim.navigation.Screen.SearchResult.createRoute(keyword))
                        },
                        onClearClick = {
                            presenter.onClearHistoryClicked()

                            // 记录清除历史记录操作
                            ActionLogger.logAction(
                                context = context,
                                action = "clear_search_history",
                                page = "search",
                                extraData = mapOf(
                                    "history_cleared" to true
                                )
                            )
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

                        // 记录搜索发现点击
                        ActionLogger.logAction(
                            context = context,
                            action = "perform_search",
                            page = "home",
                            extraData = mapOf(
                                "search_query" to storeName,
                                "search_triggered" to true
                            )
                        )

                        navController.navigate(com.example.myele_sim.navigation.Screen.SearchResult.createRoute(storeName))
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

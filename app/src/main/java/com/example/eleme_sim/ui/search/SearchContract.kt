package com.example.eleme_sim.ui.search

/**
 * Search页面的MVP契约
 */
interface SearchContract {
    interface View {
        fun updateSearchHistory(history: List<String>)
        fun updateSearchSuggestions(suggestions: List<String>)
        fun updateRecommendedStores(stores: List<StoreRecommendation>)
    }

    interface Presenter {
        fun onViewCreated()
        fun onSearchTextChanged(text: String)
        fun onSearchClicked(keyword: String)
        fun onHistoryItemClicked(keyword: String)
        fun onClearHistoryClicked()
        fun onBackClicked()
        fun onDestroy()
    }
}

data class StoreRecommendation(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val recommendCount: Int
)

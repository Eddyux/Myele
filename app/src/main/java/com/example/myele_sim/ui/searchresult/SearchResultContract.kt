package com.example.myele_sim.ui.searchresult

import com.example.myele_sim.model.Restaurant

/**
 * SearchResult页面的MVP契约
 */
interface SearchResultContract {
    interface View {
        fun updateRestaurants(restaurants: List<Restaurant>)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun onViewCreated(keyword: String)
        fun onSortChanged(sortType: SortType)
        fun onBackClicked()
        fun onDestroy()
    }
}

enum class SortType {
    COMPREHENSIVE,      // 综合排序
    PRICE_LOW_TO_HIGH, // 人均价低到高
    DISTANCE,          // 距离优先
    RATING,            // 商家好评优先
    MIN_DELIVERY       // 起送低到高
}

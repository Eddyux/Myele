package com.example.myele.ui.searchresult

import com.example.myele.model.Restaurant

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
    COMPREHENSIVE,  // 综合排序
    SALES,          // 销量优先
    SPEED,          // 速度优先
    FILTER          // 筛选
}

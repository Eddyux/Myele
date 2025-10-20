package com.example.myele.ui.takeout

import com.example.myele.model.Restaurant

/**
 * Takeout页面的MVP契约
 */
interface TakeoutContract {
    interface View {
        fun updateRestaurants(restaurants: List<Restaurant>)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun onViewCreated()
        fun onCategorySelected(category: String)
        fun onBackClicked()
        fun onDestroy()
    }
}

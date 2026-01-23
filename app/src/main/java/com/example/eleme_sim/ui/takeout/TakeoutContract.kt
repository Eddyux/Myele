package com.example.eleme_sim.ui.takeout

import com.example.eleme_sim.model.Restaurant

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

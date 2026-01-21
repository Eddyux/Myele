package com.example.myele_sim.ui.home

import com.example.myele_sim.model.Restaurant

/**
 * Home页面的MVP契约接口
 */
interface HomeContract {
    interface View {
        fun showRestaurants(restaurants: List<Restaurant>)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter {
        fun loadData()
        fun onSearchClicked(keyword: String)
        fun onRestaurantClicked(restaurantId: String)
        fun onServiceClicked(serviceName: String)
        fun onDestroy()
    }
}

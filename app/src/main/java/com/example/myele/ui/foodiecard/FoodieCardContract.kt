package com.example.myele.ui.foodiecard

import com.example.myele.model.Coupon
import com.example.myele.model.Restaurant
import com.example.myele.model.SuperFoodieCard

/**
 * FoodieCard页面的MVP契约接口
 */
interface FoodieCardContract {
    /**
     * View接口 - 定义View层需要实现的方法
     */
    interface View {
        fun showFoodieCards(cards: List<SuperFoodieCard>)
        fun showExplosivePackages(packages: List<Coupon>)
        fun showRestaurants(restaurants: List<Restaurant>)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    /**
     * Presenter接口 - 定义Presenter层需要实现的方法
     */
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadData()
        fun filterRestaurants(type: String) // "explosive" or "all"
        fun sortRestaurants(sortType: String) // "综合", "人气", etc.
    }
}

package com.example.eleme_sim.ui.home

import com.example.eleme_sim.data.DataRepository

/**
 * Home页面的Presenter
 */
class HomePresenter(
    private val view: HomeContract.View,
    private val repository: DataRepository
) : HomeContract.Presenter {

    override fun loadData() {
        view.showLoading()
        try {
            val restaurants = repository.loadRestaurants()
            view.showRestaurants(restaurants)
            view.hideLoading()
        } catch (e: Exception) {
            view.showError(e.message ?: "加载失败")
            view.hideLoading()
        }
    }

    override fun onSearchClicked(keyword: String) {
        // 导航到搜索结果页面
    }

    override fun onRestaurantClicked(restaurantId: String) {
        // 导航到餐厅详情页面
    }

    override fun onServiceClicked(serviceName: String) {
        // 处理服务入口点击
    }

    override fun onDestroy() {
        // 清理资源
    }
}

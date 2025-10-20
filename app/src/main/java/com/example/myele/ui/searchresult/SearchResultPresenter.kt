package com.example.myele.ui.searchresult

import android.content.Context
import com.example.myele.data.DataRepository
import com.example.myele.model.Restaurant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * SearchResult页面的Presenter
 */
class SearchResultPresenter(
    private val view: SearchResultContract.View,
    private val context: Context
) : SearchResultContract.Presenter {

    private val repository = DataRepository(context)
    private var allRestaurants = listOf<Restaurant>()
    private var currentSortType = SortType.COMPREHENSIVE

    override fun onViewCreated(keyword: String) {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val restaurants = repository.loadRestaurants()
            // 根据关键词过滤餐厅
            allRestaurants = restaurants.filter {
                it.name.contains(keyword, ignoreCase = true)
            }
            withContext(Dispatchers.Main) {
                view.hideLoading()
                sortAndUpdateRestaurants(currentSortType)
            }
        }
    }

    override fun onSortChanged(sortType: SortType) {
        currentSortType = sortType
        sortAndUpdateRestaurants(sortType)
    }

    override fun onBackClicked() {
        // 返回操作由View层处理
    }

    override fun onDestroy() {
        // 清理资源
    }

    private fun sortAndUpdateRestaurants(sortType: SortType) {
        val sortedRestaurants = when (sortType) {
            SortType.COMPREHENSIVE -> allRestaurants
            SortType.SALES -> allRestaurants.sortedByDescending { it.salesVolume }
            SortType.SPEED -> allRestaurants.sortedBy { it.deliveryTime }
            SortType.FILTER -> allRestaurants
        }
        view.updateRestaurants(sortedRestaurants)
    }
}

package com.example.myele.ui.searchresult

import android.content.Context
import com.example.myele.data.DataRepository
import com.example.myele.model.Restaurant
import com.example.myele.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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
    private var allProducts = listOf<Product>()
    private var currentSortType = SortType.COMPREHENSIVE
    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onViewCreated(keyword: String) {
        view.showLoading()
        presenterScope.launch {
            try {
                val restaurants = withContext(Dispatchers.IO) {
                    repository.loadRestaurants()
                }
                val products = withContext(Dispatchers.IO) {
                    repository.loadProducts()
                }
                allProducts = products

                // 根据关键词过滤餐厅，并为每个餐厅关联产品
                allRestaurants = restaurants.filter { restaurant ->
                    matchesKeyword(restaurant, keyword)
                }.map { restaurant ->
                    val restaurantProducts = allProducts.filter { it.restaurantId == restaurant.restaurantId }
                    restaurant.copy(products = restaurantProducts.take(3))
                }

                view.hideLoading()
                sortAndUpdateRestaurants(currentSortType)
            } catch (e: Exception) {
                view.hideLoading()
                e.printStackTrace()
            }
        }
    }

    private fun matchesKeyword(restaurant: Restaurant, keyword: String): Boolean {
        val lowerKeyword = keyword.lowercase()

        // 直接匹配餐厅名称
        if (restaurant.name.contains(keyword, ignoreCase = true)) {
            return true
        }

        // 为每个商家定义多种搜索关键词
        val keywords = when (restaurant.name) {
            "蜜雪冰城" -> listOf("蜜雪冰城", "mixue", "mi", "mix", "mxbc")
            "瑞幸咖啡" -> listOf("瑞幸咖啡", "瑞幸", "ruixin", "rx", "luckin")
            "茶百道" -> listOf("茶百道", "chabaidao", "cbd")
            "星巴克" -> listOf("星巴克", "xingbake", "xbk", "starbucks")
            "喜茶" -> listOf("喜茶", "xicha", "xc", "heytea")
            "川香麻辣烫" -> listOf("川香麻辣烫", "川香", "chuanxiang", "cx", "麻辣烫", "malatang", "mlt")
            "老北京炸酱面" -> listOf("老北京炸酱面", "老北京", "laobeijing", "lbj", "炸酱面", "zhajangmian", "zjm")
            "湘味轩" -> listOf("湘味轩", "xiangweixuan", "xwx")
            "粤式早茶" -> listOf("粤式早茶", "粤式", "yueshi", "ys", "早茶", "zaocha")
            "韩式炸鸡" -> listOf("韩式炸鸡", "韩式", "hanshi", "hs", "炸鸡", "zhaji")
            else -> emptyList()
        }

        // 检查是否匹配任何一个关键词
        return keywords.any { it.equals(lowerKeyword, ignoreCase = true) }
    }

    override fun onSortChanged(sortType: SortType) {
        currentSortType = sortType
        sortAndUpdateRestaurants(sortType)
    }

    override fun onBackClicked() {
        // 返回操作由View层处理
    }

    override fun onDestroy() {
        // 清理资源，取消所有协程
        presenterScope.cancel()
    }

    private fun sortAndUpdateRestaurants(sortType: SortType) {
        val sortedRestaurants = when (sortType) {
            SortType.COMPREHENSIVE -> allRestaurants
            SortType.PRICE_LOW_TO_HIGH -> allRestaurants.sortedBy { it.averagePrice }
            SortType.DISTANCE -> allRestaurants.sortedBy { it.distance }
            SortType.RATING -> allRestaurants.sortedByDescending { it.rating }
            SortType.MIN_DELIVERY -> allRestaurants.sortedBy { it.minDeliveryAmount }
        }
        view.updateRestaurants(sortedRestaurants)
    }
}

package com.example.myele.ui.searchresult

import android.content.Context
import com.example.myele.data.DataRepository
import com.example.myele.model.Restaurant
import com.example.myele.model.RestaurantFeature
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
    private var currentFilterOptions = FilterOptions()
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
        applyFilterAndSort()
    }

    fun applyFilter(filterOptions: FilterOptions) {
        currentFilterOptions = filterOptions

        // 记录筛选操作
        val filterList = mutableListOf<String>()
        filterList.addAll(filterOptions.promotions)
        filterList.addAll(filterOptions.features)

        if (filterOptions.priceRange != null) {
            val (minPrice, maxPrice) = filterOptions.priceRange
            filterList.add("价格区间:${minPrice.toInt()}-${maxPrice.toInt()}")
        }

        // 只在有筛选选项时记录
        if (filterList.isNotEmpty()) {
            com.example.myele.utils.ActionLogger.logAction(
                context = context,
                action = "apply_filter",
                page = "search_result",
                pageInfo = mapOf(),
                extraData = mapOf(
                    "filters" to filterList
                )
            )
        }

        applyFilterAndSort()
    }

    override fun onBackClicked() {
        // 返回操作由View层处理
    }

    override fun onDestroy() {
        // 清理资源，取消所有协程
        presenterScope.cancel()
    }

    fun sortBySales() {
        // 按销量从高到低排序
        val sortedRestaurants = allRestaurants.sortedByDescending { it.salesVolume }
        view.updateRestaurants(sortedRestaurants)
    }

    fun sortByDeliveryTime() {
        // 按配送时间从快到慢排序
        val sortedRestaurants = allRestaurants.sortedBy { it.deliveryTime }
        view.updateRestaurants(sortedRestaurants)
    }

    private fun applyFilterAndSort() {
        var filteredRestaurants = allRestaurants

        // 应用筛选条件
        // 优惠活动筛选
        if (currentFilterOptions.promotions.isNotEmpty()) {
            filteredRestaurants = filteredRestaurants.filter { restaurant ->
                currentFilterOptions.promotions.any { promotion ->
                    when (promotion) {
                        "首次光顾减" -> restaurant.hasFirstOrderDiscount
                        "满减优惠" -> restaurant.hasFullReduction || restaurant.coupons.isNotEmpty()
                        "下单返红包" -> restaurant.hasRedPacketReward
                        "配送费优惠" -> restaurant.hasFreeDelivery || restaurant.deliveryFee < 3.0
                        "特价商品" -> restaurant.hasSpecialOffer
                        "0元起送" -> restaurant.minDeliveryAmount == 0.0
                        else -> false
                    }
                }
            }
        }

        // 商家特色筛选
        if (currentFilterOptions.features.isNotEmpty()) {
            filteredRestaurants = filteredRestaurants.filter { restaurant ->
                currentFilterOptions.features.any { feature ->
                    when (feature) {
                        "蜂鸟准时达" -> RestaurantFeature.FENGNIAO_DELIVERY in restaurant.features
                        "到店自取" -> RestaurantFeature.SELF_PICKUP in restaurant.features
                        "品牌商家" -> RestaurantFeature.BRAND_MERCHANT in restaurant.features || restaurant.rating >= 4.5
                        "新店" -> RestaurantFeature.NEW_STORE in restaurant.features
                        "食无忧" -> RestaurantFeature.FOOD_SAFETY in restaurant.features
                        "跨天预订" -> RestaurantFeature.CROSS_DAY_BOOKING in restaurant.features
                        "线上开票" -> RestaurantFeature.ONLINE_INVOICE in restaurant.features
                        "慢必赔" -> RestaurantFeature.SLOW_MUST_COMPENSATE in restaurant.features
                        else -> false
                    }
                }
            }
        }

        // 价格筛选
        currentFilterOptions.priceRange?.let { priceRange ->
            val (minPrice, maxPrice) = priceRange
            filteredRestaurants = filteredRestaurants.filter { restaurant ->
                restaurant.averagePrice >= minPrice.toDouble() && restaurant.averagePrice <= maxPrice.toDouble()
            }
        }

        // 应用排序
        val sortedRestaurants = when (currentSortType) {
            SortType.COMPREHENSIVE -> filteredRestaurants
            SortType.PRICE_LOW_TO_HIGH -> filteredRestaurants.sortedBy { it.averagePrice }
            SortType.DISTANCE -> filteredRestaurants.sortedBy { it.distance }
            SortType.RATING -> filteredRestaurants.sortedByDescending { it.rating }
            SortType.MIN_DELIVERY -> filteredRestaurants.sortedBy { it.minDeliveryAmount }
        }

        view.updateRestaurants(sortedRestaurants)
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

/**
 * 筛选选项数据类
 */
data class FilterOptions(
    val promotions: Set<String> = setOf(),
    val features: Set<String> = setOf(),
    val priceRange: Pair<Float, Float>? = null
)

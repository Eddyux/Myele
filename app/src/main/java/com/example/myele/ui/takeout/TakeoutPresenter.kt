package com.example.myele.ui.takeout

import android.content.Context
import com.example.myele.data.DataRepository
import com.example.myele.model.Restaurant
import com.example.myele.model.RestaurantFeature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Takeout页面的Presenter
 */
class TakeoutPresenter(
    private val view: TakeoutContract.View,
    private val context: Context
) : TakeoutContract.Presenter {

    private val repository = DataRepository(context)
    private var allRestaurants = listOf<Restaurant>()
    private var currentSortType = SortType.COMPREHENSIVE
    private var currentFilterOptions = TakeoutFilterOptions()
    private var currentCategory = "精选"
    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onViewCreated() {
        view.showLoading()
        presenterScope.launch {
            allRestaurants = withContext(Dispatchers.IO) {
                repository.loadRestaurants()
            }
            view.hideLoading()
            applyFilterAndSort()
        }
    }

    override fun onCategorySelected(category: String) {
        currentCategory = category
        applyFilterAndSort()
    }

    override fun onBackClicked() {
        // 返回操作由View层处理
    }

    override fun onDestroy() {
        // 清理资源，取消所有协程
        presenterScope.cancel()
    }

    fun onSortChanged(sortType: SortType) {
        currentSortType = sortType
        applyFilterAndSort()
    }

    fun applyFilter(filterOptions: TakeoutFilterOptions) {
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
                page = "takeout",
                pageInfo = mapOf(),
                extraData = mapOf(
                    "filters" to filterList
                )
            )
        }

        applyFilterAndSort()
    }

    fun sortByDeliveryTime() {
        // 按配送时间从快到慢排序
        val sortedRestaurants = allRestaurants.sortedBy { it.deliveryTime }
        view.updateRestaurants(sortedRestaurants)
    }

    fun shuffleRestaurants() {
        // 随机打乱餐厅列表
        val shuffledRestaurants = allRestaurants.shuffled()
        view.updateRestaurants(shuffledRestaurants)
    }

    fun sortByRedPacket() {
        // 按红包优惠排序（有红包奖励的优先）
        val sortedRestaurants = allRestaurants.sortedByDescending { it.hasRedPacketReward }
        view.updateRestaurants(sortedRestaurants)
    }

    fun sortByDeliveryFee() {
        // 按配送费排序（配送费低的优先，免配送费的最优先）
        val sortedRestaurants = allRestaurants.sortedWith(
            compareBy({ !it.hasFreeDelivery }, { it.deliveryFee })
        )
        view.updateRestaurants(sortedRestaurants)
    }

    fun sortByNoThreshold() {
        // 按无门槛红包排序（无门槛或起送价低的优先）
        val sortedRestaurants = allRestaurants.sortedBy { it.minDeliveryAmount }
        view.updateRestaurants(sortedRestaurants)
    }

    private fun applyFilterAndSort() {
        var filteredRestaurants = allRestaurants

        // 分类筛选
        if (currentCategory != "精选" && currentCategory != "全部") {
            // 这里可以根据实际需求添加分类逻辑
            // 目前保持所有餐厅
        }

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
            SortType.DELIVERY_SPEED -> filteredRestaurants.sortedBy { it.deliveryTime }
        }

        view.updateRestaurants(sortedRestaurants)
    }
}

/**
 * 筛选选项数据类
 */
data class TakeoutFilterOptions(
    val promotions: Set<String> = setOf(),
    val features: Set<String> = setOf(),
    val priceRange: Pair<Float, Float>? = null
)

package com.example.myele.data

import android.content.Context
import com.example.myele.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 数据仓库 - 负责从assets加载JSON数据
 */
class DataRepository(private val context: Context) {
    private val gson = Gson()
    private val preferencesManager = PreferencesManager(context)

    fun loadRestaurants(): List<Restaurant> {
        val restaurants: List<Restaurant> = loadJsonData("data/restaurants.json")
        // 为每个餐厅添加多样化的属性
        return restaurants.mapIndexed { index, restaurant ->
            // 先创建一个安全的副本，确保所有字段都不为null
            val safeRestaurant = restaurant.copy(
                products = restaurant.products ?: emptyList(),
                coupons = restaurant.coupons ?: emptyList(),
                features = restaurant.features ?: emptyList()
            )

            safeRestaurant.copy(
                // 优惠活动多样化 (每个餐厅不同)
                hasFirstOrderDiscount = (index % 5 == 0), // 20%的餐厅有首次光顾减
                hasFullReduction = (index % 3 == 0), // 33%的餐厅有满减优惠
                hasRedPacketReward = (index % 4 == 0), // 25%的餐厅有返红包
                hasFreeDelivery = (index % 6 == 0 || safeRestaurant.deliveryFee < 2.0), // 部分餐厅免配送费
                hasSpecialOffer = (index % 7 == 0), // 14%的餐厅有特价商品

                // 商家特色多样化
                features = buildList {
                    if (safeRestaurant.rating >= 4.5) add(RestaurantFeature.BRAND_MERCHANT)
                    if (index % 4 == 0) add(RestaurantFeature.FENGNIAO_DELIVERY)
                    if (index % 8 == 0) add(RestaurantFeature.SELF_PICKUP)
                    if (index % 10 == 0) add(RestaurantFeature.NEW_STORE)
                    if (index % 5 == 0) add(RestaurantFeature.FOOD_SAFETY)
                    if (index % 9 == 0) add(RestaurantFeature.CROSS_DAY_BOOKING)
                    if (index % 7 == 0) add(RestaurantFeature.ONLINE_INVOICE)
                    if (index % 6 == 0) add(RestaurantFeature.SLOW_MUST_COMPENSATE)
                },

                // 人均价多样化 (在原价格基础上增加一些随机性)
                averagePrice = when (index % 6) {
                    0 -> safeRestaurant.averagePrice * 0.8 // 便宜
                    1 -> safeRestaurant.averagePrice * 1.2 // 稍贵
                    2 -> safeRestaurant.averagePrice * 1.5 // 较贵
                    3 -> safeRestaurant.averagePrice * 0.9 // 稍便宜
                    4 -> safeRestaurant.averagePrice * 1.1 // 稍贵
                    else -> safeRestaurant.averagePrice // 原价
                }
            )
        }
    }

    fun loadProducts(): List<Product> {
        return loadJsonData("data/products.json")
    }

    fun loadCoupons(): List<Coupon> {
        return loadJsonData("data/coupons.json")
    }

    fun loadAddresses(): List<Address> {
        return loadJsonData("data/addresses.json")
    }

    fun loadOrders(): List<Order> {
        return loadJsonData("data/orders.json")
    }

    fun loadUsers(): List<UserProfile> {
        return loadJsonData("data/users.json")
    }

    fun loadReviews(): List<Review> {
        return loadJsonData("data/reviews.json")
    }

    fun loadSearchHistory(): List<SearchHistory> {
        return loadJsonData("data/search_history.json")
    }

    // Convenience methods with 'get' prefix (for MVP presenters)
    fun getRestaurants(): List<Restaurant> = loadRestaurants()
    fun getProducts(): List<Product> = loadProducts()
    fun getCoupons(): List<Coupon> = loadCoupons()

    /**
     * 获取地址列表（包含从JSON加载的和用户自定义的，排除已删除的）
     */
    fun getAddresses(): List<Address> {
        val baseAddresses = loadAddresses()
        val customAddresses = preferencesManager.getCustomAddresses()
        val deletedIds = preferencesManager.getDeletedAddressIds()

        // 合并基础地址和自定义地址，排除已删除的
        return (baseAddresses + customAddresses).filter { it.addressId !in deletedIds }
    }

    fun getOrders(): List<Order> = loadOrders()
    fun getUsers(): List<UserProfile> = loadUsers()
    fun getReviews(): List<Review> = loadReviews()
    fun getSearchHistory(): List<SearchHistory> = loadSearchHistory()

    // ============ 地址管理方法 ============

    /**
     * 添加新地址
     */
    fun addAddress(address: Address) {
        val customAddresses = preferencesManager.getCustomAddresses()
        customAddresses.add(address)
        preferencesManager.saveCustomAddresses(customAddresses)
    }

    /**
     * 更新地址
     */
    fun updateAddress(address: Address) {
        val customAddresses = preferencesManager.getCustomAddresses()
        val index = customAddresses.indexOfFirst { it.addressId == address.addressId }

        if (index >= 0) {
            // 如果在自定义列表中，直接更新
            customAddresses[index] = address
        } else {
            // 如果是原始数据中的地址，添加到自定义列表（覆盖原始数据）
            customAddresses.add(address)
        }

        preferencesManager.saveCustomAddresses(customAddresses)
    }

    /**
     * 删除地址
     */
    fun deleteAddress(addressId: String) {
        // 从自定义地址列表中移除
        val customAddresses = preferencesManager.getCustomAddresses()
        customAddresses.removeIf { it.addressId == addressId }
        preferencesManager.saveCustomAddresses(customAddresses)

        // 添加到已删除列表（防止原始数据中的地址再次显示）
        val deletedIds = preferencesManager.getDeletedAddressIds()
        deletedIds.add(addressId)
        preferencesManager.saveDeletedAddressIds(deletedIds)
    }

    /**
     * 获取PreferencesManager实例
     */
    fun getPreferencesManager(): PreferencesManager {
        return preferencesManager
    }

    private inline fun <reified T> loadJsonData(fileName: String): List<T> {
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<T>>() {}.type
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

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
        // 使用自定义加载方法来处理Restaurant的null字段
        val jsonString = try {
            context.assets.open("data/restaurants.json").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

        // 解析JSON为Map列表，然后手动创建Restaurant对象
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        val dataList: List<Map<String, Any>> = gson.fromJson(jsonString, type) ?: return emptyList()

        val restaurants = dataList.map { data ->
            Restaurant(
                restaurantId = data["restaurantId"] as? String ?: "",
                name = data["name"] as? String ?: "",
                rating = (data["rating"] as? Double) ?: 0.0,
                salesVolume = ((data["salesVolume"] as? Double)?.toInt()) ?: 0,
                deliveryTime = ((data["deliveryTime"] as? Double)?.toInt()) ?: 30,
                distance = (data["distance"] as? Double) ?: 0.0,
                minDeliveryAmount = (data["minDeliveryAmount"] as? Double) ?: 0.0,
                deliveryFee = (data["deliveryFee"] as? Double) ?: 0.0,
                averagePrice = (data["averagePrice"] as? Double) ?: 30.0,
                cuisineType = data["cuisineType"] as? String ?: "",
                features = emptyList(), // 默认空列表
                products = emptyList(), // 默认空列表
                coupons = emptyList(),  // 默认空列表
                phone = data["phone"] as? String ?: "",
                address = data["address"] as? String ?: "",
                isFollowed = data["isFollowed"] as? Boolean ?: false,
                isFavorite = data["isFavorite"] as? Boolean ?: false,
                hasAppointment = data["hasAppointment"] as? Boolean ?: false
            )
        }

        // 为每个餐厅添加多样化的属性
        return restaurants.mapIndexed { index, restaurant ->
            restaurant.copy(
                // 优惠活动多样化 (每个餐厅不同)
                hasFirstOrderDiscount = (index % 5 == 0), // 20%的餐厅有首次光顾减
                hasFullReduction = (index % 3 == 0), // 33%的餐厅有满减优惠
                hasRedPacketReward = (index % 4 == 0), // 25%的餐厅有返红包
                hasFreeDelivery = (index % 6 == 0 || restaurant.deliveryFee < 2.0), // 部分餐厅免配送费
                hasSpecialOffer = (index % 7 == 0), // 14%的餐厅有特价商品

                // 商家特色多样化
                features = buildList {
                    if (restaurant.rating >= 4.5) add(RestaurantFeature.BRAND_MERCHANT)
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
                    0 -> restaurant.averagePrice * 0.8 // 便宜
                    1 -> restaurant.averagePrice * 1.2 // 稍贵
                    2 -> restaurant.averagePrice * 1.5 // 较贵
                    3 -> restaurant.averagePrice * 0.9 // 稍便宜
                    4 -> restaurant.averagePrice * 1.1 // 稍贵
                    else -> restaurant.averagePrice // 原价
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
     * 获取地址列表（包含JSON原始地址和运行时新增的，排除运行时删除的）
     */
    fun getAddresses(): List<Address> {
        val baseAddresses = loadAddresses()
        val runtimeAddresses = AddressManager.getRuntimeAddresses()
        val deletedIds = AddressManager.getDeletedAddressIds()

        // 合并基础地址和运行时地址，排除已删除的
        return (runtimeAddresses + baseAddresses).filter { it.addressId !in deletedIds }
    }

    fun getOrders(): List<Order> = loadOrders()
    fun getUsers(): List<UserProfile> = loadUsers()
    fun getReviews(): List<Review> = loadReviews()
    fun getSearchHistory(): List<SearchHistory> = loadSearchHistory()

    // ============ 地址管理方法 ============

    /**
     * 添加新地址（运行时，重启后恢复）
     */
    fun addAddress(address: Address) {
        AddressManager.addAddress(address)
    }

    /**
     * 更新地址（运行时，重启后恢复）
     */
    fun updateAddress(address: Address) {
        AddressManager.updateAddress(address)
    }

    /**
     * 删除地址（运行时，重启后恢复）
     */
    fun deleteAddress(addressId: String) {
        AddressManager.deleteAddress(addressId)
    }

    // ============ 评价管理方法 ============

    /**
     * 删除评价
     */
    fun deleteReview(reviewId: String) {
        val deletedIds = preferencesManager.getDeletedReviewIds()
        deletedIds.add(reviewId)
        preferencesManager.saveDeletedReviewIds(deletedIds)
    }

    /**
     * 获取评价列表（排除已删除的）
     */
    fun getFilteredReviews(): List<Review> {
        val reviews = loadReviews()
        val deletedIds = preferencesManager.getDeletedReviewIds()
        return reviews.filter { it.reviewId !in deletedIds }
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

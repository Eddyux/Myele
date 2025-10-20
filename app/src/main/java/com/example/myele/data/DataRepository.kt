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

    fun loadRestaurants(): List<Restaurant> {
        return loadJsonData("data/restaurants.json")
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
    fun getAddresses(): List<Address> = loadAddresses()
    fun getOrders(): List<Order> = loadOrders()
    fun getUsers(): List<UserProfile> = loadUsers()
    fun getReviews(): List<Review> = loadReviews()
    fun getSearchHistory(): List<SearchHistory> = loadSearchHistory()

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

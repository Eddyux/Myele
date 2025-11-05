package com.example.myele.data

import android.content.Context
import android.content.SharedPreferences
import com.example.myele.model.Address
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * SharedPreferences管理器 - 负责管理应用的持久化数据
 */
class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("MyElePrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // ============ 支付设置 ============

    /**
     * 获取支付宝免密支付状态
     */
    fun getAlipayFreePasswordEnabled(): Boolean {
        return prefs.getBoolean(KEY_ALIPAY_FREE_PASSWORD, false)
    }

    /**
     * 设置支付宝免密支付状态
     */
    fun setAlipayFreePasswordEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ALIPAY_FREE_PASSWORD, enabled).apply()
    }

    /**
     * 获取余额免密支付状态
     */
    fun getBalanceFreePasswordEnabled(): Boolean {
        return prefs.getBoolean(KEY_BALANCE_FREE_PASSWORD, false)
    }

    /**
     * 设置余额免密支付状态
     */
    fun setBalanceFreePasswordEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_BALANCE_FREE_PASSWORD, enabled).apply()
    }

    // ============ 通知设置 ============

    /**
     * 获取系统消息通知状态
     */
    fun getSystemNotificationEnabled(): Boolean {
        return prefs.getBoolean(KEY_SYSTEM_NOTIFICATION, true)
    }

    /**
     * 设置系统消息通知状态
     */
    fun setSystemNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SYSTEM_NOTIFICATION, enabled).apply()
    }

    /**
     * 获取应用内横幅通知状态
     */
    fun getInAppBannerEnabled(): Boolean {
        return prefs.getBoolean(KEY_IN_APP_BANNER, true)
    }

    /**
     * 设置应用内横幅通知状态
     */
    fun setInAppBannerEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_IN_APP_BANNER, enabled).apply()
    }

    // ============ 用户信息 ============

    /**
     * 获取用户手机号
     */
    fun getUserPhone(): String {
        return prefs.getString(KEY_USER_PHONE, "189******18") ?: "189******18"
    }

    /**
     * 设置用户手机号
     */
    fun setUserPhone(phone: String) {
        prefs.edit().putString(KEY_USER_PHONE, phone).apply()
    }

    // ============ 地址管理 ============

    /**
     * 获取自定义地址列表（用户新增或修改的地址）
     */
    fun getCustomAddresses(): MutableList<Address> {
        val json = prefs.getString(KEY_CUSTOM_ADDRESSES, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Address>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    /**
     * 保存自定义地址列表
     */
    fun saveCustomAddresses(addresses: List<Address>) {
        val json = gson.toJson(addresses)
        prefs.edit().putString(KEY_CUSTOM_ADDRESSES, json).apply()
    }

    /**
     * 获取已删除的地址ID列表
     */
    fun getDeletedAddressIds(): MutableSet<String> {
        return prefs.getStringSet(KEY_DELETED_ADDRESSES, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    }

    /**
     * 保存已删除的地址ID列表
     */
    fun saveDeletedAddressIds(ids: Set<String>) {
        prefs.edit().putStringSet(KEY_DELETED_ADDRESSES, ids).apply()
    }

    // ============ 评价管理 ============

    /**
     * 获取已删除的评价ID列表
     */
    fun getDeletedReviewIds(): MutableSet<String> {
        return prefs.getStringSet(KEY_DELETED_REVIEWS, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    }

    /**
     * 保存已删除的评价ID列表
     */
    fun saveDeletedReviewIds(ids: Set<String>) {
        prefs.edit().putStringSet(KEY_DELETED_REVIEWS, ids).apply()
    }

    companion object {
        // 支付设置相关
        private const val KEY_ALIPAY_FREE_PASSWORD = "alipay_free_password"
        private const val KEY_BALANCE_FREE_PASSWORD = "balance_free_password"

        // 通知设置相关
        private const val KEY_SYSTEM_NOTIFICATION = "system_notification"
        private const val KEY_IN_APP_BANNER = "in_app_banner"

        // 用户信息相关
        private const val KEY_USER_PHONE = "user_phone"

        // 地址管理相关
        private const val KEY_CUSTOM_ADDRESSES = "custom_addresses"
        private const val KEY_DELETED_ADDRESSES = "deleted_addresses"

        // 评价管理相关
        private const val KEY_DELETED_REVIEWS = "deleted_reviews"
    }
}

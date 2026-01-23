package com.example.eleme_sim.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * 操作日志记录器
 * 用于记录用户在APP中的操作，便于自动化测试验证
 */
object ActionLogger {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    // 定义正确的类型
    private val listType = object : TypeToken<List<Map<String, Any>>>() {}.type

    /**
     * 记录用户操作
     * @param context 上下文
     * @param action 操作类型
     * @param page 页面名称
     * @param pageInfo 页面信息
     * @param extraData 额外数据
     */
    fun logAction(
        context: Context,
        action: String,
        page: String,
        pageInfo: Map<String, Any>? = null,
        extraData: Map<String, Any>? = null
    ) {
        try {
            val data = mutableMapOf<String, Any>(
                "action" to action,
                "page" to page,
                "timestamp" to System.currentTimeMillis()
            )

            pageInfo?.let { data["page_info"] = it }
            extraData?.let { data["extra_data"] = it }

            // 读取现有的记录
            val file = File(context.filesDir, "messages.json")
            val existingList = if (file.exists()) {
                try {
                    val existingJson = file.readText()
                    if (existingJson.isNotBlank()) {
                        // 使用TypeToken正确反序列化
                        gson.fromJson<List<Map<String, Any>>>(existingJson, listType) ?: emptyList()
                    } else {
                        emptyList()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }
            } else {
                emptyList()
            }

            // 追加新记录
            val updatedList = existingList.toMutableList()
            updatedList.add(data)

            // 写入整个数组
            val jsonString = gson.toJson(updatedList)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 记录进入页面
     */
    fun logEnterPage(
        context: Context,
        page: String,
        screenName: String,
        title: String? = null,
        extraData: Map<String, Any>? = null
    ) {
        val pageInfo = mutableMapOf<String, Any>(
            "screen_name" to screenName
        )
        title?.let { pageInfo["title"] = it }

        logAction(
            context,
            "enter_${page}_page",
            page,
            pageInfo,
            extraData
        )
    }

    /**
     * 记录搜索操作
     */
    fun logSearch(
        context: Context,
        keyword: String,
        clickedSearch: Boolean = false
    ) {
        val extraData = mapOf(
            "keyword" to keyword,
            "clicked_search" to clickedSearch
        )

        logAction(
            context,
            "search",
            "search",
            null,
            extraData
        )
    }

    /**
     * 记录筛选操作
     */
    fun logFilter(
        context: Context,
        page: String,
        priceMin: Int? = null,
        priceMax: Int? = null,
        otherFilters: Map<String, Any>? = null
    ) {
        val extraData = mutableMapOf<String, Any>()
        priceMin?.let { extraData["price_min"] = it }
        priceMax?.let { extraData["price_max"] = it }
        otherFilters?.forEach { (k, v) -> extraData[k] = v }

        logAction(
            context,
            "filter",
            page,
            null,
            extraData
        )
    }

    /**
     * 记录排序选择
     */
    fun logSort(
        context: Context,
        page: String,
        sortType: String,
        sortOption: String
    ) {
        val extraData = mapOf(
            "sort_type" to sortType,
            "sort_option" to sortOption
        )

        logAction(
            context,
            "select_sort_option",
            page,
            null,
            extraData
        )
    }

    /**
     * 记录订单操作
     */
    fun logOrderAction(
        context: Context,
        action: String,
        orderId: String? = null,
        orderStatus: String? = null,
        cancelReason: String? = null,
        extraData: Map<String, Any>? = null
    ) {
        val data = mutableMapOf<String, Any>()
        orderId?.let { data["order_id"] = it }
        orderStatus?.let { data["order_status"] = it }
        cancelReason?.let { data["cancel_reason"] = it }
        extraData?.forEach { (k, v) -> data[k] = v }

        logAction(
            context,
            action,
            "order",
            null,
            data
        )
    }

    /**
     * 记录设置操作
     */
    fun logSettings(
        context: Context,
        settingType: String,
        enabled: Boolean? = null,
        value: Any? = null,
        showDialog: Boolean = false
    ) {
        val extraData = mutableMapOf<String, Any>(
            "setting_type" to settingType
        )
        enabled?.let { extraData["enabled"] = it }
        value?.let { extraData["value"] = it }
        if (showDialog) extraData["show_dialog"] = true

        logAction(
            context,
            "change_setting",
            "settings",
            null,
            extraData
        )
    }

    /**
     * 记录购物车操作
     */
    fun logCartAction(
        context: Context,
        action: String,
        selectAll: Boolean? = null,
        itemCount: Int? = null,
        extraData: Map<String, Any>? = null
    ) {
        val data = mutableMapOf<String, Any>()
        selectAll?.let { data["select_all"] = it }
        itemCount?.let { data["item_count"] = it }
        extraData?.forEach { (k, v) -> data[k] = v }

        logAction(
            context,
            action,
            "cart",
            null,
            data
        )
    }

    /**
     * 记录发送消息
     */
    fun logSendMessage(
        context: Context,
        recipient: String,
        message: String,
        recipientType: String = "rider"
    ) {
        val extraData = mapOf(
            "recipient" to recipient,
            "message" to message,
            "recipient_type" to recipientType
        )

        logAction(
            context,
            "send_message",
            "chat",
            null,
            extraData
        )
    }
}

package com.example.myele.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * 操作日志记录器 - 用于记录用户操作到messages.json
 * 数据保存在: data/data/com.example.myele/files/messages.json
 */
object ActionLogger {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private const val FILE_NAME = "messages.json"

    // 定义正确的类型
    private val listType = object : TypeToken<List<Map<String, Any>>>() {}.type

    /**
     * 记录用户操作
     * @param context 上下文
     * @param action 操作类型
     * @param page 页面标识
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

            pageInfo?.let {
                data["page_info"] = it
            }

            extraData?.let {
                data["extra_data"] = it
            }

            // 读取现有的记录
            val file = File(context.filesDir, FILE_NAME)
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
     * 读取操作日志
     * @param context 上下文
     * @return 操作日志的Map，如果文件不存在则返回null
     */
    fun readLog(context: Context): Map<String, Any>? {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) {
                return null
            }
            val jsonString = file.readText()
            return gson.fromJson(jsonString, Map::class.java) as? Map<String, Any>
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 清除操作日志
     * @param context 上下文
     */
    fun clearLog(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

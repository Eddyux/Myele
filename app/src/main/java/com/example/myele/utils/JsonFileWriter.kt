package com.example.myele.utils

import android.content.Context
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

/**
 * JSON文件写入工具类
 * 用于将结构化数据写入到应用私有存储目录
 */
object JsonFileWriter {

    /**
     * 写入JSON数据到messages.json文件
     * 文件路径: /data/data/com.example.myele/files/messages.json
     *
     * @param context 应用上下文
     * @param jsonData JSON对象数据
     * @return 是否写入成功
     */
    fun writeToMessagesJson(context: Context, jsonData: JSONObject): Boolean {
        return try {
            // 获取应用私有文件目录: /data/data/com.example.myele/files/
            val filesDir = context.filesDir
            val messagesFile = File(filesDir, "messages.json")

            // 写入JSON数据
            FileWriter(messagesFile).use { writer ->
                writer.write(jsonData.toString(4)) // 格式化输出，缩进4个空格
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 清空/初始化messages.json文件
     * 将文件内容设置为空的JSON对象: {}
     *
     * @param context 应用上下文
     * @return 是否清空成功
     */
    fun clearMessagesJson(context: Context): Boolean {
        return try {
            val filesDir = context.filesDir
            val messagesFile = File(filesDir, "messages.json")

            // 写入空的JSON对象
            FileWriter(messagesFile).use { writer ->
                writer.write("{}")
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 创建红包卡券页面的结构化JSON数据
     *
     * @param action 操作类型，默认为 "enter_coupons_page"
     * @param timestamp 时间戳，默认为当前时间
     * @param extraData 额外数据
     * @return JSON对象
     */
    fun createCouponsPageData(
        action: String = "enter_coupons_page",
        timestamp: Long = System.currentTimeMillis(),
        extraData: Map<String, Any>? = null
    ): JSONObject {
        return JSONObject().apply {
            put("action", action)
            put("page", "coupons")
            put("timestamp", timestamp)

            // 添加页面信息
            put("page_info", JSONObject().apply {
                put("title", "红包卡券")
                put("screen_name", "CouponsScreen")
            })

            // 添加额外数据
            extraData?.let { data ->
                put("extra_data", JSONObject(data))
            }
        }
    }
}

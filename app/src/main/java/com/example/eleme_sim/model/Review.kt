package com.example.eleme_sim.model

import java.util.Date

/**
 * 评价数据结构
 */
data class Review(
    val reviewId: String,
    val orderId: String,
    val userId: String,
    val restaurantId: String,
    val restaurantName: String,
    val rating: Int,                            // 评分（1-5星）
    val comment: String? = null,                // 评价内容
    val images: List<String> = emptyList(),     // 评价图片URL列表
    val tags: List<String> = emptyList(),       // 评价标签（如"味道好"、"送得快"等）
    val createTime: Date,
    val canDelete: Boolean = true               // 是否可以删除
) {
    /**
     * 判断是否为好评（4星及以上）
     */
    fun isPositive(): Boolean {
        return rating >= 4
    }

    /**
     * 判断是否为差评（2星及以下）
     */
    fun isNegative(): Boolean {
        return rating <= 2
    }
}

/**
 * 评价标签
 */
data class ReviewTag(
    val tagId: String,
    val name: String,
    val isPositive: Boolean = true              // 是否为正面标签
)

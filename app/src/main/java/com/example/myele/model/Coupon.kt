package com.example.myele.model

import java.util.Date

/**
 * 优惠券类型
 */
enum class CouponType {
    DISCOUNT,           // 折扣券
    REDUCTION,          // 满减券
    DELIVERY_FREE,      // 免配送费券
    NEW_USER,           // 新人券
    SPECIAL             // 特价券
}

/**
 * 优惠券状态
 */
enum class CouponStatus {
    AVAILABLE,          // 可用
    USED,               // 已使用
    EXPIRED,            // 已过期
    UNAVAILABLE         // 不可用（未达到使用条件）
}

/**
 * 优惠券数据结构
 */
data class Coupon(
    val couponId: String,
    val name: String,
    val type: CouponType,
    val discountAmount: Double? = null,         // 减免金额（满减券）
    val discountRate: Double? = null,           // 折扣率（折扣券，如0.8表示8折）
    val minOrderAmount: Double? = null,         // 最低使用金额
    val maxDiscountAmount: Double? = null,      // 最大优惠金额（折扣券时使用）
    val validFrom: Date,                        // 有效期开始时间
    val validUntil: Date,                       // 有效期结束时间
    val status: CouponStatus,
    val applicableRestaurants: List<String> = emptyList(), // 适用餐厅ID列表（空表示全部适用）
    val description: String? = null,            // 使用说明
    val isExpiringSoon: Boolean = false         // 是否即将过期（7天内）
) {
    /**
     * 判断优惠券是否可用于指定订单
     */
    fun isApplicableFor(restaurantId: String, orderAmount: Double): Boolean {
        if (status != CouponStatus.AVAILABLE) return false

        // 检查餐厅限制
        if (applicableRestaurants.isNotEmpty() && !applicableRestaurants.contains(restaurantId)) {
            return false
        }

        // 检查最低订单金额
        if (minOrderAmount != null && orderAmount < minOrderAmount) {
            return false
        }

        return true
    }

    /**
     * 计算优惠金额
     */
    fun calculateDiscount(orderAmount: Double): Double {
        return when (type) {
            CouponType.REDUCTION -> discountAmount ?: 0.0
            CouponType.DISCOUNT -> {
                val discount = orderAmount * (1 - (discountRate ?: 1.0))
                if (maxDiscountAmount != null) {
                    minOf(discount, maxDiscountAmount)
                } else {
                    discount
                }
            }
            CouponType.DELIVERY_FREE -> 0.0 // 配送费在其他地方处理
            else -> 0.0
        }
    }
}

/**
 * 超级吃货卡
 */
data class SuperFoodieCard(
    val cardId: String,
    val name: String,
    val price: Double,                          // 购买价格
    val validDays: Int,                         // 有效天数
    val monthlyDiscountLimit: Double,           // 每月优惠上限
    val benefits: List<String>,                 // 权益说明
    val isPurchased: Boolean = false,           // 是否已购买
    val purchaseDate: Date? = null,             // 购买日期
    val expiryDate: Date? = null                // 到期日期
)

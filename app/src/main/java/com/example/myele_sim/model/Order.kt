package com.example.myele_sim.model

import java.util.Date

/**
 * 订单状态枚举
 */
enum class OrderStatus {
    PENDING_ACCEPT,      // 待接单
    ACCEPTED,            // 已接单
    PREPARING,           // 制作中
    DELIVERING,          // 配送中
    COMPLETED,           // 已完成
    CANCELLED,           // 已取消
    PENDING_REVIEW       // 待评价
}

/**
 * 订单项（订单中的商品）
 */
data class OrderItem(
    val itemId: String,
    val productName: String,
    val productId: String,
    val quantity: Int,
    val price: Double,
    val specifications: String? = null,  // 商品规格（如大杯、中杯等）
    val hasBeef: Boolean = false         // 是否含有牛肉（用于食无忧理赔）
)

/**
 * 订单数据结构
 */
data class Order(
    val orderId: String,
    val restaurantId: String,
    val restaurantName: String,
    val status: OrderStatus,
    val orderTime: Date,
    val deliveryTime: Date? = null,
    val items: List<OrderItem>,
    val totalAmount: Double,              // 原价
    val discountAmount: Double = 0.0,     // 优惠金额
    val actualAmount: Double,             // 实付金额
    val deliveryAddress: Address,
    val deliveryFee: Double = 0.0,
    val packagingFee: Double = 0.0,
    val usedCouponId: String? = null,     // 使用的优惠券ID
    val riderPhone: String? = null,        // 骑手电话
    val merchantPhone: String? = null,     // 商家电话
    val hasReview: Boolean = false,        // 是否已评价
    val reviewId: String? = null,          // 评价ID
    val canCancel: Boolean = true,         // 是否可以取消
    val canApplyRefund: Boolean = false,   // 是否可以申请退款
    val canApplyInsurance: Boolean = false // 是否可以申请食无忧理赔
)

/**
 * 取消订单原因
 */
enum class CancelReason {
    WRONG_ORDER,         // 下错单了
    DONT_WANT_ANYMORE,   // 不想要了
    TOO_LONG_WAIT,       // 等待时间太长
    ADDRESS_WRONG,       // 地址填错了
    OTHER                // 其他原因
}

/**
 * 退款原因
 */
enum class RefundReason {
    QUALITY_ISSUE,       // 质量问题
    WRONG_ITEM,          // 送错商品
    MISSING_ITEM,        // 少送商品
    TASTE_BAD,           // 口味不好
    PACKAGE_DAMAGED,     // 包装破损
    OTHER                // 其他原因
}

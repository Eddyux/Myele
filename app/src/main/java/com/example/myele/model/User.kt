package com.example.myele.model

import java.util.Date

/**
 * 用户资料数据结构
 */
data class UserProfile(
    val userId: String,
    val nickname: String,
    val phone: String,
    val avatar: String? = null,                 // 头像URL
    val gender: Gender? = null,
    val birthday: Date? = null,
    val email: String? = null,
    val memberLevel: Int = 0,                   // 会员等级
    val points: Int = 0,                        // 积分
    val hasSuperFoodieCard: Boolean = false     // 是否拥有超级吃货卡
)

/**
 * 性别枚举
 */
enum class Gender {
    MALE,
    FEMALE,
    OTHER
}

/**
 * 钱包数据结构
 */
data class Wallet(
    val walletId: String,
    val userId: String,
    val balance: Double = 0.0,                  // 余额
    val coupons: List<Coupon> = emptyList(),    // 优惠券列表
    val superFoodieCard: SuperFoodieCard? = null, // 超级吃货卡
    val isPinPaymentEnabled: Boolean = false     // 是否开启免密支付
)

/**
 * 账单类型
 */
enum class BillType {
    ORDER,              // 订单
    REFUND,             // 退款
    RECHARGE,           // 充值
    COUPON              // 优惠券
}

/**
 * 账单数据结构
 */
data class Bill(
    val billId: String,
    val userId: String,
    val type: BillType,
    val amount: Double,
    val balance: Double,                        // 交易后余额
    val description: String,
    val relatedOrderId: String? = null,         // 关联订单ID
    val createTime: Date
)

/**
 * 搜索历史
 */
data class SearchHistory(
    val keyword: String,
    val searchTime: Date
)

/**
 * 消息通知设置
 */
data class NotificationSettings(
    val userId: String,
    val orderUpdates: Boolean = true,           // 订单状态更新
    val promotions: Boolean = true,             // 活动优惠
    val systemMessages: Boolean = true,         // 系统消息
    val deliveryUpdates: Boolean = true         // 配送进度
)

/**
 * 设置数据结构
 */
data class Settings(
    val userId: String,
    val notificationSettings: NotificationSettings,
    val isPinPaymentEnabled: Boolean = false,   // 免密支付
    val language: String = "zh-CN",             // 语言
    val receiveMarketingMessages: Boolean = true // 接收营销信息
)

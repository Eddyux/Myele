package com.example.myele_sim.model

/**
 * 餐厅特色标签
 */
enum class RestaurantFeature {
    SLOW_MUST_COMPENSATE,       // 慢必赔
    AFTER_SALES_WORRY_FREE,     // 售后无忧
    LOVE_MERCHANT,              // 爱心商家
    BRAND_MERCHANT,             // 品牌商家
    APPOINTMENT_AVAILABLE,      // 支持预约
    FENGNIAO_DELIVERY,          // 蜂鸟准时达
    SELF_PICKUP,                // 到店自取
    NEW_STORE,                  // 新店
    FOOD_SAFETY,                // 食无忧
    CROSS_DAY_BOOKING,          // 跨天预订
    ONLINE_INVOICE              // 线上开票
}

/**
 * 排序方式
 */
enum class SortType {
    DEFAULT,                 // 默认排序
    DELIVERY_FASTEST,        // 配送最快
    RATING_HIGHEST,          // 好评优先
    SALES_HIGHEST,           // 销量优先
    DISTANCE_NEAREST         // 距离最近
}

/**
 * 餐厅筛选条件
 */
data class RestaurantFilter(
    val keyword: String? = null,              // 搜索关键词
    val cuisineType: String? = null,          // 菜系类型（如川菜、湘菜等）
    val minRating: Double? = null,            // 最低评分（如4.5+）
    val priceRangeMin: Int? = null,           // 价格区间最小值
    val priceRangeMax: Int? = null,           // 价格区间最大值
    val maxDeliveryTime: Int? = null,         // 最大配送时间（分钟，如30min内）
    val features: List<RestaurantFeature> = emptyList(), // 商家特色
    val sortType: SortType = SortType.DEFAULT // 排序方式
)

/**
 * 餐厅数据结构
 */
data class Restaurant(
    val restaurantId: String,
    val name: String,
    val rating: Double,                       // 评分
    val salesVolume: Int,                     // 月销量
    val deliveryTime: Int,                    // 预计送达时间（分钟）
    val distance: Double,                     // 距离（公里）
    val minDeliveryAmount: Double,            // 起送价
    val deliveryFee: Double,                  // 配送费
    val averagePrice: Double = 30.0,          // 人均价（默认30元）
    val cuisineType: String,                  // 菜系类型
    val features: List<RestaurantFeature> = emptyList(), // 商家特色
    val products: List<Product> = emptyList(), // 商品列表
    val coupons: List<String> = emptyList(),   // 可用优惠券ID列表
    val phone: String = "",                    // 商家电话
    val address: String = "",                  // 商家地址
    val isFollowed: Boolean = false,           // 是否已关注
    val isFavorite: Boolean = false,           // 是否收藏
    val hasAppointment: Boolean = false,       // 是否支持预约
    // 优惠活动相关
    val hasFirstOrderDiscount: Boolean = false, // 首次光顾减
    val hasFullReduction: Boolean = false,      // 满减优惠
    val hasRedPacketReward: Boolean = false,    // 下单返红包
    val hasFreeDelivery: Boolean = false,       // 配送费优惠
    val hasSpecialOffer: Boolean = false        // 特价商品
)

package com.example.myele.model

/**
 * 任务类型枚举
 */
enum class TaskType {
    // 订单相关
    VIEW_ORDER_STATUS,              // 查看订单状态
    VIEW_ORDER_LIST,                // 查看订单列表
    VIEW_ORDER_DETAIL,              // 查看订单详情
    CANCEL_ORDER,                   // 取消订单
    REORDER,                        // 再来一单
    APPLY_REFUND,                   // 申请退款
    APPLY_INSURANCE,                // 申请食无忧理赔
    CONTACT_MERCHANT,               // 联系商家
    CONTACT_RIDER,                  // 联系骑手

    // 搜索和筛选相关
    SEARCH_RESTAURANT,              // 搜索餐厅
    FILTER_BY_RATING,               // 按评分筛选
    FILTER_BY_PRICE,                // 按价格筛选
    FILTER_BY_DELIVERY_TIME,        // 按配送时间筛选
    FILTER_BY_FEATURES,             // 按商家特色筛选
    SORT_BY_RATING,                 // 按好评排序
    SORT_BY_DELIVERY_SPEED,         // 按配送速度排序
    CLEAR_SEARCH_HISTORY,           // 清除搜索历史

    // 购物车相关
    ADD_TO_CART,                    // 加入购物车
    CLEAR_CART,                     // 清空购物车
    SELECT_ALL_CART,                // 全选购物车
    CHECKOUT,                       // 结算
    PLACE_ORDER,                    // 下单
    APPOINTMENT_ORDER,              // 预约下单

    // 优惠券相关
    VIEW_COUPONS,                   // 查看优惠券
    VIEW_EXPIRING_COUPONS,          // 查看即将过期的优惠券
    USE_COUPON,                     // 使用优惠券
    SELECT_BEST_COUPON,             // 选择最优优惠券
    PURCHASE_FOODIE_CARD,           // 购买超级吃货卡

    // 地址相关
    VIEW_ADDRESSES,                 // 查看地址
    ADD_ADDRESS,                    // 添加地址
    EDIT_ADDRESS,                   // 编辑地址
    SET_DEFAULT_ADDRESS,            // 设置默认地址

    // 个人中心相关
    VIEW_PROFILE,                   // 查看个人信息
    CHANGE_AVATAR,                  // 更换头像
    CHANGE_NICKNAME,                // 更换昵称
    CHANGE_PHONE,                   // 更换手机号
    VIEW_WALLET,                    // 查看钱包
    VIEW_BILL,                      // 查看账单
    VIEW_MONTH_BILL,                // 查看月账单
    VIEW_FOLLOWS,                   // 查看我的关注
    VIEW_FAVORITES,                 // 查看最近常点

    // 评价相关
    SUBMIT_REVIEW,                  // 提交评价
    DELETE_REVIEW,                  // 删除评价

    // 设置相关
    OPEN_SETTINGS,                  // 打开设置
    PAYMENT_SETTINGS,               // 支付设置
    ENABLE_PIN_PAYMENT,             // 开启免密支付
    NOTIFICATION_SETTINGS,          // 消息通知设置
    DISABLE_PROMOTION_NOTIFICATION, // 关闭活动优惠通知

    // 客服相关
    CONTACT_CUSTOMER_SERVICE,       // 联系客服

    // 其他
    REFRESH_PAGE,                   // 刷新页面
    SHARE_RESTAURANT                // 分享餐厅
}

/**
 * 任务状态
 */
enum class TaskStatus {
    NOT_STARTED,        // 未开始
    IN_PROGRESS,        // 进行中
    COMPLETED,          // 已完成
    FAILED              // 失败
}

/**
 * 任务数据结构
 */
data class Task(
    val taskId: Int,                            // 任务ID（对应任务清单序号）
    val description: String,                    // 任务描述（完整的任务说明）
    val type: TaskType,                         // 任务类型
    val status: TaskStatus = TaskStatus.NOT_STARTED,
    val requiredParams: TaskParams? = null,     // 任务所需参数
    val steps: List<TaskStep> = emptyList()     // 任务步骤
)

/**
 * 任务步骤
 */
data class TaskStep(
    val stepId: Int,
    val description: String,
    val action: TaskType,
    val isCompleted: Boolean = false
)

/**
 * 任务参数（用于存储执行任务所需的具体参数）
 */
data class TaskParams(
    // 搜索相关
    val searchKeyword: String? = null,

    // 筛选相关
    val cuisineType: String? = null,
    val minRating: Double? = null,
    val priceMin: Int? = null,
    val priceMax: Int? = null,
    val maxDeliveryTime: Int? = null,
    val features: List<RestaurantFeature>? = null,
    val sortType: SortType? = null,

    // 订单相关
    val orderStatus: OrderStatus? = null,
    val cancelReason: CancelReason? = null,
    val refundReason: RefundReason? = null,

    // 商品相关
    val productName: String? = null,
    val restaurantName: String? = null,

    // 评价相关
    val rating: Int? = null,
    val reviewComment: String? = null,

    // 联系相关
    val message: String? = null,

    // 地址相关
    val addressType: AddressType? = null,
    val detailAddress: String? = null,

    // 个人信息相关
    val newNickname: String? = null,
    val avatarPath: String? = null,

    // 账单相关
    val billViewType: String? = null,           // "day" 或 "month"

    // 设置相关
    val notificationType: String? = null,       // 要关闭的通知类型

    // 预约相关
    val appointmentTime: AppointmentTimeSlot? = null,

    // 分享相关
    val shareTarget: String? = null             // 分享目标（如"微信好友"）
)

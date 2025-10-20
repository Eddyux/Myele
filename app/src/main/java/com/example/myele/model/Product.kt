package com.example.myele.model

/**
 * 商品分类
 */
enum class ProductCategory {
    HOT_SALE,           // 热销
    NEW_ARRIVAL,        // 新品
    SIGNATURE,          // 招牌
    SPECIAL,            // 特色商品
    COMBO,              // 套餐
    BEVERAGE,           // 饮品
    MAIN_DISH,          // 主食
    SIDE_DISH,          // 小菜
    DESSERT             // 甜品
}

/**
 * 商品规格选项
 */
data class ProductSpecification(
    val specId: String,
    val name: String,           // 规格名称（如"大杯"、"中杯"、"小杯"）
    val price: Double,          // 该规格价格
    val isDefault: Boolean = false
)

/**
 * 商品数据结构
 */
data class Product(
    val productId: String,
    val name: String,
    val restaurantId: String,
    val restaurantName: String,
    val description: String? = null,
    val price: Double,                          // 基础价格
    val originalPrice: Double? = null,          // 原价（如果有折扣）
    val monthSales: Int = 0,                    // 月销量
    val rating: Double? = null,                 // 评分
    val imageUrl: String? = null,
    val category: ProductCategory? = null,
    val specifications: List<ProductSpecification> = emptyList(), // 可选规格
    val isAvailable: Boolean = true,            // 是否可售
    val tags: List<String> = emptyList()        // 标签（如"辣"、"新品"等）
)

/**
 * 购物车商品项
 */
data class CartItem(
    val cartItemId: String,
    val product: Product,
    val quantity: Int,
    val selectedSpecId: String? = null,         // 选择的规格ID
    val remarks: String? = null                 // 备注（如"少辣"、"不要香菜"）
) {
    /**
     * 计算该项的总价
     */
    fun getTotalPrice(): Double {
        val spec = product.specifications.find { it.specId == selectedSpecId }
        val unitPrice = spec?.price ?: product.price
        return unitPrice * quantity
    }
}

/**
 * 购物车数据结构
 */
data class ShoppingCart(
    val cartId: String,
    val restaurantId: String,
    val restaurantName: String,
    val items: List<CartItem> = emptyList(),
    val selectedCouponId: String? = null        // 选中的优惠券ID
) {
    /**
     * 计算商品总价
     */
    fun getSubtotal(): Double {
        return items.sumOf { it.getTotalPrice() }
    }

    /**
     * 获取商品总数量
     */
    fun getTotalQuantity(): Int {
        return items.sumOf { it.quantity }
    }

    /**
     * 判断购物车是否为空
     */
    fun isEmpty(): Boolean {
        return items.isEmpty()
    }
}

/**
 * 预约时间段
 */
data class AppointmentTimeSlot(
    val date: String,           // 日期（如"2024-01-01"）
    val timeRange: String       // 时间段（如"12:00-13:00"）
)

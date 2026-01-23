package com.example.eleme_sim.model

/**
 * 地址类型
 */
enum class AddressType {
    HOME,       // 家
    COMPANY,    // 公司
    SCHOOL,     // 学校
    OTHER       // 其他
}

/**
 * 地址数据结构
 */
data class Address(
    val addressId: String,
    val receiverName: String,               // 收货人姓名
    val receiverPhone: String,              // 收货人电话
    val street: String,                     // 街道
    val detailAddress: String,              // 详细地址（门牌号等）
    val addressType: AddressType,           // 地址类型
    val isDefault: Boolean = false,         // 是否默认地址
    val tag: String? = null,                // 地址标签（"家"、"公司"、"学校"）
    val yuwei: String? = null               // 余味信息
) {
    /**
     * 获取完整地址
     */
    fun getFullAddress(): String {
        return "$street $detailAddress"
    }

    /**
     * 获取简短地址（用于列表显示）
     */
    fun getShortAddress(): String {
        return "$street$detailAddress"
    }
}

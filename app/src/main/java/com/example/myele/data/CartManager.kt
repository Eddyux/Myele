package com.example.myele.data

import com.example.myele.model.Product

/**
 * 购物车管理器 - 单例模式
 * 用于在不同页面间共享购物车状态
 */
object CartManager {
    // 当前待结算的商品列表 (productId to quantity)
    private val checkoutItems = mutableMapOf<String, Int>()

    // 所有商品数据的引用
    private var allProducts: List<Product> = emptyList()

    // 记录是否全选（用于任务11检测）
    private var wasSelectAll: Boolean = false

    /**
     * 设置商品数据
     */
    fun setProducts(products: List<Product>) {
        allProducts = products
    }

    /**
     * 设置待结算商品
     * @param items Map of productId to quantity
     * @param selectAll 是否全选
     */
    fun setCheckoutItems(items: Map<String, Int>, selectAll: Boolean = false) {
        checkoutItems.clear()
        checkoutItems.putAll(items)
        wasSelectAll = selectAll
    }

    /**
     * 获取是否全选
     */
    fun wasSelectAll(): Boolean {
        return wasSelectAll
    }

    /**
     * 获取待结算商品列表
     */
    fun getCheckoutItems(): Map<String, Int> {
        return checkoutItems.toMap()
    }

    /**
     * 获取待结算商品的详细信息
     */
    fun getCheckoutProducts(): List<Pair<Product, Int>> {
        return checkoutItems.mapNotNull { (productId, quantity) ->
            allProducts.find { it.productId == productId }?.let { it to quantity }
        }
    }

    /**
     * 清空待结算商品
     */
    fun clearCheckout() {
        checkoutItems.clear()
    }

    /**
     * 计算商品小计
     */
    fun getSubtotal(): Double {
        return checkoutItems.entries.sumOf { (productId, quantity) ->
            val product = allProducts.find { it.productId == productId }
            (product?.price ?: 0.0) * quantity
        }
    }

    /**
     * 获取商品总数量
     */
    fun getTotalQuantity(): Int {
        return checkoutItems.values.sum()
    }
}

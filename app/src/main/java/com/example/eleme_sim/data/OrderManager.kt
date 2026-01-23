package com.example.eleme_sim.data

import com.example.eleme_sim.model.Order

/**
 * 订单管理器 - 管理运行时创建的订单
 * 仅在应用运行期间保存订单，重启后清空
 * 不会影响JSON文件中的原始订单数据
 */
object OrderManager {
    // 运行时订单列表
    private val runtimeOrders = mutableListOf<Order>()

    /**
     * 添加新订单
     */
    fun addOrder(order: Order) {
        runtimeOrders.add(0, order) // 添加到列表开头，最新的在前面
    }

    /**
     * 获取所有运行时订单
     */
    fun getRuntimeOrders(): List<Order> {
        return runtimeOrders.toList()
    }

    /**
     * 根据订单ID查找订单
     */
    fun getOrderById(orderId: String): Order? {
        return runtimeOrders.find { it.orderId == orderId }
    }

    /**
     * 清空所有运行时订单
     */
    fun clearOrders() {
        runtimeOrders.clear()
    }

    /**
     * 获取订单数量
     */
    fun getOrderCount(): Int {
        return runtimeOrders.size
    }
}

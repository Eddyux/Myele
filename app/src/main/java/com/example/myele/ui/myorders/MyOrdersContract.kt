package com.example.myele.ui.myorders

import com.example.myele.model.Order

/**
 * MyOrders页面的MVP契约接口
 */
interface MyOrdersContract {
    /**
     * View接口 - 定义View层需要实现的方法
     */
    interface View {
        fun showOrders(orders: List<Order>)
        fun showMonthlyExpense(amount: Double)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    /**
     * Presenter接口 - 定义Presenter层需要实现的方法
     */
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadOrders(filter: String) // "全部", "待收货/使用", "待评价", "退款"
        fun searchOrders(keyword: String)
    }
}

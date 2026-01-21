package com.example.myele_sim.ui.myorders

import com.example.myele_sim.data.DataRepository
import com.example.myele_sim.model.Order
import com.example.myele_sim.model.OrderStatus
import java.util.*

/**
 * MyOrders页面的Presenter实现
 */
class MyOrdersPresenter(private val repository: DataRepository) : MyOrdersContract.Presenter {
    private var view: MyOrdersContract.View? = null
    private var allOrders: List<Order> = emptyList()

    override fun attachView(view: MyOrdersContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadOrders(filter: String) {
        view?.showLoading()

        // 获取所有订单：合并JSON中的原始订单和运行时创建的订单
        val originalOrders = repository.getOrders()
        val runtimeOrders = com.example.myele_sim.data.OrderManager.getRuntimeOrders()
        allOrders = runtimeOrders + originalOrders // 运行时订单在前面

        // 根据筛选条件过滤订单
        val filteredOrders = when (filter) {
            "全部" -> allOrders
            "待收货/使用" -> allOrders.filter {
                it.status == OrderStatus.ACCEPTED ||
                        it.status == OrderStatus.PREPARING ||
                        it.status == OrderStatus.DELIVERING
            }
            "待评价" -> allOrders.filter {
                it.status == OrderStatus.PENDING_REVIEW ||
                        (it.status == OrderStatus.COMPLETED && !it.hasReview)
            }
            "退款" -> allOrders.filter { it.status == OrderStatus.CANCELLED }
            else -> allOrders
        }

        view?.showOrders(filteredOrders)

        // 计算本月支出
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val monthlyExpense = allOrders
            .filter {
                val orderMonth = Calendar.getInstance().apply {
                    time = it.orderTime
                }.get(Calendar.MONTH)
                orderMonth == currentMonth && it.status != OrderStatus.CANCELLED
            }
            .sumOf { it.actualAmount }

        view?.showMonthlyExpense(monthlyExpense)
        view?.hideLoading()
    }

    override fun searchOrders(keyword: String) {
        val searchResults = allOrders.filter {
            it.restaurantName.contains(keyword, ignoreCase = true) ||
                    it.items.any { item -> item.productName.contains(keyword, ignoreCase = true) }
        }
        view?.showOrders(searchResults)
    }
}

package com.example.myele.navigation

/**
 * 屏幕路由定义
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object SearchResult : Screen("search_result/{keyword}") {
        fun createRoute(keyword: String) = "search_result/$keyword"
    }
    object Takeout : Screen("takeout")
    object Checkout : Screen("checkout")
    object Messages : Screen("messages")
    object ShoppingCart : Screen("shopping_cart")
    object Profile : Screen("profile")

    // Profile子页面
    object FoodieCard : Screen("foodie_card")
    object MyOrders : Screen("my_orders")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }
    object OnlineChat : Screen("online_chat")
    object Coupons : Screen("coupons")
    object Reviews : Screen("reviews")
    object MyBills : Screen("my_bills")
    object MyAddresses : Screen("my_addresses")
    object MyFollows : Screen("my_follows")
    object FrequentStores : Screen("frequent_stores")
    object CustomerService : Screen("customer_service")
    object Settings : Screen("settings")
    object PaymentSettings : Screen("payment_settings")
    object NotificationSettings : Screen("notification_settings")
    object MyInfo : Screen("my_info")
    object ChangePhone : Screen("change_phone")
}

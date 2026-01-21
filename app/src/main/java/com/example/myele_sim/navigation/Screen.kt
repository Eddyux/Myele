package com.example.myele_sim.navigation

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
    object PaymentSuccess : Screen("payment_success/{orderId}/{amount}/{paymentMethod}") {
        fun createRoute(orderId: String, amount: Double, paymentMethod: String) =
            "payment_success/$orderId/$amount/$paymentMethod"
    }
    object StorePage : Screen("store_page/{restaurantId}?searchKey={searchKey}") {
        fun createRoute(restaurantId: String, searchKey: String? = null) =
            if (searchKey != null) "store_page/$restaurantId?searchKey=$searchKey"
            else "store_page/$restaurantId"
    }
    object Messages : Screen("messages")
    object MessageDetail : Screen("message_detail/{riderName}") {
        fun createRoute(riderName: String) = "message_detail/$riderName"
    }
    object MerchantChat : Screen("merchant_chat")
    object ShoppingCart : Screen("shopping_cart")
    object Profile : Screen("profile")

    // Profile子页面
    object FoodieCard : Screen("foodie_card")
    object MyOrders : Screen("my_orders")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }
    object OnlineChat : Screen("online_chat?orderStatus={orderStatus}") {
        fun createRoute(orderStatus: String? = null) =
            if (orderStatus != null) "online_chat?orderStatus=$orderStatus"
            else "online_chat"
    }
    object Coupons : Screen("coupons")
    object Reviews : Screen("reviews")
    object MyBills : Screen("my_bills")
    object MyAddresses : Screen("my_addresses")
    object AddressEdit : Screen("address_edit?addressId={addressId}") {
        fun createRoute(addressId: String? = null) =
            if (addressId != null) "address_edit?addressId=$addressId" else "address_edit"
    }
    object FoodInsurance : Screen("food_insurance/{orderId}") {
        fun createRoute(orderId: String) = "food_insurance/$orderId"
    }
    object MyFollows : Screen("my_follows")
    object FrequentStores : Screen("frequent_stores")
    object CustomerService : Screen("customer_service")
    object MyKefu : Screen("my_kefu")
    object ServiceProgress : Screen("service_progress")
    object OrderRewards : Screen("order_rewards")
    object Settings : Screen("settings")
    object PaymentSettings : Screen("payment_settings")
    object NotificationSettings : Screen("notification_settings")
    object MyInfo : Screen("my_info")
    object ChangePhone : Screen("change_phone")

    // 通用服务页面
    object ServicePage : Screen("service_page/{serviceName}") {
        fun createRoute(serviceName: String) = "service_page/$serviceName"
    }

    // 通用未开发页面
    object Undeveloped : Screen("undeveloped/{title}") {
        fun createRoute(title: String) = "undeveloped/$title"
    }
}

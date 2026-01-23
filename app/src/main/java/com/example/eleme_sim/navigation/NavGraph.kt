package com.example.eleme_sim.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.eleme_sim.data.DataRepository
import com.example.eleme_sim.ui.home.HomeScreen
import com.example.eleme_sim.ui.search.SearchScreen
import com.example.eleme_sim.ui.searchresult.SearchResultScreen
import com.example.eleme_sim.ui.takeout.TakeoutScreen
import com.example.eleme_sim.ui.messages.MessagesScreen
import com.example.eleme_sim.ui.messagedetail.MessageDetailScreen
import com.example.eleme_sim.ui.merchantchat.MerchantChatScreen
import com.example.eleme_sim.ui.shoppingcart.ShoppingCartScreen
import com.example.eleme_sim.ui.profile.ProfileScreen
import com.example.eleme_sim.ui.checkout.CheckoutScreen
import com.example.eleme_sim.ui.paymentsuccess.PaymentSuccessScreen
import com.example.eleme_sim.ui.foodiecard.FoodieCardScreen
import com.example.eleme_sim.ui.myorders.MyOrdersScreen
import com.example.eleme_sim.ui.orderdetail.OrderDetailScreen
import com.example.eleme_sim.ui.foodinsurance.FoodInsuranceScreen
import com.example.eleme_sim.ui.onlinechat.OnlineChatScreen
import com.example.eleme_sim.ui.coupons.CouponsScreen
import com.example.eleme_sim.ui.reviews.ReviewsScreen
import com.example.eleme_sim.ui.mybills.MyBillsScreen
import com.example.eleme_sim.ui.myaddresses.MyAddressesScreen
import com.example.eleme_sim.ui.myaddresses.AddressEditScreen
import com.example.eleme_sim.ui.myfollows.MyFollowsScreen
import com.example.eleme_sim.ui.frequentstores.FrequentStoresScreen
import com.example.eleme_sim.ui.customerservice.CustomerServiceScreen
import com.example.eleme_sim.ui.mykefu.MyKefuScreen
import com.example.eleme_sim.ui.serviceprogress.ServiceProgressScreen
import com.example.eleme_sim.ui.profile.OrderRewardsScreen
import com.example.eleme_sim.ui.settings.SettingsScreen
import com.example.eleme_sim.ui.paymentsettings.PaymentSettingsScreen
import com.example.eleme_sim.ui.notificationsettings.NotificationSettingsScreen
import com.example.eleme_sim.ui.myinfo.MyInfoScreen
import com.example.eleme_sim.ui.changephone.ChangePhoneScreen
import com.example.eleme_sim.ui.storepage.StorePageScreen
import com.example.eleme_sim.ui.servicepage.ServicePageScreen
import com.example.eleme_sim.ui.undeveloped.UndevelopedScreen

/**
 * 导航图
 */
@Composable
fun NavGraph(navController: NavHostController, repository: DataRepository) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Search.route) {
            SearchScreen(navController)
        }

        composable(
            route = Screen.SearchResult.route,
            arguments = listOf(navArgument("keyword") { type = NavType.StringType })
        ) { backStackEntry ->
            val keyword = backStackEntry.arguments?.getString("keyword") ?: ""
            SearchResultScreen(navController, keyword)
        }

        composable(Screen.Takeout.route) {
            TakeoutScreen(navController)
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(navController, repository)
        }

        composable(
            route = Screen.PaymentSuccess.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType },
                navArgument("paymentMethod") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
            val paymentMethod = backStackEntry.arguments?.getString("paymentMethod") ?: "微信支付"
            PaymentSuccessScreen(navController, orderId, amount, paymentMethod)
        }

        composable(
            route = Screen.StorePage.route,
            arguments = listOf(
                navArgument("restaurantId") { type = NavType.StringType },
                navArgument("searchKey") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            val searchKey = backStackEntry.arguments?.getString("searchKey")
            StorePageScreen(navController, restaurantId, searchKey)
        }

        composable(Screen.Messages.route) {
            MessagesScreen(navController)
        }

        composable(
            route = Screen.MessageDetail.route,
            arguments = listOf(navArgument("riderName") { type = NavType.StringType })
        ) { backStackEntry ->
            val riderName = backStackEntry.arguments?.getString("riderName") ?: "骑手"
            MessageDetailScreen(navController, riderName)
        }

        composable(Screen.MerchantChat.route) {
            MerchantChatScreen(navController)
        }

        composable(Screen.ShoppingCart.route) {
            ShoppingCartScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // Profile子页面
        composable(Screen.FoodieCard.route) {
            FoodieCardScreen(navController, repository)
        }

        composable(Screen.MyOrders.route) {
            MyOrdersScreen(navController, repository)
        }

        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailScreen(navController, repository, orderId)
        }

        composable(
            route = Screen.FoodInsurance.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            FoodInsuranceScreen(navController, repository, orderId)
        }

        composable(
            route = Screen.OnlineChat.route,
            arguments = listOf(
                navArgument("orderStatus") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "配送中"
                }
            )
        ) { backStackEntry ->
            val orderStatus = backStackEntry.arguments?.getString("orderStatus") ?: "配送中"
            OnlineChatScreen(navController, orderStatus = orderStatus)
        }

        composable(Screen.Coupons.route) {
            CouponsScreen(navController, repository)
        }

        composable(Screen.Reviews.route) {
            ReviewsScreen(navController, repository)
        }

        composable(Screen.MyBills.route) {
            MyBillsScreen(navController, repository)
        }

        composable(Screen.MyAddresses.route) {
            MyAddressesScreen(navController, repository)
        }

        composable(
            route = Screen.AddressEdit.route,
            arguments = listOf(
                navArgument("addressId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val addressId = backStackEntry.arguments?.getString("addressId")
            AddressEditScreen(navController, repository, addressId)
        }

        composable(Screen.MyFollows.route) {
            MyFollowsScreen(navController, repository)
        }

        composable(Screen.FrequentStores.route) {
            FrequentStoresScreen(navController, repository)
        }

        composable(Screen.CustomerService.route) {
            CustomerServiceScreen(navController, repository)
        }

        composable(Screen.MyKefu.route) {
            MyKefuScreen(navController)
        }

        composable(Screen.ServiceProgress.route) {
            ServiceProgressScreen(navController)
        }

        composable(Screen.OrderRewards.route) {
            OrderRewardsScreen(navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }

        composable(Screen.PaymentSettings.route) {
            PaymentSettingsScreen(navController)
        }

        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(navController)
        }

        composable(Screen.MyInfo.route) {
            MyInfoScreen(navController)
        }

        composable(Screen.ChangePhone.route) {
            ChangePhoneScreen(navController)
        }

        // 通用服务页面
        composable(
            route = Screen.ServicePage.route,
            arguments = listOf(navArgument("serviceName") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
            ServicePageScreen(navController, serviceName)
        }

        composable(
            route = Screen.Undeveloped.route,
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "功能页"
            UndevelopedScreen(navController, title)
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$title 页面",
            textAlign = TextAlign.Center
        )
    }
}

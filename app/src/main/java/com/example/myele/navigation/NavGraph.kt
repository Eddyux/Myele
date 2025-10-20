package com.example.myele.navigation

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
import com.example.myele.data.DataRepository
import com.example.myele.ui.home.HomeScreen
import com.example.myele.ui.search.SearchScreen
import com.example.myele.ui.searchresult.SearchResultScreen
import com.example.myele.ui.takeout.TakeoutScreen
import com.example.myele.ui.messages.MessagesScreen
import com.example.myele.ui.shoppingcart.ShoppingCartScreen
import com.example.myele.ui.profile.ProfileScreen
import com.example.myele.ui.checkout.CheckoutScreen
import com.example.myele.ui.foodiecard.FoodieCardScreen
import com.example.myele.ui.myorders.MyOrdersScreen
import com.example.myele.ui.orderdetail.OrderDetailScreen
import com.example.myele.ui.onlinechat.OnlineChatScreen
import com.example.myele.ui.coupons.CouponsScreen
import com.example.myele.ui.reviews.ReviewsScreen
import com.example.myele.ui.mybills.MyBillsScreen
import com.example.myele.ui.myaddresses.MyAddressesScreen

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
            CheckoutScreen(navController)
        }

        composable(Screen.Messages.route) {
            MessagesScreen(navController)
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

        composable(Screen.OnlineChat.route) {
            OnlineChatScreen(navController)
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

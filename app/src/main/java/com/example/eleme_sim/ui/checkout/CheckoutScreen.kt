package com.example.eleme_sim.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.eleme_sim.data.CartManager
import com.example.eleme_sim.data.DataRepository
import com.example.eleme_sim.model.Coupon
import com.example.eleme_sim.model.CouponStatus
import com.example.eleme_sim.navigation.Screen
import com.example.eleme_sim.utils.ActionLogger
import com.example.eleme_sim.data.OrderManager
import com.example.eleme_sim.model.Order
import com.example.eleme_sim.model.OrderItem
import com.example.eleme_sim.model.OrderStatus
import java.util.Date

@Composable
fun CheckoutScreen(navController: NavController, repository: DataRepository) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var deliveryMethod by remember { mutableStateOf("外卖配送") }
    var deliveryTimeType by remember { mutableStateOf("立即送出") }
    var selectedDeliveryDate by remember { mutableStateOf("今日") }
    var selectedDeliveryTimeSlot by remember { mutableStateOf("") }
    var showDeliveryTimeDialog by remember { mutableStateOf(false) }
    var showCouponDialog by remember { mutableStateOf(false) }
    var selectedCoupon by remember { mutableStateOf<Coupon?>(null) }
    var paymentMethod by remember { mutableStateOf("微信支付") }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }

    val addresses = remember { repository.getAddresses() }
    var selectedAddress by remember { mutableStateOf(addresses.firstOrNull()) }

    val checkoutProducts = remember { CartManager.getCheckoutProducts() }
    val subtotal = remember { CartManager.getSubtotal() }
    val productsByRestaurant = remember { checkoutProducts.groupBy { it.first.restaurantName } }
    val restaurantIds = remember { checkoutProducts.map { it.first.restaurantId }.distinct() }

    val allCoupons = remember { repository.getCoupons() }
    val deliveryFee = 5.0
    val availableCoupons = remember(subtotal, restaurantIds) {
        allCoupons.filter { coupon ->
            coupon.status == CouponStatus.AVAILABLE &&
                (coupon.applicableRestaurants.isEmpty() ||
                    restaurantIds.any { it in coupon.applicableRestaurants }) &&
                (coupon.minOrderAmount == null || subtotal >= (coupon.minOrderAmount ?: 0.0))
        }.sortedByDescending {
            it.calculateDiscount(subtotal, deliveryFee)
        }
    }

    val couponDiscount = selectedCoupon?.calculateDiscount(subtotal, deliveryFee) ?: 0.0

    LaunchedEffect(Unit) {
        val pageInfoMap = mutableMapOf<String, Any>(
            "total_amount" to subtotal,
            "product_count" to checkoutProducts.size
        )

        val searchKey = CartManager.getSearchKeyword()
        if (searchKey != null) {
            pageInfoMap["search_query"] = searchKey
        }

        ActionLogger.logAction(
            context = context,
            action = "enter_checkout",
            page = "checkout",
            pageInfo = pageInfoMap
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5F7))
    ) {
        TopBar(onBackClicked = { navController.popBackStack() })

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 20.dp)
        ) {
            item {
                DeliveryMethodSection(
                    selectedMethod = deliveryMethod,
                    onMethodChanged = { deliveryMethod = it }
                )
            }

            item {
                AddressSection(
                    selectedAddress = selectedAddress,
                    onClick = { showAddressDialog = true }
                )
            }

            item {
                DeliveryTimeSection(
                    selectedTimeType = deliveryTimeType,
                    selectedDate = selectedDeliveryDate,
                    selectedTimeSlot = selectedDeliveryTimeSlot,
                    onImmediateClicked = {
                        deliveryTimeType = "立即送出"
                        selectedDeliveryDate = "今日"
                        selectedDeliveryTimeSlot = ""
                    },
                    onScheduleClicked = { showDeliveryTimeDialog = true }
                )
            }

            items(productsByRestaurant.toList(), key = { it.first }) { (restaurantName, products) ->
                OrderItemsSection(
                    restaurantName = restaurantName,
                    products = products
                )
            }

            item {
                CouponSelectionSection(
                    selectedCoupon = selectedCoupon,
                    onClick = { showCouponDialog = true }
                )
            }

            item {
                OrderFeesSection(
                    subtotal = subtotal,
                    couponDiscount = couponDiscount
                )
            }

            item {
                OrderOptionsSection()
            }

            item {
                PaymentMethodSection(
                    selectedMethod = paymentMethod,
                    onClick = { showPaymentDialog = true }
                )
            }
        }

        BottomSubmitBar(
            total = subtotal + 2.0 + 5.0 - couponDiscount,
            onSubmit = {
                val orderId = "order_${System.currentTimeMillis()}"
                val totalAmount = subtotal + 2.0 + 5.0 - couponDiscount

                val searchKey = CartManager.getSearchKeyword()
                val hasKFCProducts = checkoutProducts.any { it.first.restaurantName.contains("肯德基") }

                if (hasKFCProducts && searchKey != null) {
                    val usedCoupon = selectedCoupon != null
                    val selectedMaxCoupon = if (availableCoupons.isNotEmpty() && selectedCoupon != null) {
                        selectedCoupon == availableCoupons.first()
                    } else {
                        false
                    }

                    ActionLogger.logAction(
                        context = context,
                        action = "complete_order",
                        page = "checkout",
                        pageInfo = mapOf("restaurant" to "肯德基"),
                        extraData = mapOf(
                            "search_query" to searchKey,
                            "added_to_cart" to true,
                            "used_coupon" to usedCoupon,
                            "selected_max_coupon" to selectedMaxCoupon,
                            "payment_success" to true
                        )
                    )
                }

                val fromPage = CartManager.getFromPage()
                if (fromPage != null && !(hasKFCProducts && searchKey != null)) {
                    val extraDataMap = mutableMapOf<String, Any>(
                        "from_page" to fromPage,
                        "payment_success" to true
                    )

                    selectedAddress?.let { address ->
                        extraDataMap["delivery_address_name"] = address.receiverName
                        extraDataMap["delivery_address_phone"] = address.receiverPhone
                        extraDataMap["delivery_address_street"] = address.street
                        extraDataMap["delivery_address_detail"] = address.detailAddress
                        extraDataMap["delivery_address_full"] = address.getFullAddress()
                        address.yuwei?.let { extraDataMap["delivery_address_yuwei"] = it }
                    }

                    if (deliveryTimeType == "预约配送") {
                        extraDataMap["delivery_type"] = "scheduled"
                        extraDataMap["delivery_date"] = selectedDeliveryDate
                        extraDataMap["delivery_time_slot"] = selectedDeliveryTimeSlot
                    } else {
                        extraDataMap["delivery_type"] = "immediate"
                    }

                    ActionLogger.logAction(
                        context = context,
                        action = "complete_order",
                        page = "checkout",
                        pageInfo = mapOf(),
                        extraData = extraDataMap
                    )
                }

                selectedAddress?.let { address ->
                    val orderItems = checkoutProducts.map { (product, quantity) ->
                        OrderItem(
                            itemId = "item_${System.currentTimeMillis()}_${product.productId}",
                            productName = product.name,
                            productId = product.productId,
                            quantity = quantity,
                            price = product.price,
                            specifications = null
                        )
                    }

                    val firstProduct = checkoutProducts.firstOrNull()?.first
                    if (firstProduct != null) {
                        val order = Order(
                            orderId = orderId,
                            restaurantId = firstProduct.restaurantId,
                            restaurantName = firstProduct.restaurantName,
                            status = OrderStatus.PENDING_ACCEPT,
                            orderTime = Date(),
                            items = orderItems,
                            totalAmount = subtotal,
                            discountAmount = couponDiscount,
                            actualAmount = totalAmount,
                            deliveryAddress = address,
                            deliveryFee = 5.0,
                            packagingFee = 2.0,
                            usedCouponId = selectedCoupon?.couponId,
                            canCancel = true
                        )

                        OrderManager.addOrder(order)
                    }
                }

                navController.navigate(
                    Screen.PaymentSuccess.createRoute(
                        orderId = orderId,
                        amount = totalAmount,
                        paymentMethod = paymentMethod
                    )
                )
            }
        )
    }

    if (showCouponDialog) {
        CouponSelectionDialog(
            availableCoupons = availableCoupons,
            selectedCoupon = selectedCoupon,
            subtotal = subtotal,
            onCouponSelected = { coupon ->
                selectedCoupon = coupon
                showCouponDialog = false
            },
            onDismiss = { showCouponDialog = false }
        )
    }

    if (showPaymentDialog) {
        PaymentMethodDialog(
            selectedMethod = paymentMethod,
            onMethodSelected = { method ->
                paymentMethod = method
                showPaymentDialog = false
            },
            onDismiss = { showPaymentDialog = false }
        )
    }

    if (showAddressDialog) {
        AddressSelectionDialog(
            addresses = addresses,
            selectedAddress = selectedAddress,
            onAddressSelected = { address ->
                selectedAddress = address
                showAddressDialog = false
                ActionLogger.logAction(
                    context = context,
                    action = "change_delivery_address",
                    page = "checkout",
                    pageInfo = mapOf(
                        "address_id" to address.addressId,
                        "address_name" to address.receiverName,
                        "detailed_address" to address.detailAddress
                    )
                )
            },
            onDismiss = { showAddressDialog = false }
        )
    }

    if (showDeliveryTimeDialog) {
        DeliveryTimeSelectionDialog(
            onTimeSelected = { date, timeSlot ->
                deliveryTimeType = "预约配送"
                selectedDeliveryDate = date
                selectedDeliveryTimeSlot = timeSlot
                showDeliveryTimeDialog = false

                ActionLogger.logAction(
                    context = context,
                    action = "select_scheduled_delivery",
                    page = "checkout",
                    pageInfo = mapOf(
                        "delivery_date" to date,
                        "delivery_time_slot" to timeSlot
                    )
                )
            },
            onDismiss = { showDeliveryTimeDialog = false }
        )
    }
}

package com.example.eleme_sim.ui.mykefu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.eleme_sim.model.Order

@Composable
fun MyKefuScreen(navController: NavController) {
    var showOrderSelector by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    var inputText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFBFE7FF),
                        Color(0xFFE9F6FF),
                        Color(0xFFF7FBFF)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(onBackClicked = { navController.popBackStack() })
            ServiceBusyTip()
            SmartServiceGreeting()

            if (selectedOrder != null) {
                SelectedOrderSection(
                    order = selectedOrder!!,
                    onOrderChange = { showOrderSelector = true }
                )
            } else {
                OrderSuggestionSection(onOrderSelect = { showOrderSelector = true })
            }

            ChatMessagesSection()
            Spacer(modifier = Modifier.weight(1f))
            InputSection(
                inputText = inputText,
                onInputChange = { inputText = it }
            )
        }

        if (showOrderSelector) {
            OrderSelectorDialog(
                onDismiss = { showOrderSelector = false },
                onOrderSelected = { order ->
                    selectedOrder = order
                    showOrderSelector = false
                }
            )
        }
    }
}

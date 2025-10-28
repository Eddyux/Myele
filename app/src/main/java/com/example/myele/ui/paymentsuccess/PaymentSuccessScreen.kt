package com.example.myele.ui.paymentsuccess

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.navigation.Screen
import kotlinx.coroutines.delay

/**
 * 支付成功页面
 * 显示支付成功信息，然后自动跳转到我的订单页面
 */
@Composable
fun PaymentSuccessScreen(
    navController: NavController,
    orderId: String,
    amount: Double,
    paymentMethod: String = "微信支付"
) {
    // 3秒后自动跳转到我的订单页面
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate(Screen.MyOrders.route) {
            popUpTo(Screen.Home.route) {
                inclusive = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // 绿色圆圈图标，里面有白色勾
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Color(0xFF4CAF50)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // "支付成功" 文字
            Text(
                text = "支付成功",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 红色的金额显示
            Text(
                text = "¥%.2f".format(amount),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF3333)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // "微信支付"文字
            Text(
                text = paymentMethod,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 提示文字
            Text(
                text = "3秒后自动跳转到我的订单...",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 立即查看按钮
            Button(
                onClick = {
                    navController.navigate(Screen.MyOrders.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BFFF)
                ),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("立即查看订单", fontSize = 16.sp)
            }
        }
    }
}

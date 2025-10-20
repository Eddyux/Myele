package com.example.myele.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CheckoutScreen(navController: NavController) {
    var deliveryMethod by remember { mutableStateOf("外卖配送") }
    var deliveryTime by remember { mutableStateOf("立即送出") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部标题栏
        TopBar(onBackClicked = { navController.popBackStack() })

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // 配送方式选择
            item {
                DeliveryMethodSection(
                    selectedMethod = deliveryMethod,
                    onMethodChanged = { deliveryMethod = it }
                )
            }

            // 收货地址
            item {
                AddressSection()
            }

            // 配送时间
            item {
                DeliveryTimeSection(
                    selectedTime = deliveryTime,
                    onTimeChanged = { deliveryTime = it }
                )
            }

            // 订单商品
            item {
                OrderItemsSection()
            }

            // 订单费用明细
            item {
                OrderFeesSection()
            }

            // 备注和餐具
            item {
                OrderOptionsSection()
            }

            // 支付方式
            item {
                PaymentMethodSection()
            }
        }

        // 底部提交订单
        BottomSubmitBar()
    }
}

@Composable
fun TopBar(onBackClicked: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.Black
                )
            }
            Text(
                text = "确认订单",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DeliveryMethodSection(selectedMethod: String, onMethodChanged: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("外卖配送", "到店自取").forEach { method ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (selectedMethod == method) Color(0xFF00BFFF) else Color(0xFFF5F5F5),
                    modifier = Modifier.clickable { onMethodChanged(method) }
                ) {
                    Text(
                        text = method,
                        fontSize = 14.sp,
                        color = if (selectedMethod == method) Color.White else Color.Black,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddressSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFF00BFFF),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "华中师范大学元宝山公寓二期六栋",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "于骁 138****8888",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun DeliveryTimeSection(selectedTime: String, onTimeChanged: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("配送时间", fontSize = 14.sp, color = Color.Black)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(selectedTime, fontSize = 14.sp, color = Color(0xFF00BFFF))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun OrderItemsSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "箐筵·荷叶烤鸡(理工大店)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 商品项
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("招牌荷叶烤鸡 x1", fontSize = 14.sp, color = Color.Black)
                Text("¥38.0", fontSize = 14.sp, color = Color.Black)
            }
        }
    }
}

@Composable
fun OrderFeesSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FeeItem("商品总额", "¥38.0")
            FeeItem("打包费", "¥2.0")
            FeeItem("配送费", "¥5.0")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("小计", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("¥45.0", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF3366))
            }
        }
    }
}

@Composable
fun FeeItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun OrderOptionsSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OptionItem("备注", "口味、偏好等要求")
            Divider()
            OptionItem("餐具", "不需要餐具")
            Divider()
            OptionItem("发票", "不需要发票")
        }
    }
}

@Composable
fun OptionItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = Color.Black)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, fontSize = 14.sp, color = Color.Gray)
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun PaymentMethodSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("支付方式", fontSize = 14.sp, color = Color.Black)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("支付宝", fontSize = 14.sp, color = Color(0xFF00BFFF))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BottomSubmitBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("合计:", fontSize = 12.sp, color = Color.Gray)
                Text(
                    "¥45.0",
                    fontSize = 20.sp,
                    color = Color(0xFFFF3366),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3366)),
                modifier = Modifier.height(48.dp)
            ) {
                Text("提交订单", fontSize = 16.sp)
            }
        }
    }
}

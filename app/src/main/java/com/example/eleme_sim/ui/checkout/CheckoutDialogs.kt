package com.example.eleme_sim.ui.checkout

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CouponSelectionDialog(
    availableCoupons: List<com.example.eleme_sim.model.Coupon>,
    selectedCoupon: com.example.eleme_sim.model.Coupon?,
    subtotal: Double,
    onCouponSelected: (com.example.eleme_sim.model.Coupon?) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF5F5F5),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
                Text("选择饿了么红包", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("兑换码", fontSize = 14.sp, color = Color(0xFF00BFFF))
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            ) {
                item {
                    Text(
                        "饿了么红包  可选1张",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(availableCoupons.size) { index ->
                    val coupon = availableCoupons[index]
                    CouponItem(
                        coupon = coupon,
                        isSelected = coupon.couponId == selectedCoupon?.couponId,
                        subtotal = subtotal,
                        isRecommended = index == 0,
                        onSelect = { onCouponSelected(coupon) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // 不想爆涨选项
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCouponSelected(null) },
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
                            Text("不想爆涨?可直接使用", fontSize = 14.sp, color = Color.Gray)
                            RadioButton(
                                selected = selectedCoupon == null,
                                onClick = { onCouponSelected(null) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (selectedCoupon != null) {
                        val deliveryFee = 5.0
                        "已选1张,可减 ¥%.0f".format(selectedCoupon.calculateDiscount(subtotal, deliveryFee))
                    } else {
                        "确定"
                    },
                    fontSize = 16.sp
                )
            }
        }
    )
}

@Composable
fun PaymentMethodDialog(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text("选择支付方式", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("微信支付", "支付宝").forEach { method ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMethodSelected(method) },
                        color = if (selectedMethod == method) Color(0xFFF0F8FF) else Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = method,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            if (selectedMethod == method) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF00BFFF)
                                )
                            }
                        }
                    }
                    if (method != "支付宝") {
                        Divider()
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
            ) {
                Text("确定")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryTimeSelectionDialog(
    onTimeSelected: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf("今日(周二)") }
    var selectedTimeSlot by remember { mutableStateOf("") }

    // 生成日期列表
    val dateList = remember {
        listOf(
            "今日(周二)",
            "明日(周三)",
            "11-06(周四)",
            "11-07(周五)",
            "11-08(周六)",
            "11-09(周日)",
            "11-10(周一)"
        )
    }

    // 生成所有时间段列表(今日+明日及之后的时间段)
    val allTimeSlots = remember {
        listOf(
            // 今日时间段
            "今日" to listOf(
                "尽快送达" to "1元配送费",
                "22:25-22:45" to "1元配送费",
                "22:45-23:05" to "1元配送费",
                "23:05-23:25" to "1元配送费",
                "23:25-23:45" to "1元配送费"
            ),
            // 明日及以后的时间段
            "明日" to listOf(
                "07:40-08:00" to "0元配送费",
                "08:00-08:20" to "0元配送费",
                "08:20-08:40" to "0元配送费",
                "08:40-09:00" to "0元配送费",
                "11:00-11:20" to "1元配送费",
                "11:20-11:40" to "1元配送费",
                "11:40-12:00" to "1元配送费",
                "12:00-12:20" to "1元配送费",
                "12:20-12:40" to "1元配送费",
                "12:40-13:00" to "1元配送费"
            )
        )
    }

    val timeSlots = remember(selectedDate) {
        val todaySlots = allTimeSlots[0].second
        val tomorrowSlots = allTimeSlots[1].second
        when {
            selectedDate.contains("今日") -> todaySlots + tomorrowSlots
            else -> tomorrowSlots
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "选择送达时间",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 左右分栏布局
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                // 左侧:日期列表
                LazyColumn(
                    modifier = Modifier
                        .weight(0.35f)
                        .fillMaxHeight()
                        .background(Color(0xFFF8F8F8)),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(dateList.size) { index ->
                        val date = dateList[index]
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDate = date },
                            color = if (selectedDate == date) Color.White else Color(0xFFF8F8F8)
                        ) {
                            Text(
                                text = date,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(1.dp))

                // 右侧:时间段列表
                LazyColumn(
                    modifier = Modifier
                        .weight(0.65f)
                        .fillMaxHeight()
                        .background(Color.White)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(timeSlots.size) { index ->
                        val (timeSlot, description) = timeSlots[index]
                        val displayText = when {
                            timeSlot == "尽快送达" -> "今日 $timeSlot"
                            selectedDate.contains("今日") && index < 5 -> "今日 $timeSlot"
                            selectedDate.contains("今日") && index >= 5 -> "明日 $timeSlot"
                            else -> "${selectedDate.take(5)} $timeSlot"
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedTimeSlot = timeSlot

                                    // 判断实际是今日还是明日的时间段
                                    val isToday = selectedDate.contains("今日") && index < 5
                                    val isTomorrow = selectedDate.contains("今日") && index >= 5

                                    // 根据选择的时间段自动切换左侧日期
                                    if (isTomorrow) {
                                        selectedDate = "明日(周三)"
                                    }

                                    // 确定实际日期
                                    val actualDate = when {
                                        isToday -> "今日"
                                        isTomorrow -> "明日"
                                        selectedDate.contains("明日") -> "明日"
                                        else -> selectedDate.take(5)
                                    }

                                    onTimeSelected(actualDate, timeSlot)
                                },
                            color = if (selectedTimeSlot == timeSlot) Color(0xFFF0F8FF) else Color.White
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = displayText,
                                        fontSize = 14.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = description,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                                if (selectedTimeSlot == timeSlot) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF00BFFF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AddressSelectionDialog(
    addresses: List<com.example.eleme_sim.model.Address>,
    selectedAddress: com.example.eleme_sim.model.Address?,
    onAddressSelected: (com.example.eleme_sim.model.Address) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "选择收货地址",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    items(addresses.size) { index ->
                        val address = addresses[index]
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onAddressSelected(address) },
                            shape = RoundedCornerShape(8.dp),
                            color = if (address.addressId == selectedAddress?.addressId)
                                Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = address.receiverName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = address.receiverPhone.replace(Regex("(\\d{3})\\d{4}(\\d{4})"), "$1****$2"),
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = address.getFullAddress(),
                                        fontSize = 13.sp,
                                        color = Color.Gray,
                                        maxLines = 2
                                    )
                                }
                                if (address.addressId == selectedAddress?.addressId) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF00BFFF),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("取消", fontSize = 16.sp)
                }
            }
        }
    }
}

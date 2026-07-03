package com.example.eleme_sim.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.eleme_sim.model.Address
import com.example.eleme_sim.model.Coupon

@Composable
fun CouponSelectionDialog(
    availableCoupons: List<Coupon>,
    selectedCoupon: Coupon?,
    subtotal: Double,
    onCouponSelected: (Coupon?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF7FAFC),
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
                Text("兑换码", fontSize = 14.sp, color = Color(0xFF16B8F3))
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "饿了么红包  可选1张",
                        fontSize = 14.sp,
                        color = Color(0xFF98A2B3),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(availableCoupons) { coupon ->
                    CouponItem(
                        coupon = coupon,
                        isSelected = coupon.couponId == selectedCoupon?.couponId,
                        subtotal = subtotal,
                        isRecommended = coupon == availableCoupons.firstOrNull(),
                        onSelect = { onCouponSelected(coupon) }
                    )
                }

                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCouponSelected(null) },
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("不想爆涨?可直接使用", fontSize = 14.sp, color = Color(0xFF667085))
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
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16B8F3)),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (selectedCoupon != null) {
                        val deliveryFee = 5.0
                        "已选1张,可减 ¥%.0f".format(selectedCoupon.calculateDiscount(subtotal, deliveryFee))
                    } else {
                        "确定"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
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
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text("选择支付方式", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                listOf("微信支付", "支付宝").forEach { method ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMethodSelected(method) },
                        color = if (selectedMethod == method) Color(0xFFF0F9FF) else Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = method, fontSize = 16.sp, color = Color.Black)
                            if (selectedMethod == method) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF16B8F3)
                                )
                            }
                        }
                    }
                    if (method != "支付宝") {
                        HorizontalDivider(color = Color(0xFFF0F2F5))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16B8F3))
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

    val allTimeSlots = remember {
        listOf(
            "今日" to listOf(
                "尽快送达" to "1元配送费",
                "22:25-22:45" to "1元配送费",
                "22:45-23:05" to "1元配送费",
                "23:05-23:25" to "1元配送费",
                "23:25-23:45" to "1元配送费"
            ),
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(0.35f)
                        .fillMaxHeight()
                        .background(Color(0xFFF7F8FA)),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(dateList) { date ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDate = date },
                            color = if (selectedDate == date) Color.White else Color(0xFFF7F8FA)
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

                LazyColumn(
                    modifier = Modifier
                        .weight(0.65f)
                        .fillMaxHeight()
                        .background(Color.White)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    items(timeSlots) { pair ->
                        val (timeSlot, description) = pair
                        val index = timeSlots.indexOf(pair)
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
                                    val isToday = selectedDate.contains("今日") && index < 5
                                    val isTomorrow = selectedDate.contains("今日") && index >= 5
                                    if (isTomorrow) {
                                        selectedDate = "明日(周三)"
                                    }
                                    val actualDate = when {
                                        isToday -> "今日"
                                        isTomorrow -> "明日"
                                        selectedDate.contains("明日") -> "明日"
                                        else -> selectedDate.take(5)
                                    }
                                    onTimeSelected(actualDate, timeSlot)
                                },
                            shape = RoundedCornerShape(14.dp),
                            color = if (selectedTimeSlot == timeSlot) Color(0xFFF0F9FF) else Color.White
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
                                        tint = Color(0xFF16B8F3)
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
fun AddressSelectionDialog(
    addresses: List<Address>,
    selectedAddress: Address?,
    onAddressSelected: (Address) -> Unit,
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
            shape = RoundedCornerShape(18.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "选择收货地址",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(addresses) { address ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAddressSelected(address) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (address.addressId == selectedAddress?.addressId) {
                                Color(0xFFEAF8FF)
                            } else {
                                Color(0xFFF7F8FA)
                            }
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
                                        tint = Color(0xFF16B8F3)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16B8F3)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("取消", fontSize = 16.sp)
                }
            }
        }
    }
}

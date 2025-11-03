package com.example.myele.ui.coupons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.model.Coupon
import com.example.myele.model.CouponStatus
import com.example.myele.utils.JsonFileWriter
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponsScreen(navController: NavController, repository: DataRepository) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("红包") }
    var selectedSort by remember { mutableStateOf("默认排序") }
    val coupons = remember { repository.getCoupons() }

    // 页面进入时写入JSON数据到 /data/data/com.example.myele/files/messages.json
    LaunchedEffect(Unit) {
        val jsonData = JsonFileWriter.createCouponsPageData(
            extraData = mapOf(
                "selected_tab" to "红包",
                "total_coupons" to coupons.size,
                "source" to "profile_menu"
            )
        )
        JsonFileWriter.writeToMessagesJson(context, jsonData)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("红包卡券") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 标签页
            TabRow(
                selectedTabIndex = listOf("红包", "已购券", "卡").indexOf(selectedTab),
                containerColor = Color.White
            ) {
                listOf("红包", "已购券", "卡").forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = tab,
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                                )
                                if (selectedTab == tab && tab == "红包") {
                                    Text(
                                        text = "12",
                                        fontSize = 12.sp,
                                        color = Color(0xFFFF3366)
                                    )
                                }
                            }
                        }
                    )
                }
            }

            // 排序和筛选
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listOf("默认排序", "爆红包", "平台红包", "商品红包")) { sort ->
                    FilterChip(
                        selected = selectedSort == sort,
                        onClick = { selectedSort = sort },
                        label = { Text(sort, fontSize = 13.sp) }
                    )
                }
            }

            // 红包卡券列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 30元超级吃货卡
                item {
                    SuperFoodieCardCoupon()
                }

                // 红包列表
                items(coupons.size.coerceAtMost(10)) { index ->
                    CouponCard(coupons[index])
                }

                // 底部功能按钮
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomActionButton("历史红包")
                        BottomActionButton("兑换码")
                        BottomActionButton("天天爆红包")
                        BottomActionButton("天猫优惠")
                    }
                }
            }
        }
    }
}

@Composable
fun SuperFoodieCardCoupon() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "5",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B00)
                    )
                    Text(text = "元×", fontSize = 14.sp)
                    Text(
                        text = "6",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B00)
                    )
                    Text(text = "张", fontSize = 14.sp)
                }
                Text(
                    text = "无门槛",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFFF3366).copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "爆红包",
                        fontSize = 11.sp,
                        color = Color(0xFFFF3366),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "30元超级吃货卡",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "0.4折",
                    fontSize = 12.sp,
                    color = Color(0xFFFF3366)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "爆红包商家专享",
                    fontSize = 12.sp,
                    color = Color(0xFFFF3366)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B00)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("¥", fontSize = 12.sp)
                        Text("0.7", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("购买", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CouponCard(coupon: Coupon) {
    val isExpiring = coupon.isExpiringSoon
    val dateFormat = SimpleDateFormat("今日 HH:mm 到期", Locale.getDefault())

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧红包图标
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = if (coupon.status == CouponStatus.AVAILABLE) Color(0xFFFF3366) else Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "爆",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 中间信息
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coupon.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateFormat.format(coupon.validUntil),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                if (coupon.description != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = coupon.description,
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 右侧金额和按钮
            Column(horizontalAlignment = Alignment.End) {
                if (isExpiring) {
                    Text(
                        text = "将到期",
                        fontSize = 11.sp,
                        color = Color(0xFFFF6B00)
                    )
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "¥",
                        fontSize = 16.sp,
                        color = Color(0xFFFF3366)
                    )
                    Text(
                        text = "${coupon.discountAmount?.toInt() ?: 6}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF3366)
                    )
                }
                Text(
                    text = "满${coupon.minOrderAmount?.toInt() ?: 20}可用",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (coupon.name.contains("可爆")) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        OutlinedButton(
                            onClick = { },
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("去使用", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF6B00)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("去爆涨", fontSize = 12.sp)
                        }
                    }
                } else {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B00)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text("去使用", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomActionButton(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Icon(
            imageVector = when (text) {
                "历史红包" -> Icons.Default.History
                "兑换码" -> Icons.Default.QrCode
                "天天爆红包" -> Icons.Default.LocalOffer
                else -> Icons.Default.CardGiftcard
            },
            contentDescription = text,
            tint = Color(0xFFFF6B00),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, fontSize = 12.sp)
    }
}

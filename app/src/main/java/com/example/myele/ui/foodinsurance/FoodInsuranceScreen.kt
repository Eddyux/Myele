package com.example.myele.ui.foodinsurance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInsuranceScreen(
    navController: NavController,
    repository: DataRepository,
    orderId: String
) {
    val order = remember { repository.getOrders().find { it.orderId == orderId } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("订单保障") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 订单信息头部
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = order?.items?.firstOrNull()?.productName ?: "订单商品",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "下单时间：${order?.orderTime?.let { SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).format(it) } ?: ""}",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }

                        // 蓝色盾牌图标
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF00BFFF),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            // 超时免单权益
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "超时免单权益",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFFFE5E5)
                                ) {
                                    Text(
                                        "下一单专享",
                                        fontSize = 11.sp,
                                        color = Color(0xFFFF3366),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF3366)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text("免费领", fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "超时≥1分钟就免（超时≥1分钟就免）",
                            fontSize = 13.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "订单实付金额100%赔付（最高赔100元），领取后即刻生效",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 食无忧理赔
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "食无忧",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFE0E0E0)
                                ) {
                                    Text(
                                        "商家赠送",
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            TextButton(onClick = { }) {
                                Text("服务详情 >", color = Color.Gray, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "如发现食物变质、存在异物或引起就医，均可申请理赔",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 三个理赔选项
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            InsuranceOptionItem("食物变质", "2天内", Icons.Default.Warning, Color(0xFFFF9800))
                            InsuranceOptionItem("存在异物", "2天内", Icons.Default.ErrorOutline, Color(0xFFFF5252))
                            InsuranceOptionItem("致病就医", "15天内\n需证明", Icons.Default.LocalHospital, Color(0xFF42A5F5))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("申请理赔", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }

            // 慢必赔
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "慢必赔",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFE0E0E0)
                                ) {
                                    Text(
                                        "平台赠送",
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            TextButton(onClick = { }) {
                                Text("服务详情 >", color = Color.Gray, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "订单已送达，未满足赔付条件，服务保障已结束",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 送达时间进度条
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("11:09送达", fontSize = 12.sp, color = Color(0xFF00BFFF), fontWeight = FontWeight.Bold)
                                Text("11:19送达", fontSize = 12.sp, color = Color.Gray)
                                Text("11:29送达", fontSize = 12.sp, color = Color.Gray)
                                Text("11:39送达", fontSize = 12.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // 进度条
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(Color(0xFFE0E0E0), RoundedCornerShape(2.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.15f)
                                        .height(4.dp)
                                        .background(Color(0xFF00BFFF), RoundedCornerShape(2.dp))
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("原定送达时间", fontSize = 11.sp, color = Color.Gray)
                                Text("超时10分钟", fontSize = 11.sp, color = Color.Gray)
                                Text("超时20分钟", fontSize = 11.sp, color = Color.Gray)
                                Text("超时≥30分钟", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 赔付金额
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CompensationAmount("¥3")
                            CompensationAmount("¥5")
                            CompensationAmount("¥7")
                        }
                    }
                }
            }

            // 底部提示
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "饿了么app或淘宝闪购搜 慢必赔",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "天天领・订单权益",
                        fontSize = 12.sp,
                        color = Color(0xFF00BFFF)
                    )
                }
            }
        }
    }
}

@Composable
fun InsuranceOptionItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconColor.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.height(32.dp)
        )
    }
}

@Composable
fun CompensationAmount(amount: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFE5E5),
        modifier = Modifier.size(80.dp, 60.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = amount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            )
        }
    }
}

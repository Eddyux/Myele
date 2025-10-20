package com.example.myele.ui.myaddresses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.myele.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAddressesScreen(navController: NavController, repository: DataRepository) {
    val addresses = remember { repository.getAddresses() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("收货地址") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    TextButton(onClick = { }) {
                        Text("新增地址", fontSize = 15.sp, color = Color(0xFF00BFFF))
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
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(addresses.size) { index ->
                AddressCard(addresses[index])
            }
        }
    }
}

@Composable
fun AddressCard(address: Address) {
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // 地址标签
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val labelColor = when (address.tag) {
                        "常用" -> Color(0xFFFF6B00)
                        "学校" -> Color(0xFF00BFFF)
                        "公司" -> Color(0xFF4CAF50)
                        "家" -> Color(0xFFFF3366)
                        else -> Color.Gray
                    }

                    if (!address.tag.isNullOrBlank()) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = labelColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = address.tag,
                                fontSize = 12.sp,
                                color = labelColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = address.street,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 详细地址（房号等）
                if (address.detailAddress.isNotBlank()) {
                    Text(
                        text = address.detailAddress,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // 收货人信息
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = address.receiverName,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(先生)",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = address.receiverPhone,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 编辑按钮
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "编辑",
                    tint = Color.Gray
                )
            }
        }
    }
}

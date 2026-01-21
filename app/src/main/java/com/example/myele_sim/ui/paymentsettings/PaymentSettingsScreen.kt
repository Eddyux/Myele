package com.example.myele_sim.ui.paymentsettings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import com.example.myele_sim.data.DataRepository

/**
 * 支付设置页面
 * 从Settings页面点击"支付设置"进入
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = DataRepository(context)
    val prefsManager = repository.getPreferencesManager()

    var alipayFreePassword by remember { mutableStateOf(prefsManager.getAlipayFreePasswordEnabled()) }
    var balanceFreePassword by remember { mutableStateOf(prefsManager.getBalanceFreePasswordEnabled()) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("支付设置") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 12.dp)
        ) {
            // 支付宝免密支付
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
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
                                text = "支付宝免密支付",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "开启免密，无需密码付款",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = alipayFreePassword,
                            onCheckedChange = { enabled ->
                                alipayFreePassword = enabled
                                prefsManager.setAlipayFreePasswordEnabled(enabled)
                                dialogMessage = if (enabled) "免密支付已开启" else "免密支付已关闭"
                                showDialog = true

                                // 记录设置操作
                                com.example.myele_sim.utils.ActionLogger.logSettings(
                                    context = context,
                                    settingType = "免密支付",
                                    enabled = enabled,
                                    showDialog = true
                                )
                            }
                        )
                    }
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            // 余额支付分类标题
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF5F5F5)
                ) {
                    Text(
                        text = "余额支付",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // 余额免密支付
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
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
                                text = "余额免密支付",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "开启免密，50元以下无需密码付款",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = balanceFreePassword,
                            onCheckedChange = { enabled ->
                                balanceFreePassword = enabled
                                prefsManager.setBalanceFreePasswordEnabled(enabled)
                                dialogMessage = if (enabled) "余额免密支付已开启" else "余额免密支付已关闭"
                                showDialog = true
                            }
                        )
                    }
                }
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            }

            // 常见问题
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "常见问题",
                            fontSize = 16.sp,
                            color = Color.Black
                        )

                        TextButton(onClick = { /* TODO */ }) {
                            Text(
                                text = "查看",
                                fontSize = 14.sp,
                                color = Color(0xFF2196F3)
                            )
                        }
                    }
                }
            }
        }

        // 设置成功弹窗
        if (showDialog) {
            PaymentSettingDialog(
                message = dialogMessage,
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun PaymentSettingDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "设置成功",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = message,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("确定", color = Color.White, fontSize = 16.sp)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

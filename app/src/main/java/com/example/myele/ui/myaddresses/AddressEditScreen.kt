package com.example.myele.ui.myaddresses

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
import androidx.navigation.NavController
import com.example.myele.data.DataRepository
import com.example.myele.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressEditScreen(
    navController: NavController,
    repository: DataRepository,
    addressId: String?
) {
    // 获取地址数据（如果是编辑模式）
    val existingAddress = addressId?.let { repository.getAddresses().find { it.addressId == addressId } }
    val isEditMode = existingAddress != null

    // 状态变量
    var address by remember { mutableStateOf(existingAddress?.street ?: "华中师范大学元宝山学生公寓二期") }
    var detailAddress by remember { mutableStateOf(existingAddress?.detailAddress ?: "") }
    var receiverName by remember { mutableStateOf(existingAddress?.receiverName ?: "") }
    var receiverPhone by remember { mutableStateOf(existingAddress?.receiverPhone ?: "") }
    var selectedGender by remember { mutableStateOf("先生") }
    var selectedTag by remember { mutableStateOf(existingAddress?.tag ?: "学校") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "修改收货地址" else "新增收货地址") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "删除",
                                tint = Color.Gray
                            )
                        }
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
            // 地图区域（简化版）
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF00BFFF),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = address,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 粘贴文本识别
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                        Text(
                            text = "粘贴文本，智能识别地址信息",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        TextButton(onClick = { /* TODO: 粘贴功能 */ }) {
                            Text("粘贴", color = Color(0xFF00BFFF), fontSize = 14.sp)
                        }
                    }
                }
            }

            // 地址输入
            item {
                AddressInputSection(
                    title = "地址",
                    value = address,
                    onValueChange = { address = it },
                    placeholder = "请输入地址"
                )
            }

            // 门牌号输入
            item {
                AddressInputSection(
                    title = "门牌号",
                    value = detailAddress,
                    onValueChange = { detailAddress = it },
                    placeholder = "楼栋/门牌号等详细信息"
                )
            }

            // 收货人输入
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "收货人",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.width(70.dp)
                        )
                        TextField(
                            value = receiverName,
                            onValueChange = { receiverName = it },
                            placeholder = { Text("请输入收货人姓名", fontSize = 14.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // 性别选择
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedGender == "先生",
                                onClick = { selectedGender = "先生" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF00BFFF)
                                )
                            )
                            Text("先生", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            RadioButton(
                                selected = selectedGender == "女士",
                                onClick = { selectedGender = "女士" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF00BFFF)
                                )
                            )
                            Text("女士", fontSize = 14.sp)
                        }
                    }
                }
            }

            // 手机号输入
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "手机号",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.width(70.dp)
                        )
                        Text(
                            text = "+86",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = receiverPhone,
                            onValueChange = { receiverPhone = it },
                            placeholder = { Text("请输入手机号", fontSize = 14.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 标签选择
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "标签",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.width(70.dp)
                        )
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            listOf("家", "公司", "学校").forEach { tag ->
                                Surface(
                                    modifier = Modifier.clickable { selectedTag = tag },
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (selectedTag == tag) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = 14.sp,
                                        color = if (selectedTag == tag) Color(0xFF00BFFF) else Color.Gray,
                                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 底部保存按钮
            item {
                Button(
                    onClick = {
                        // TODO: 保存地址逻辑
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFFF)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "保存地址",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        // 删除地址确认弹窗
        if (showDeleteDialog) {
            DeleteAddressDialog(
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    // TODO: 删除地址逻辑
                    showDeleteDialog = false
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun AddressInputSection(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(70.dp)
            )
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, fontSize = 14.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            )
        }
    }
}

@Composable
fun DeleteAddressDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "确认删除此地址？",
                fontSize = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
            ) {
                Text("确认", color = Color.White, fontSize = 16.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消", color = Color.Gray, fontSize = 16.sp)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

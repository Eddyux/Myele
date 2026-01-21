package com.example.myele_sim.ui.myaddresses

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myele_sim.data.DataRepository
import com.example.myele_sim.model.Address
import com.example.myele_sim.model.AddressType
import java.util.UUID

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
    var showSaveSuccessDialog by remember { mutableStateOf(false) }

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
                MapSection(address = address)
            }

            // 粘贴文本识别
            item {
                PasteTextSection()
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
                ReceiverSection(
                    receiverName = receiverName,
                    onReceiverNameChange = { receiverName = it },
                    selectedGender = selectedGender,
                    onGenderChange = { selectedGender = it }
                )
            }

            // 手机号输入
            item {
                PhoneSection(
                    receiverPhone = receiverPhone,
                    onPhoneChange = { receiverPhone = it }
                )
            }

            // 标签选择
            item {
                TagSection(
                    selectedTag = selectedTag,
                    onTagChange = { selectedTag = it }
                )
            }

            // 底部保存按钮
            item {
                Button(
                    onClick = {
                        // 保存地址逻辑
                        val addressToSave = Address(
                            addressId = existingAddress?.addressId ?: UUID.randomUUID().toString(),
                            receiverName = receiverName,
                            receiverPhone = receiverPhone,
                            street = address,
                            detailAddress = detailAddress,
                            addressType = when (selectedTag) {
                                "家" -> AddressType.HOME
                                "公司" -> AddressType.COMPANY
                                "学校" -> AddressType.SCHOOL
                                else -> AddressType.OTHER
                            },
                            isDefault = false,
                            tag = selectedTag
                        )

                        if (isEditMode) {
                            repository.updateAddress(addressToSave)
                        } else {
                            repository.addAddress(addressToSave)

                            // 记录添加地址操作（仅在新增模式下）
                            com.example.myele_sim.utils.ActionLogger.logAction(
                                context = navController.context,
                                action = "add_address",
                                page = "address",
                                pageInfo = mapOf(
                                    "screen_name" to "AddressEditScreen"
                                ),
                                extraData = mapOf(
                                    "address" to address,
                                    "detail_address" to detailAddress,
                                    "name" to receiverName,
                                    "phone" to receiverPhone,
                                    "tag" to selectedTag
                                )
                            )
                        }

                        // 显示保存成功弹窗
                        showSaveSuccessDialog = true
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
                    // 删除地址逻辑
                    addressId?.let { repository.deleteAddress(it) }
                    showDeleteDialog = false
                    navController.popBackStack()
                }
            )
        }

        // 保存成功弹窗
        if (showSaveSuccessDialog) {
            SaveSuccessDialog(
                onDismiss = {
                    showSaveSuccessDialog = false
                    navController.popBackStack()
                }
            )
        }
    }
}

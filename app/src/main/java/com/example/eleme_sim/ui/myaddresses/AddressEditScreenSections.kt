package com.example.eleme_sim.ui.myaddresses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MapSection(address: String) {
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

@Composable
fun PasteTextSection() {
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

@Composable
fun ReceiverSection(
    receiverName: String,
    onReceiverNameChange: (String) -> Unit,
    selectedGender: String,
    onGenderChange: (String) -> Unit
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
                text = "收货人",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(70.dp)
            )
            TextField(
                value = receiverName,
                onValueChange = onReceiverNameChange,
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
                    onClick = { onGenderChange("先生") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF00BFFF)
                    )
                )
                Text("先生", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                RadioButton(
                    selected = selectedGender == "女士",
                    onClick = { onGenderChange("女士") },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF00BFFF)
                    )
                )
                Text("女士", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun PhoneSection(
    receiverPhone: String,
    onPhoneChange: (String) -> Unit
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
                onValueChange = onPhoneChange,
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

@Composable
fun TagSection(
    selectedTag: String,
    onTagChange: (String) -> Unit
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
                        modifier = Modifier.clickable { onTagChange(tag) },
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

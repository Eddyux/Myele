package com.example.eleme_sim.ui.storepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.eleme_sim.model.Restaurant
import com.example.eleme_sim.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreMenuDialog(
    showMoreMenu: Boolean,
    onDismissRequest: () -> Unit,
    onShareClick: () -> Unit,
    navController: NavController
) {
    if (showMoreMenu) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                // 横向选项
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 分享按钮
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDismissRequest()
                                onShareClick()
                            }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF00BFFF)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("分享", fontSize = 14.sp, color = Color.Black)
                    }

                    // 举报商家按钮
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDismissRequest()
                                navController.navigate(Screen.Undeveloped.createRoute("举报商家"))
                            }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Report,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF00BFFF)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("举报商家", fontSize = 14.sp, color = Color.Black)
                    }

                    // 智能客服按钮
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDismissRequest()
                                navController.navigate(Screen.MyKefu.route)
                            }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Support,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF00BFFF)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("智能客服", fontSize = 14.sp, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 取消按钮
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("取消", fontSize = 16.sp, color = Color.Gray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareMenuDialog(
    showShareMenu: Boolean,
    onDismissRequest: () -> Unit,
    restaurantId: String,
    restaurant: Restaurant?,
    onPlatformSelected: (String) -> Unit
) {
    val context = LocalContext.current

    if (showShareMenu) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "分享到",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 分享平台网格
                val platforms = listOf(
                    "微信" to Icons.Default.Chat,
                    "朋友圈" to Icons.Default.Group,
                    "微博" to Icons.Default.Public,
                    "QQ" to Icons.Default.Message,
                    "QQ空间" to Icons.Default.Photo,
                    "钉钉" to Icons.Default.Work
                )

                platforms.chunked(4).forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { (platform, icon) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        // 记录分享操作
                                        com.example.eleme_sim.utils.ActionLogger.logAction(
                                            context = context,
                                            action = "share_store",
                                            page = "store_page",
                                            pageInfo = mapOf(
                                                "restaurant_name" to (restaurant?.name ?: ""),
                                                "restaurant_id" to restaurantId
                                            ),
                                            extraData = mapOf(
                                                "platform" to platform
                                            )
                                        )

                                        onPlatformSelected(platform)
                                        onDismissRequest()
                                    }
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = platform,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(8.dp),
                                    tint = Color(0xFF00BFFF)
                                )
                                Text(
                                    text = platform,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 取消按钮
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("取消", fontSize = 16.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ShareSuccessDialog(
    showDialog: Boolean,
    sharedPlatform: String,
    onDismissRequest: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "分享成功",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "已分享到$sharedPlatform",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text("确定", fontSize = 16.sp, color = Color(0xFF00BFFF))
                }
            }
        )
    }
}

package com.example.eleme_sim.ui.customerservice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.R

private val ServiceHallCard = Color(0xFFFFFFFF)
private val ServiceHallPrimary = Color(0xFF161B22)
private val ServiceHallSecondary = Color(0xFF8F98A5)
private val ServiceHallAccent = Color(0xFF22B8F8)

@Composable
fun SmartServiceBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 8.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 6.dp, bottom = 12.dp)
            ) {
                Text(
                    text = "智能客服为您服务",
                    fontSize = 24.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ServiceHallPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "您的满意是我们不懈的追求！",
                    fontSize = 14.sp,
                    color = ServiceHallSecondary
                )
            }

            Image(
                painter = painterResource(id = R.drawable.customerservice_kefu),
                contentDescription = "客服",
                modifier = Modifier
                    .width(148.dp)
                    .height(148.dp)
                    .offset(y = 4.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ServiceButton(text: String, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { },
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color(0xFFD7DEE8)),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun NotificationBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { },
        shape = RoundedCornerShape(20.dp),
        color = ServiceHallCard,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = Color(0xFFFF8A47),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "夜间配送延迟通知",
                    fontSize = 15.sp,
                    color = Color(0xFFFF8A47),
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFFF8A47)
            )
        }
    }
}

@Composable
fun ServiceFunctionItem(label: String, icon: ImageVector, badge: String? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Box {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFF8FBFF)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = ServiceHallPrimary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            if (badge != null) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFF6C58)
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = ServiceHallPrimary
        )
    }
}

@Composable
fun ServiceTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = ServiceHallAccent,
            divider = {},
            indicator = {}
        ) {
            listOf("热门问题", "订单问题", "红包", "账户", "支付").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) ServiceHallPrimary else ServiceHallSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .width(if (selectedTab == index) 34.dp else 0.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (selectedTab == index) ServiceHallAccent else Color.Transparent
                                    )
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomButtons(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { navController.navigate(com.example.eleme_sim.navigation.Screen.ServiceProgress.route) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color(0xFFD4DBE5)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = ServiceHallPrimary
                )
            ) {
                Text(
                    text = "服务进度",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { navController.navigate("my_kefu") },
                modifier = Modifier
                    .weight(2f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ServiceHallAccent
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "在线客服",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFF5A5A)
                    ) {
                        Text(
                            text = "繁忙",
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

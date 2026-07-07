package com.example.eleme_sim.ui.coupons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eleme_sim.data.DataRepository
import com.example.eleme_sim.utils.JsonFileWriter

private val CouponsPageBg = Color(0xFFF7F7F8)
private val CouponsPrimary = Color(0xFF17181A)
private val CouponsSecondary = Color(0xFF8A8D94)
private val CouponsAccent = Color(0xFF25B8F7)
private val CouponsChipBg = Color(0xFFF3F4F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponsScreen(navController: NavController, repository: DataRepository) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("红包") }
    var selectedSort by remember { mutableStateOf("默认排序") }
    val coupons = remember { repository.getCoupons() }

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
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "红包卡券",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CouponsPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = CouponsPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            )
        },
        containerColor = CouponsPageBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CouponsPageBg)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                TabRow(
                    selectedTabIndex = listOf("红包", "已购券", "卡").indexOf(selectedTab),
                    containerColor = Color.White,
                    divider = {},
                    indicator = {}
                ) {
                    listOf("红包", "已购券", "卡").forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        Text(
                                            text = tab,
                                            fontSize = 18.sp,
                                            fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedTab == tab) CouponsAccent else CouponsPrimary
                                        )
                                        if (selectedTab == tab && tab == "红包") {
                                            Text(
                                                text = "12",
                                                fontSize = 12.sp,
                                                color = CouponsAccent,
                                                modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .background(
                                                if (selectedTab == tab) CouponsAccent else Color.Transparent,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .fillMaxWidth(0.34f)
                                            .padding(vertical = 2.5f.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listOf("默认排序", "爆红包", "平台红包", "商品红包")) { sort ->
                    FilterChip(
                        selected = selectedSort == sort,
                        onClick = { selectedSort = sort },
                        label = {
                            Text(
                                text = sort,
                                fontSize = 13.sp,
                                color = if (selectedSort == sort) CouponsPrimary else CouponsSecondary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CouponsChipBg,
                            containerColor = CouponsChipBg
                        ),
                        border = null
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 0.dp, bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SuperFoodieCardCoupon()
                }

                items(coupons.size.coerceAtMost(10)) { index ->
                    CouponCard(coupons[index])
                }

                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp),
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
}

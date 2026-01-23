package com.example.eleme_sim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.eleme_sim.data.DataRepository
import com.example.eleme_sim.navigation.NavGraph
import com.example.eleme_sim.ui.main.MainScreen
import com.example.eleme_sim.ui.theme.MyEleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 注意：已将清空逻辑移到HomeScreen，避免应用重启时丢失测试数据
        // 只在进入首页时清空，保证测试流程完整性

        enableEdgeToEdge()
        setContent {
            MyEleTheme {
                val navController = rememberNavController()
                val repository = DataRepository(applicationContext)
                MainScreen(navController) { paddingValues ->
                    Box(
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        NavGraph(navController = navController, repository = repository)
                    }
                }
            }
        }
    }
}
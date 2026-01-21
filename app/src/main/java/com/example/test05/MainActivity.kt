package com.example.myele_sim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myele_sim.data.DataRepository
import com.example.myele_sim.navigation.NavGraph
import com.example.myele_sim.ui.main.MainScreen
import com.example.myele_sim.ui.theme.MyEleTheme
import com.example.myele_sim.utils.JsonFileWriter

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
                        modifier = Modifier
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
package com.example.myele

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.myele.data.DataRepository
import com.example.myele.navigation.NavGraph
import com.example.myele.ui.main.MainScreen
import com.example.myele.ui.theme.MyEleTheme
import com.example.myele.utils.JsonFileWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 每次应用启动时清空messages.json
        JsonFileWriter.clearMessagesJson(applicationContext)

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
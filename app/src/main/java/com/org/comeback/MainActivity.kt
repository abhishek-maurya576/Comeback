package com.org.comeback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.org.comeback.ui.navigation.AppNavigation
import com.org.comeback.ui.theme.ComebackTheme
import com.org.comeback.ui.viewmodels.MainViewModel
import com.org.comeback.ui.viewmodels.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val context = LocalContext.current
            val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context))
            
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            val themeColor by viewModel.themeColor.collectAsState()
            
            ComebackTheme(
                darkTheme = isDarkTheme,
                customPrimaryColor = Color(themeColor)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}
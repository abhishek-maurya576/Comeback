package com.org.comeback.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.org.comeback.ui.components.AppTopBar
import com.org.comeback.ui.components.ColorSelector
import com.org.comeback.ui.components.ThemeSwitch
import com.org.comeback.ui.viewmodels.MainViewModel
import com.org.comeback.ui.viewmodels.MainViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeCustomizationScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context))
    
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val themeColor by viewModel.themeColor.collectAsState()
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Theme Settings",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Section title
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Theme toggle
            ThemeSwitch(
                isDarkTheme = isDarkTheme,
                onThemeChange = { viewModel.setDarkTheme(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(24.dp))
            
            // Color theme selection
            ColorSelector(
                colors = viewModel.availableColors,
                selectedColor = Color(themeColor),
                onColorSelected = { color ->
                    viewModel.setThemeColor(color)
                }
            )
        }
    }
} 
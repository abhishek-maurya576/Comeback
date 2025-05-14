package com.org.comeback.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.org.comeback.ui.screens.ChatScreen
import com.org.comeback.ui.screens.CustomMessageScreen
import com.org.comeback.ui.screens.HomeScreen
import com.org.comeback.ui.screens.LanguageSelectionScreen
import com.org.comeback.ui.screens.OnboardingScreen
import com.org.comeback.ui.screens.SplashScreen
import com.org.comeback.ui.screens.ThemeCustomizationScreen
import com.org.comeback.ui.viewmodels.MainViewModel
import com.org.comeback.ui.viewmodels.MainViewModelFactory

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object CustomMessage : Screen("custom_message")
    object LanguageSelection : Screen("language_selection")
    object ThemeCustomization : Screen("theme_customization")
    object Chat : Screen("chat")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context))
    val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateNext = {
                    if (onboardingCompleted) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    viewModel.setOnboardingCompleted(true)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.CustomMessage.route) {
            CustomMessageScreen(navController = navController)
        }
        
        composable(Screen.LanguageSelection.route) {
            LanguageSelectionScreen(navController = navController)
        }
        
        composable(Screen.ThemeCustomization.route) {
            ThemeCustomizationScreen(navController = navController)
        }
        
        composable(Screen.Chat.route) {
            ChatScreen(navController = navController)
        }
    }
} 
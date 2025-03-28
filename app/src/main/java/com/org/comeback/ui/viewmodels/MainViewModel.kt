package com.org.comeback.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.org.comeback.data.api.GeminiApi
import com.org.comeback.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(private val preferencesManager: PreferencesManager) : ViewModel() {
    
    // UI state
    var showRejectionMessage by mutableStateOf(false)
    var rejectionMessage by mutableStateOf("")
    var userName by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    
    // Easter egg states
    var logoClickCount by mutableStateOf(0)
    var showEasterEgg by mutableStateOf(false)
    var easterEggGame by mutableStateOf(EasterEggType.NONE)
    
    // Onboarding state
    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()
    
    // Theme settings
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    private val _themeColor = MutableStateFlow(Color.Red.toArgb())
    val themeColor: StateFlow<Int> = _themeColor.asStateFlow()
    
    // Language settings
    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language.asStateFlow()
    
    // Available languages
    val availableLanguages = listOf(
        "en" to "English",
        "es" to "Español",
        "fr" to "Français",
        "hi" to "हिन्दी"
    )
    
    // Available theme colors
    val availableColors = listOf(
        "Red" to Color.Red,
        "Blue" to Color.Blue,
        "Green" to Color(0xFF4CAF50),
        "Orange" to Color(0xFFFF9800),
        "Purple" to Color(0xFF9C27B0)
    )
    
    init {
        viewModelScope.launch {
            preferencesManager.isDarkTheme.collectLatest { isDark ->
                _isDarkTheme.value = isDark
            }
        }
        
        viewModelScope.launch {
            preferencesManager.themeColor.collectLatest { color ->
                _themeColor.value = color
            }
        }
        
        viewModelScope.launch {
            preferencesManager.language.collectLatest { lang ->
                _language.value = lang
            }
        }
        
        viewModelScope.launch {
            preferencesManager.onboardingCompleted.collectLatest { completed ->
                _onboardingCompleted.value = completed
            }
        }
    }
    
    // Function to handle logo clicks
    fun onLogoClick() {
        logoClickCount++
        android.util.Log.d("MainViewModel", "Logo clicked! Count: $logoClickCount")
        
        if (logoClickCount >= 3) {
            logoClickCount = 0
            easterEggGame = EasterEggType.values().random().takeIf { it != EasterEggType.NONE } 
                ?: EasterEggType.COLOR_MATCH
            showEasterEgg = true
            android.util.Log.d("MainViewModel", "Easter egg triggered: $easterEggGame")
        }
    }
    
    // Function to dismiss the easter egg
    fun dismissEasterEgg() {
        showEasterEgg = false
        easterEggGame = EasterEggType.NONE
    }
    
    // Function to generate a funny rejection message
    fun generateRejectionMessage(personalized: Boolean = false) {
        isLoading = true
        android.util.Log.d("MainViewModel", "Starting to generate rejection message, personalized=$personalized")
        viewModelScope.launch {
            try {
                rejectionMessage = if (personalized && userName.isNotBlank()) {
                    android.util.Log.d("MainViewModel", "Generating personalized message for name=$userName")
                    GeminiApi.generatePersonalizedMessage(userName, _language.value)
                } else {
                    android.util.Log.d("MainViewModel", "Generating standard message")
                    GeminiApi.generateFunnyRejectionMessage(_language.value)
                }
                android.util.Log.d("MainViewModel", "Message generated: $rejectionMessage")
            } catch (e: Exception) {
                android.util.Log.e("MainViewModel", "Error generating message", e)
                rejectionMessage = "Oops! Something went wrong, but you're still awesome! 😎"
            } finally {
                isLoading = false
                showRejectionMessage = true
                android.util.Log.d("MainViewModel", "Loading finished, showing message")
            }
        }
    }
    
    // Function to translate a message
    suspend fun translateMessage(message: String, targetLanguage: String): String {
        return GeminiApi.translateText(message, targetLanguage)
    }
    
    // Update theme settings
    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            preferencesManager.setDarkTheme(isDark)
        }
    }
    
    // Update theme color
    fun setThemeColor(color: Color) {
        viewModelScope.launch {
            preferencesManager.setThemeColor(color.toArgb())
        }
    }
    
    // Update language
    fun setLanguage(lang: String) {
        viewModelScope.launch {
            preferencesManager.setLanguage(lang)
        }
    }
    
    // Set onboarding completed
    fun setOnboardingCompleted(completed: Boolean) {
        viewModelScope.launch {
            preferencesManager.setOnboardingCompleted(completed)
        }
    }
}

// Enum for different types of Easter egg mini-games
enum class EasterEggType {
    NONE,
    COLOR_MATCH,
    MEMORY_GAME,
    SPIN_LOGO
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(PreferencesManager(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 
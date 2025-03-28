package com.org.comeback.data.preferences

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class PreferencesManager(private val context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val THEME_COLOR_KEY = intPreferencesKey("theme_color")
        val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
    }

    // Theme preference
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false
    }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDarkTheme
        }
    }

    // Language preference
    val language: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "en" // Default language is English
    }

    suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    // Theme color preference
    val themeColor: Flow<Int> = dataStore.data.map { preferences ->
        preferences[THEME_COLOR_KEY] ?: Color.Red.toArgb() // Default is our Red theme
    }

    suspend fun setThemeColor(colorInt: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_COLOR_KEY] = colorInt
        }
    }
    
    // Onboarding completed preference
    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED_KEY] ?: false
    }
    
    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }
} 
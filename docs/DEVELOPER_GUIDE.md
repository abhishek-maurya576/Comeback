# Comeback App - Developer Guide

This technical documentation provides detailed information about the Comeback app's architecture, components, and implementation details to help developers understand and contribute to the project.

## Table of Contents

1. [Project Architecture](#project-architecture)
2. [Package Structure](#package-structure)
3. [Key Components](#key-components)
4. [Data Flow](#data-flow)
5. [UI Implementation](#ui-implementation)
6. [API Integration](#api-integration)
7. [State Management](#state-management)
8. [Preferences and Storage](#preferences-and-storage)
9. [Navigation](#navigation)
10. [Testing](#testing)
11. [Common Issues](#common-issues)

## Project Architecture

The Comeback app follows the MVVM (Model-View-ViewModel) architecture pattern, which ensures a clean separation of concerns:

- **Model**: Data classes, API services, and data sources in the `data` package
- **View**: Compose UI components and screens in the `ui` package
- **ViewModel**: State management and business logic in the `viewmodels` package

The app also utilizes a unidirectional data flow pattern where:
1. UI events trigger ViewModel functions
2. ViewModel updates state based on business logic
3. UI observes and reacts to state changes

## Package Structure

```
com.org.comeback/
├── data/
│   ├── api/                 # API service interfaces and implementation
│   └── preferences/         # DataStore preference management
├── ui/
│   ├── components/          # Reusable UI components
│   ├── navigation/          # Navigation setup and screen definitions
│   ├── screens/             # Full-screen Composables
│   ├── theme/               # Theme definitions and styling
│   └── viewmodels/          # ViewModels for state management
├── util/                    # Utility classes and helper functions
└── MainActivity.kt          # Entry point of the application
```

## Key Components

### MainViewModel

Central ViewModel that manages:
- UI state for rejection messages
- Theme and language preferences
- Easter egg state and game selection
- Onboarding completion state

```kotlin
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
    
    // Language settings
    private val _language = MutableStateFlow("en")
    val language: StateFlow<String> = _language.asStateFlow()
    
    // ... other properties and functions
}
```

### PreferencesManager

Manages persistent user preferences using Jetpack DataStore:

```kotlin
class PreferencesManager(private val context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val THEME_COLOR_KEY = intPreferencesKey("theme_color")
        val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
    }
    
    // ... preferences getters and setters
}
```

### GeminiAPI

Handles communication with the Google Gemini API for generating content:

```kotlin
object GeminiApi {
    private val service: GeminiApiService = /* Retrofit service initialization */
    
    // Function to generate funny rejection messages
    suspend fun generateFunnyRejectionMessage(language: String = "en"): String {
        // Implementation
    }
    
    // Function to generate personalized funny rejection messages
    suspend fun generatePersonalizedMessage(name: String, language: String = "en"): String {
        // Implementation
    }
    
    // Function to translate a text to another language
    suspend fun translateText(text: String, targetLanguage: String): String {
        // Implementation
    }
}
```

## Data Flow

1. **User Input**: User interacts with UI elements (e.g., clicks the Install button)
2. **Event Handling**: Event is handled by a Composable function and triggers a ViewModel function
3. **Processing**: ViewModel processes the event, potentially making API calls
4. **State Update**: ViewModel updates its state properties
5. **UI Update**: Composables observe state changes and recompose to reflect the new state

Example flow for generating a rejection message:
```
User clicks Install → HomeScreen calls viewModel.generateRejectionMessage() →
ViewModel sets isLoading = true → ViewModel calls GeminiApi.generateFunnyRejectionMessage() →
API returns message → ViewModel updates rejectionMessage and showRejectionMessage = true →
HomeScreen observes state change and displays RejectionMessageCard
```

## UI Implementation

The app uses Jetpack Compose for all UI components with Material 3 design principles.

### Screen Structure

Most screens follow a common pattern:
```kotlin
@Composable
fun ScreenName(navController: NavController) {
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(LocalContext.current))
    // Collect states
    val stateA by viewModel.stateA.collectAsState()
    var stateB by remember { mutableStateOf("") }
    
    // Screen content
    Scaffold(
        topBar = { /* TopBar */ },
        // ...
    ) { paddingValues ->
        // Main content
    }
    
    // Conditional UI elements (dialogs, popovers)
    if (someCondition) {
        // Show dialog or other UI element
    }
}
```

### Easter Egg Implementation

Easter eggs are implemented as Composable dialogs that appear when triggered:

```kotlin
@Composable
fun GameName(onDismiss: () -> Unit) {
    var gameState by remember { mutableStateOf(...) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(...) {
            // Game implementation
        }
    }
}
```

## API Integration

The app uses Retrofit with OkHttp for API communication:

```kotlin
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface GeminiApiService {
    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String = API_KEY,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}
```

API calls are wrapped in try-catch blocks with appropriate error handling and logging.

## State Management

The app uses a combination of:

1. **Compose State**: `mutableStateOf` for UI-specific state that doesn't need to persist
2. **StateFlow/Flow**: For observable states that may need to be collected from multiple locations
3. **DataStore**: For persistent user preferences

Example of state collection in Compose:
```kotlin
val isDarkTheme by viewModel.isDarkTheme.collectAsState()
```

## Preferences and Storage

User preferences are stored using DataStore:

```kotlin
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

// Reading preferences
val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
    preferences[DARK_THEME_KEY] ?: false
}

// Writing preferences
suspend fun setDarkTheme(isDarkTheme: Boolean) {
    dataStore.edit { preferences ->
        preferences[DARK_THEME_KEY] = isDarkTheme
    }
}
```

## Navigation

The app uses Jetpack Compose Navigation:

```kotlin
@Composable
fun AppNavigation(navController: NavHostController) {
    val viewModel: MainViewModel = viewModel(...)
    val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) { /* ... */ }
        composable(Screen.Onboarding.route) { /* ... */ }
        composable(Screen.Home.route) { /* ... */ }
        // Other screens
    }
}
```

## Testing

[Testing details to be added]

## Common Issues

### API Key Management

The Gemini API key is currently hardcoded in `GeminiApiService.kt`. In a production environment, it should be:
- Stored securely (e.g., using BuildConfig or encrypted storage)
- Not committed to version control
- Potentially fetched from a secure backend

### State Collection

Be cautious of collecting state in multiple places, as it can lead to unnecessary recompositions. Prefer passing state down to child Composables when possible.

### Performance Considerations

- Use `remember` and `derivedStateOf` to avoid unnecessary calculations during recomposition
- Limit the scope of `collectAsState()` to avoid unnecessary recompositions
- Consider using `LaunchedEffect` for side effects instead of direct function calls

### Handling Configuration Changes

The app is designed to handle configuration changes automatically through ViewModels and DataStore, but be aware of potential issues with:
- Dialog state during rotation
- In-progress API calls during configuration changes 
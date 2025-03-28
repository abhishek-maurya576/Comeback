# Onboarding Implementation Guide

This document provides detailed information about the onboarding experience implemented in the Comeback app, including its architecture, implementation details, and customization options.

## Overview

The onboarding experience is a multi-page introduction that welcomes new users to the app and highlights its key features. It is shown only on the first launch of the app and can be bypassed if desired.

## User Flow

1. When the app is launched for the first time, the splash screen appears
2. After the splash animation completes, the app checks if onboarding has been completed
3. If onboarding has not been completed, the user is directed to the onboarding screens
4. The user can navigate through the screens or skip to the main app
5. After completing or skipping onboarding, the user is directed to the home screen
6. On subsequent app launches, the onboarding is bypassed

## Implementation Architecture

### Components Involved

1. **PreferencesManager**: Handles persistence of onboarding completion state
2. **MainViewModel**: Manages onboarding state and provides functions to update it
3. **Navigation.kt**: Controls navigation flow based on onboarding completion
4. **SplashScreen**: Entry point that directs to onboarding or home screen
5. **OnboardingScreen**: The actual onboarding experience UI

## Key Implementation Details

### Preference Storage

In `PreferencesManager.kt`, the onboarding completion state is stored using DataStore:

```kotlin
companion object {
    val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
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
```

### ViewModel Implementation

The `MainViewModel` exposes the onboarding state and provides a function to update it:

```kotlin
// Onboarding state
private val _onboardingCompleted = MutableStateFlow(false)
val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

init {
    // ... other initializations ...
    viewModelScope.launch {
        preferencesManager.onboardingCompleted.collectAsState { completed ->
            _onboardingCompleted.value = completed
        }
    }
}

// Set onboarding completed
fun setOnboardingCompleted(completed: Boolean) {
    viewModelScope.launch {
        preferencesManager.setOnboardingCompleted(completed)
    }
}
```

### Navigation Logic

The navigation flow in `Navigation.kt` checks if onboarding has been completed:

```kotlin
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
        
        // Other navigation destinations...
    }
}
```

### SplashScreen Implementation

The `SplashScreen` uses a callback to navigate to the appropriate next screen:

```kotlin
@Composable
fun SplashScreen(onNavigateNext: () -> Unit) {
    // Animation logic...
    
    LaunchedEffect(key1 = true) {
        // Rotate animation
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 1500)
        )
        
        // Delay before navigation
        delay(500)
        
        // Navigate to next screen based on onboarding completion
        onNavigateNext()
    }
    
    // UI implementation...
}
```

## Onboarding Screen Implementation

The `OnboardingScreen` uses the Accompanist Pager library to create a swipeable multi-page experience:

```kotlin
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pages = listOf(
        OnboardingPage(
            title = "Welcome to Comeback",
            description = "A fun app that generates humorous rejection messages for compatible devices",
            imageRes = R.drawable.ic_comeback_logo
        ),
        // Other pages...
    )
    
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Skip button
        // Pager
        // Indicators and navigation buttons
    }
}
```

### Page Content

Each onboarding page follows a consistent structure:

```kotlin
@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 24.dp),
            contentScale = ContentScale.Fit
        )
        
        Text(
            text = page.title,
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}
```

### Navigation Controls

The onboarding screen includes:

1. **Skip button**: Allows users to bypass onboarding completely
2. **Page indicators**: Show the current position in the onboarding flow
3. **Next button**: Advances to the next page
4. **Get Started button**: On the last page, completes onboarding

## Resources

The onboarding experience uses vector drawables for the page illustrations:

- `ic_comeback_logo`: The app logo for the welcome page
- `onboarding_install`: Icon representing the installation feature
- `onboarding_custom`: Icon representing the personalized messages
- `onboarding_eggs`: Icon representing the Easter eggs

## Customization

To customize the onboarding experience:

### Adding or Modifying Pages

Edit the `pages` list in `OnboardingScreen.kt`:

```kotlin
val pages = listOf(
    OnboardingPage(
        title = "Your Title",
        description = "Your description",
        imageRes = R.drawable.your_image
    ),
    // Add more pages...
)
```

### Styling

The onboarding screens follow the app's Material theme. To customize the appearance:

1. Modify the theme colors in `Theme.kt`
2. Update the text styles in `OnboardingPageContent`
3. Adjust padding and sizes as needed

## Best Practices

1. **Keep it concise**: Limit to 3-5 screens with short, focused content
2. **Visual consistency**: Use consistent image styles and sizes
3. **Allow skipping**: Always provide a way to skip the onboarding
4. **Clear progress**: Use visible indicators to show progress
5. **Engaging visuals**: Use illustrations that clearly represent features
6. **Action-oriented**: End with a clear call to action

## Potential Enhancements

1. **Animations**: Add page transition animations for a more polished feel
2. **Interactive elements**: Make onboarding more interactive with demo features
3. **Localization**: Add language-specific onboarding content
4. **Accessibility**: Enhance accessibility features for screen readers
5. **Metrics**: Add analytics to track onboarding completion rates
6. **Re-entry point**: Add option in settings to view onboarding again 
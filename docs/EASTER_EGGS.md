# Easter Eggs Implementation Guide

This document provides detailed information about the Easter eggs feature implemented in the Comeback app, including their activation, implementation details, and game mechanics.

## Overview

The app includes three hidden mini-games that are triggered when the user taps the app logo three times on the home screen. These games include:

1. **Color Match Game**: A color matching challenge
2. **Memory Game**: A card-matching memory game
3. **Spin Logo Game**: A tapping speed challenge

## Activation Mechanism

The Easter eggs are activated through the following flow:

1. The user taps the app logo on the HomeScreen
2. Each tap increments a counter in the MainViewModel
3. When the counter reaches 3, a random game is selected and displayed

### Implementation Details

#### Logo Click Detection

In `HomeScreen.kt`, the app logo includes a pointer input detector:

```kotlin
Image(
    painter = painterResource(id = R.drawable.ic_comeback_logo),
    contentDescription = "App Logo",
    modifier = Modifier
        .size(150.dp)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { viewModel.onLogoClick() }
            )
        }
)
```

#### Click Handling in ViewModel

The `MainViewModel` handles the logo clicks and triggers the Easter egg:

```kotlin
// Easter egg states
var logoClickCount by mutableStateOf(0)
var showEasterEgg by mutableStateOf(false)
var easterEggGame by mutableStateOf(EasterEggType.NONE)

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
```

#### Game Types Enum

The available games are defined in an enum:

```kotlin
// Enum for different types of Easter egg mini-games
enum class EasterEggType {
    NONE,
    COLOR_MATCH,
    MEMORY_GAME,
    SPIN_LOGO
}
```

## Game Implementations

### Color Match Game

A game where players must match a target color by selecting the correct color from options.

#### Key Features

- Target color display
- Multiple color options to choose from
- Score tracking
- Timer countdown
- Time penalties for incorrect choices

#### Implementation

The game uses Compose state to track score, time, and game status:

```kotlin
@Composable
fun ColorMatchGame(onDismiss: () -> Unit) {
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan)
    var targetColor by remember { mutableStateOf(colors.random()) }
    var options by remember { mutableStateOf(generateColorOptions(targetColor, colors)) }
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var gameActive by remember { mutableStateOf(true) }
    
    // Timer logic with LaunchedEffect
    // UI implementation with color boxes
    // Click handling for color selection
}
```

#### Score Calculation

- +1 point for each correct match
- -2 seconds for each incorrect match

### Memory Game

A classic card-matching memory game with emoji cards.

#### Key Features

- 4x4 grid of cards (8 pairs of emoji)
- Card flipping animations
- Match detection
- Move counter
- Game completion detection

#### Implementation

The game uses several state variables to track the current game state:

```kotlin
@Composable
fun MemoryGame(onDismiss: () -> Unit) {
    val emojis = listOf("🍎", "🍌", "🍇", "🍓", "🍒", "🍊", "🥝", "🍉")
    val gameEmojis = remember { (emojis + emojis).shuffled() }
    var flippedIndices by remember { mutableStateOf(setOf<Int>()) }
    var matchedPairs by remember { mutableStateOf(setOf<Int>()) }
    var tempFlipped by remember { mutableStateOf<Int?>(null) }
    var moves by remember { mutableStateOf(0) }
    
    // LaunchedEffect to check for matches
    // Grid implementation with card components
    // Game completion logic
}
```

#### Card Component

Each card is a separate composable that handles its own state:

```kotlin
@Composable
fun MemoryCard(emoji: String, isFaceUp: Boolean, isMatched: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(60.dp)
            .padding(4.dp)
            .clickable(enabled = !isFaceUp && !isMatched) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isMatched -> MaterialTheme.colorScheme.primaryContainer
                isFaceUp -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        // Card content
    }
}
```

### Spin Logo Game

A game where players tap a spinning logo as many times as possible before time runs out.

#### Key Features

- Continuously spinning app logo
- Tap detection
- Score counter
- Timer countdown

#### Implementation

The game uses animation and state to create the spin effect and track score:

```kotlin
@Composable
fun SpinLogoGame(onDismiss: () -> Unit) {
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(15) }
    var gameActive by remember { mutableStateOf(true) }
    
    // Animation for spinning logo using infiniteTransition
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Timer, UI implementation, and score tracking
}
```

## Adding New Easter Eggs

To add a new Easter egg game:

1. Add a new game type to the `EasterEggType` enum
2. Create a new Composable function for the game, following the pattern of existing games
3. Add the game to the conditional in `HomeScreen.kt`:

```kotlin
// Show Easter egg if triggered
if (viewModel.showEasterEgg) {
    when (viewModel.easterEggGame) {
        EasterEggType.COLOR_MATCH -> ColorMatchGame(onDismiss = { viewModel.dismissEasterEgg() })
        EasterEggType.MEMORY_GAME -> MemoryGame(onDismiss = { viewModel.dismissEasterEgg() })
        EasterEggType.SPIN_LOGO -> SpinLogoGame(onDismiss = { viewModel.dismissEasterEgg() })
        EasterEggType.YOUR_NEW_GAME -> YourNewGame(onDismiss = { viewModel.dismissEasterEgg() })
        else -> { /* No game */ }
    }
}
```

## Tips for Easter Egg Development

1. **Keep games simple**: Easter eggs should be quick to understand and play
2. **Ensure dismissability**: Always provide a way to exit the game
3. **Randomize selection**: Use random selection to add variety to the user experience
4. **Consider performance**: Use efficient animations and state management
5. **Add sound effects**: For a more immersive experience (considering proper volume control)

## Future Enhancements

Potential enhancements for the Easter eggs feature:

1. **High scores**: Persist high scores for each game
2. **Difficulty levels**: Add increasing difficulty
3. **Unlock system**: Make some games unlock only after achieving certain scores in others
4. **Rewards**: Provide unique rejection messages or themes as rewards for achieving high scores 
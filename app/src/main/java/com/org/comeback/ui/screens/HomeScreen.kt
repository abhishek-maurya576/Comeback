package com.org.comeback.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.org.comeback.R
import com.org.comeback.ui.components.AppTopBar
import com.org.comeback.ui.components.InstallButton
import com.org.comeback.ui.components.RejectionMessageCard
import com.org.comeback.ui.components.SettingsButton
import com.org.comeback.ui.navigation.Screen
import com.org.comeback.ui.viewmodels.EasterEggType
import com.org.comeback.ui.viewmodels.MainViewModel
import com.org.comeback.ui.viewmodels.MainViewModelFactory
import com.org.comeback.util.FeedbackUtils
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context))
    
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val themeColor by viewModel.themeColor.collectAsState()
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Compatibility Check",
                showBackButton = false,
                actions = {
                    IconButton(onClick = { 
                        navController.navigate(Screen.ThemeCustomization.route) 
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Brush,
                            contentDescription = "Theme Settings"
                        )
                    }
                    IconButton(onClick = { 
                        navController.navigate(Screen.LanguageSelection.route) 
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Language,
                            contentDescription = "Language Settings"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // App Logo with click detection
                Image(
                    painter = painterResource(id = R.drawable.ic_comeback_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(150.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { viewModel.onLogoClick() }
                            )
                        },
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // App Title
                Text(
                    text = "Comeback",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // App Description
                Text(
                    text = "Check if your device is compatible with this app",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Install Button
                InstallButton(
                    onClick = {
                        // Play sound and vibration
                        FeedbackUtils.vibrate(context)
                        FeedbackUtils.playSound(context, R.raw.button_click)
                        
                        // Generate rejection message
                        viewModel.generateRejectionMessage()
                    },
                    isLoading = viewModel.isLoading
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Custom Message Button
                SettingsButton(
                    icon = Icons.Rounded.Edit,
                    text = "Create Personalized Message",
                    onClick = {
                        navController.navigate(Screen.CustomMessage.route)
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Smart Chat Button
                SettingsButton(
                    icon = Icons.Rounded.Chat,
                    text = "Smart Chat Mode (AI Comebacks)",
                    onClick = {
                        navController.navigate(Screen.Chat.route)
                    }
                )
            }
            
            // Show rejection message if available
            if (viewModel.showRejectionMessage) {
                RejectionMessageCard(
                    message = viewModel.rejectionMessage,
                    onDismiss = { viewModel.showRejectionMessage = false },
                    onShare = {
                        shareMessage(context, viewModel.rejectionMessage)
                    }
                )
            }
            
            // Show Easter egg if triggered
            if (viewModel.showEasterEgg) {
                when (viewModel.easterEggGame) {
                    EasterEggType.COLOR_MATCH -> ColorMatchGame(onDismiss = { viewModel.dismissEasterEgg() })
                    EasterEggType.MEMORY_GAME -> MemoryGame(onDismiss = { viewModel.dismissEasterEgg() })
                    EasterEggType.SPIN_LOGO -> SpinLogoGame(onDismiss = { viewModel.dismissEasterEgg() })
                    else -> { /* No game */ }
                }
            }
        }
    }
}

private fun shareMessage(context: Context, message: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
    
    val shareIntent = Intent.createChooser(sendIntent, "Share via")
    context.startActivity(shareIntent)
}

// Easter Egg mini-games
@Composable
fun ColorMatchGame(onDismiss: () -> Unit) {
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan)
    var targetColor by remember { mutableStateOf(colors.random()) }
    var options by remember { mutableStateOf(generateColorOptions(targetColor, colors)) }
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var gameActive by remember { mutableStateOf(true) }
    
    // Timer logic
    LaunchedEffect(key1 = Unit) {
        while (timeLeft > 0 && gameActive) {
            delay(1000)
            timeLeft--
        }
        gameActive = false
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Color Match Game",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Game info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Score: $score")
                    Text(text = "Time: $timeLeft")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Target color to match
                if (gameActive) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(targetColor)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(text = "Tap the matching color")
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Color options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        options.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable {
                                        if (color == targetColor) {
                                            score++
                                            targetColor = colors.random()
                                            options = generateColorOptions(targetColor, colors)
                                        } else {
                                            // Wrong choice penalty
                                            timeLeft = (timeLeft - 2).coerceAtLeast(0)
                                        }
                                    }
                            )
                        }
                    }
                } else {
                    // Game over
                    Text(
                        text = "Game Over!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Final Score: $score",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MemoryGame(onDismiss: () -> Unit) {
    val emojis = listOf("🍎", "🍌", "🍇", "🍓", "🍒", "🍊", "🥝", "🍉")
    val gameEmojis = remember { (emojis + emojis).shuffled() }
    var flippedIndices by remember { mutableStateOf(setOf<Int>()) }
    var matchedPairs by remember { mutableStateOf(setOf<Int>()) }
    var tempFlipped by remember { mutableStateOf<Int?>(null) }
    var moves by remember { mutableStateOf(0) }
    
    // Check for match logic
    LaunchedEffect(tempFlipped) {
        if (tempFlipped != null && flippedIndices.size == 2) {
            val indices = flippedIndices.toList()
            delay(1000) // Show cards briefly
            
            if (gameEmojis[indices[0]] == gameEmojis[indices[1]]) {
                // Match found
                matchedPairs = matchedPairs.plus(indices)
            }
            
            // Reset flipped cards
            flippedIndices = emptySet()
            tempFlipped = null
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Memory Game",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(text = "Moves: $moves")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Game grid - 4x4
                if (matchedPairs.size == gameEmojis.size) {
                    // Game completed
                    Text(
                        text = "Well done!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Completed in $moves moves",
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    // Game grid
                    Column {
                        for (row in 0..3) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (col in 0..3) {
                                    val index = row * 4 + col
                                    if (index < gameEmojis.size) {
                                        MemoryCard(
                                            emoji = gameEmojis[index],
                                            isFaceUp = index in flippedIndices || index in matchedPairs,
                                            isMatched = index in matchedPairs,
                                            onClick = {
                                                // Handle card click
                                                if (index !in flippedIndices && index !in matchedPairs && flippedIndices.size < 2) {
                                                    flippedIndices = flippedIndices.plus(index)
                                                    tempFlipped = index
                                                    moves++
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isFaceUp) {
                Text(
                    text = emoji,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun SpinLogoGame(onDismiss: () -> Unit) {
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(15) }
    var gameActive by remember { mutableStateOf(true) }
    
    // Animation for spinning logo
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
    
    // Timer logic
    LaunchedEffect(key1 = Unit) {
        while (timeLeft > 0 && gameActive) {
            delay(1000)
            timeLeft--
        }
        gameActive = false
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Spin Master",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Game info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Score: $score")
                    Text(text = "Time: $timeLeft")
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                if (gameActive) {
                    // Spinning logo to tap
                    Image(
                        painter = painterResource(id = R.drawable.ic_comeback_logo),
                        contentDescription = "Spinning Logo",
                        modifier = Modifier
                            .size(120.dp)
                            .rotate(rotation)
                            .clickable {
                                score++
                            }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Tap the logo as fast as you can!",
                        textAlign = TextAlign.Center
                    )
                } else {
                    // Game over
                    Text(
                        text = "Game Over!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Final Score: $score",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

// Helper function to generate color options (one correct, others random)
private fun generateColorOptions(target: Color, allColors: List<Color>): List<Color> {
    val options = mutableListOf<Color>()
    options.add(target)
    
    // Add 2 random different colors
    while (options.size < 3) {
        val randomColor = allColors.random()
        if (randomColor != target && randomColor !in options) {
            options.add(randomColor)
        }
    }
    
    return options.shuffled()
} 
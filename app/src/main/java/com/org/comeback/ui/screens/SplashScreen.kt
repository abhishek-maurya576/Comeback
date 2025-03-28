package com.org.comeback.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.org.comeback.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateNext: () -> Unit) {
    val rotation = remember { Animatable(0f) }
    
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
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // App logo with rotation animation
        Image(
            painter = painterResource(id = R.drawable.ic_comeback_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .rotate(rotation.value),
            contentScale = ContentScale.Fit
        )
    }
} 
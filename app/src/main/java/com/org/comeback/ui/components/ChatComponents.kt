package com.org.comeback.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.InsertEmoticon
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.comeback.data.models.ChatMessage
import com.org.comeback.data.models.RoastType
import com.org.comeback.util.FeedbackUtils
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Type a message...") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            maxLines = 4,
            trailingIcon = {
                IconButton(
                    onClick = { /* Optional: Add emoji picker */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.InsertEmoticon,
                        contentDescription = "Emoji",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        IconButton(
            onClick = {
                if (value.isNotBlank() && !isLoading) {
                    onSend()
                    FeedbackUtils.vibrate(context)
                }
            },
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showMenu by remember { mutableStateOf(false) }
    
    val isFromUser = message.isFromUser
    val bubbleShape = if (isFromUser) {
        RoundedCornerShape(16.dp, 4.dp, 16.dp, 16.dp)
    } else {
        RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp)
    }
    
    val animationState = remember { MutableTransitionState(false).apply { targetState = true } }
    
    AnimatedVisibility(
        visibleState = animationState,
        enter = fadeIn(animationSpec = tween(300)) + 
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { if (isFromUser) 100 else -100 }
                ),
        exit = fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = if (isFromUser) Alignment.End else Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isFromUser) Arrangement.End else Arrangement.Start
            ) {
                Card(
                    shape = bubbleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFromUser) 
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .widthIn(max = 300.dp)
                        .clickable { showMenu = true }
                ) {
                    Box(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        if (isFromUser) {
                            Text(
                                text = message.content,
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            MarkdownText(
                                markdown = message.content,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                                linkColor = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Text(
                text = message.getFormattedTime(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(
                    start = if (isFromUser) 0.dp else 8.dp,
                    end = if (isFromUser) 8.dp else 0.dp
                )
            )
            
            // Menu for message actions (copy, share)
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Copy") },
                    leadingIcon = { 
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = "Copy"
                        )
                    },
                    onClick = {
                        clipboardManager.setText(AnnotatedString(message.content))
                        FeedbackUtils.vibrate(context)
                        showMenu = false
                    }
                )
                if (!isFromUser) {
                    DropdownMenuItem(
                        text = { Text("Share as Text") },
                        leadingIcon = { 
                            Icon(
                                imageVector = Icons.Rounded.Share,
                                contentDescription = "Share"
                            )
                        },
                        onClick = {
                            shareMessage(context, message.content)
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Share as Image") },
                        leadingIcon = { 
                            Icon(
                                imageVector = Icons.Rounded.Share,
                                contentDescription = "Share as Image"
                            )
                        },
                        onClick = {
                            // Use a simpler approach for now - just share as text
                            shareMessage(context, message.content)
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RoastTypeSelector(
    selectedType: RoastType,
    onTypeSelected: (RoastType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        RoastType.values().forEach { type ->
            val isSelected = selectedType == type
            val backgroundColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
            
            val textColor = if (isSelected) 
                MaterialTheme.colorScheme.onPrimaryContainer 
            else 
                MaterialTheme.colorScheme.onSurface
            
            val borderColor = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.outline
            
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .clickable { onTypeSelected(type) },
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                border = BorderStroke(1.dp, borderColor)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = type.displayName,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    var dotCount by remember { mutableStateOf(1) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            dotCount = (dotCount % 3) + 1
        }
    }
    
    // Clean up when leaving the composition
    DisposableEffect(Unit) {
        onDispose { }
    }
    
    Card(
        shape = RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .padding(8.dp)
            .widthIn(max = 100.dp)
    ) {
        Box(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = ".".repeat(dotCount),
                fontSize = 18.sp,
                letterSpacing = 2.sp
            )
        }
    }
}

// Helper functions for sharing
private fun shareMessage(context: android.content.Context, message: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
    
    val shareIntent = Intent.createChooser(sendIntent, "Share comeback via")
    context.startActivity(shareIntent)
} 
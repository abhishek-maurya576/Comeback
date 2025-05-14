package com.org.comeback.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.org.comeback.ui.components.AppTopBar
import com.org.comeback.ui.components.ChatInputField
import com.org.comeback.ui.components.MessageBubble
import com.org.comeback.ui.components.RoastTypeSelector
import com.org.comeback.ui.components.SettingsButton
import com.org.comeback.ui.components.TypingIndicator
import com.org.comeback.ui.viewmodels.ChatViewModel
import com.org.comeback.ui.viewmodels.ChatViewModelFactory
import com.org.comeback.util.FeedbackUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(context))
    
    val messages by viewModel.messages.collectAsState()
    val selectedRoastType = viewModel.selectedRoastType
    val isLoading = viewModel.isLoading
    
    var showSettingsDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    
    // Scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = "ComebackBot",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        modifier = Modifier.imePadding()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Roast type selector
                RoastTypeSelector(
                    selectedType = selectedRoastType,
                    onTypeSelected = { type ->
                        viewModel.selectedRoastType = type
                        FeedbackUtils.vibrate(context)
                    }
                )
                
                if (messages.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Welcome to ComebackBot!",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Send a message to get a witty comeback or roast.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Try saying:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            SuggestionItem("Roast me!")
                            SuggestionItem("I need a comeback for someone who called me lazy")
                            SuggestionItem("My friend says I'm always late")
                        }
                    }
                } else {
                    // Chat message list
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        state = listState
                    ) {
                        items(messages) { message ->
                            MessageBubble(message = message)
                        }
                        
                        if (isLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 8.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    TypingIndicator()
                                }
                            }
                        }
                    }
                }
                
                // Input field
                ChatInputField(
                    value = viewModel.inputText,
                    onValueChange = { viewModel.inputText = it },
                    onSend = { viewModel.sendMessage() },
                    isLoading = isLoading
                )
            }
            
            // Clear chat button
            if (messages.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        FeedbackUtils.vibrate(context)
                        viewModel.clearMessages()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 80.dp, end = 16.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear Chat",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            // Personality settings dialog
            if (showSettingsDialog) {
                PersonalitySettingsDialog(
                    viewModel = viewModel,
                    onDismiss = { showSettingsDialog = false }
                )
            }
        }
    }
}

@Composable
fun SuggestionItem(text: String) {
    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(context))
    
    SettingsButton(
        icon = Icons.Rounded.Person,
        text = text,
        onClick = {
            viewModel.inputText = text
            viewModel.sendMessage()
            FeedbackUtils.vibrate(context)
        },
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun PersonalitySettingsDialog(
    viewModel: ChatViewModel,
    onDismiss: () -> Unit
) {
    val aiPersonality by viewModel.aiPersonality.collectAsState()
    
    // Show a dialog with personality settings
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("AI Personality Settings") },
        text = {
            Column {
                Text("Select the AI's personality tone:")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Personality options
                listOf("Friendly", "Sarcastic", "Savage").forEach { personality ->
                    androidx.compose.material3.RadioButton(
                        selected = aiPersonality == personality,
                        onClick = {
                            viewModel.setAiPersonality(personality)
                        }
                    )
                    Text(personality)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
} 
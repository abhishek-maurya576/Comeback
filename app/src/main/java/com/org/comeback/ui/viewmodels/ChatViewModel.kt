package com.org.comeback.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.org.comeback.data.api.GeminiApi
import com.org.comeback.data.models.ChatMessage
import com.org.comeback.data.models.RoastType
import com.org.comeback.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChatViewModel(
    private val context: Context,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // UI state
    var inputText by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var selectedRoastType by mutableStateOf(RoastType.SARCASTIC)
    
    // Chat messages
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    // Currently selected AI personality
    private val _aiPersonality = MutableStateFlow<String>("Sarcastic")
    val aiPersonality: StateFlow<String> = _aiPersonality.asStateFlow()
    
    init {
        viewModelScope.launch {
            // Initialize with saved personality preference if available
            val savedPersonality = preferencesManager.aiPersonality.first()
            if (savedPersonality.isNotEmpty()) {
                _aiPersonality.value = savedPersonality
                
                // Set the roast type based on saved personality
                selectedRoastType = when (savedPersonality) {
                    "Friendly" -> RoastType.MILD
                    "Savage" -> RoastType.SAVAGE
                    else -> RoastType.SARCASTIC
                }
            }
        }
    }
    
    // Send a message and get AI response
    fun sendMessage() {
        if (inputText.isBlank()) return
        
        val userMessage = ChatMessage(
            id = System.currentTimeMillis(),
            content = inputText,
            isFromUser = true,
            timestamp = System.currentTimeMillis()
        )
        
        _messages.value = _messages.value + userMessage
        val userInput = inputText
        inputText = ""
        
        isLoading = true
        viewModelScope.launch {
            try {
                val aiResponse = GeminiApi.generateChatResponse(
                    userInput = userInput,
                    roastType = selectedRoastType
                )
                
                val aiMessage = ChatMessage(
                    id = System.currentTimeMillis(),
                    content = aiResponse,
                    isFromUser = false,
                    timestamp = System.currentTimeMillis()
                )
                
                _messages.value = _messages.value + aiMessage
            } catch (e: Exception) {
                // Handle error
                val errorMessage = ChatMessage(
                    id = System.currentTimeMillis(),
                    content = "Oops! My sarcasm module crashed. Maybe your message was too boring? Try again!",
                    isFromUser = false,
                    timestamp = System.currentTimeMillis()
                )
                _messages.value = _messages.value + errorMessage
            } finally {
                isLoading = false
            }
        }
    }
    
    // Set AI personality
    fun setAiPersonality(personality: String) {
        viewModelScope.launch {
            _aiPersonality.value = personality
            preferencesManager.setAiPersonality(personality)
            
            // Update roast type based on personality
            selectedRoastType = when (personality) {
                "Friendly" -> RoastType.MILD
                "Savage" -> RoastType.SAVAGE
                else -> RoastType.SARCASTIC
            }
        }
    }
    
    // Clear all messages
    fun clearMessages() {
        _messages.value = emptyList()
    }
}

class ChatViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(context, PreferencesManager(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 
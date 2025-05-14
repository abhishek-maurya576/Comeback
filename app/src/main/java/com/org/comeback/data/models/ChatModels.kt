package com.org.comeback.data.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val id: Long,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long
) {
    fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

enum class RoastType(val displayName: String, val description: String) {
    MILD("Friendly", "Light teasing with a friendly tone"),
    SARCASTIC("Sarcastic", "Classic sarcasm with wit and humor"),
    SAVAGE("Savage", "No-holds-barred roasting (but still fun)")
} 
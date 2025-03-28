# Gemini AI API Integration Guide

This document provides detailed information about how the Comeback app integrates with Google's Gemini AI API to generate humorous rejection messages and personalized content.

## Overview

The Comeback app uses Google's Gemini API to dynamically generate funny rejection messages that are shown to users when they attempt to "install" the app. This integration allows for fresh, unique content every time a user interacts with the app.

## API Setup

### API Key Configuration

The API key is defined in the `GeminiApiService.kt` file:

```kotlin
private const val API_KEY = "TODO"
private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
```

> **Security Note**: In a production environment, API keys should not be hardcoded. Consider using BuildConfig, environment variables, or a secure backend service to provide the key.

### API Service Interface

The app defines a Retrofit interface for the Gemini API:

```kotlin
interface GeminiApiService {
    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String = API_KEY,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}
```

### API Models

The app defines several data classes to model the request and response:

```kotlin
// Request models
data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

// Response models
data class GeminiResponse(
    val candidates: List<Candidate>?,
    val promptFeedback: PromptFeedback?
)

data class Candidate(
    val content: Content,
    val finishReason: String
)

data class PromptFeedback(
    val blockReason: String?
)
```

## HTTP Client Configuration

The app uses OkHttp with logging interceptor for better debugging:

```kotlin
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()
```

## API Functions

### Standard Rejection Messages

The app generates standard funny rejection messages using this function:

```kotlin
suspend fun generateFunnyRejectionMessage(language: String = "en"): String {
    Log.d(TAG, "Generating rejection message in language: $language")
    val promptTemplate = when (language) {
        "en" -> "Generate a funny rejection message for a fake app installation that makes the user smile. Keep it short (max 100 characters) and humorous."
        "es" -> "Genera un mensaje de rechazo divertido para una instalación falsa de aplicación que haga sonreír al usuario. Mantenlo breve (máximo 100 caracteres) y humorístico."
        // Additional languages...
    }
    
    // API call implementation
}
```

### Personalized Messages

The app can generate personalized messages that include the user's name:

```kotlin
suspend fun generatePersonalizedMessage(name: String, language: String = "en"): String {
    Log.d(TAG, "Generating personalized message for '$name' in language: $language")
    val promptTemplate = when (language) {
        "en" -> "Generate a funny rejection message for a fake app installation that mentions the name '$name'. Make it humorous and personal. Keep it short (max 120 characters)."
        // Additional languages...
    }
    
    // API call implementation
}
```

### Text Translation

The app can also translate messages to different languages:

```kotlin
suspend fun translateText(text: String, targetLanguage: String): String {
    val promptTemplate = "Translate the following text to $targetLanguage: \"$text\""
    
    // API call implementation
}
```

## Error Handling

The API implementation includes comprehensive error handling:

1. **API Response Errors**: Checks for successful HTTP response codes
2. **Network Exceptions**: Catches and handles network-related exceptions
3. **Fallback Messages**: Provides fallback messages if the API call fails

Example error handling:

```kotlin
return try {
    // API call
    if (response.isSuccessful) {
        // Process successful response
    } else {
        Log.e(TAG, "API error: ${response.code()} - ${response.errorBody()?.string()}")
        "Sorry, installation failed with error: You're too awesome! 🌟"
    }
} catch (e: Exception) {
    Log.e(TAG, "Exception in API call", e)
    "App rejected your install request. It said you're too fabulous! ✨"
}
```

## Multilingual Support

The API integration supports multiple languages by adapting the prompt based on the user's language preference:

| Language Code | Language |
|---------------|----------|
| en            | English  |
| es            | Spanish  |
| fr            | French   |
| hi            | Hindi    |

The language selection affects both the prompt sent to the API and the response formatting.

## Implementation in the View Layer

In the UI layer, the API calls are triggered from the `MainViewModel`:

```kotlin
fun generateRejectionMessage(personalized: Boolean = false) {
    isLoading = true
    viewModelScope.launch {
        try {
            rejectionMessage = if (personalized && userName.isNotBlank()) {
                GeminiApi.generatePersonalizedMessage(userName, _language.value)
            } else {
                GeminiApi.generateFunnyRejectionMessage(_language.value)
            }
        } catch (e: Exception) {
            rejectionMessage = "Oops! Something went wrong, but you're still awesome! 😎"
        } finally {
            isLoading = false
            showRejectionMessage = true
        }
    }
}
```

## Prompt Engineering

The prompts are carefully engineered to generate appropriate content:

1. **Clarity**: The prompts clearly specify the task (generate a funny rejection message)
2. **Constraints**: Length limits are specified (max 100-120 characters)
3. **Tone**: The desired tone is specified (humorous, makes the user smile)
4. **Personalization**: For personalized messages, the name is included in the prompt
5. **Multilingual**: Different prompts for different languages

## Performance Considerations

The Gemini API integration includes several performance optimizations:

1. **Timeout Settings**: Custom timeouts to handle slow connections
2. **Error Fallbacks**: Quick fallback messages when API calls fail
3. **Loading States**: UI feedback during API calls
4. **Coroutine Scopes**: API calls run in the appropriate coroutine scope

## Adding New API Features

To extend the Gemini API functionality:

1. **Define a new function in `GeminiApi`**:
   ```kotlin
   suspend fun yourNewFunction(params: YourParams): String {
       val promptTemplate = "Your prompt here"
       // Implementation
   }
   ```

2. **Add a new function in `MainViewModel`**:
   ```kotlin
   fun triggerNewFunction(params: YourParams) {
       viewModelScope.launch {
           // Implementation using GeminiApi.yourNewFunction
       }
   }
   ```

3. **Call the function from your UI**:
   ```kotlin
   viewModel.triggerNewFunction(yourParams)
   ```

## Troubleshooting

Common issues and solutions:

1. **API Key Issues**: Ensure the API key is valid and has the necessary permissions
2. **Network Problems**: Check for connection issues using the logging interceptor
3. **Rate Limiting**: Handle API rate limits with appropriate backoff strategies
4. **Content Filtering**: Be aware that Gemini may filter certain content that violates its policies
5. **Response Parsing**: Ensure proper null handling when parsing the API response

## Best Practices

1. **Limit API Calls**: Avoid unnecessary API calls to reduce costs and latency
2. **Cache Responses**: Consider caching responses for frequently used prompts
3. **Monitor Usage**: Track API usage to stay within quotas
4. **Handle Failures Gracefully**: Always provide fallback content
5. **Respect User Privacy**: Be transparent about data sent to external APIs 

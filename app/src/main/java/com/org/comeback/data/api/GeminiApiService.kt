package com.org.comeback.data.api

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.org.comeback.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// API key is now stored in BuildConfig instead of being hardcoded
private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
private const val TAG = "GeminiApi"

interface GeminiApiService {
    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String = BuildConfig.GEMINI_API_KEY,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}

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

object GeminiApi {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: GeminiApiService = retrofit.create(GeminiApiService::class.java)
    
    // Function to generate funny rejection messages
    suspend fun generateFunnyRejectionMessage(language: String = "en"): String {
        Log.d(TAG, "Generating rejection message in language: $language")
        val promptTemplate = when (language) {
            "en" -> "Generate a funny rejection message for a fake app installation that makes the user smile. Keep it short (max 100 characters) and humorous."
            "es" -> "Genera un mensaje de rechazo divertido para una instalación falsa de aplicación que haga sonreír al usuario. Mantenlo breve (máximo 100 caracteres) y humorístico."
            "fr" -> "Générez un message de rejet amusant pour une fausse installation d'application qui fait sourire l'utilisateur. Gardez-le court (max 100 caractères) et humoristique."
            "hi" -> "एक नकली ऐप इंस्टॉलेशन के लिए एक मजेदार अस्वीकृति संदेश बनाएं जो उपयोगकर्ता को मुस्कुराए। इसे छोटा (अधिकतम 100 अक्षर) और हास्यपूर्ण रखें।"
            else -> "Generate a funny rejection message for a fake app installation that makes the user smile. Keep it short (max 100 characters) and humorous."
        }
        
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(
                                text = promptTemplate
                            )
                        )
                    )
                )
            )
            
            // Log just the first few characters of the API key for security
            Log.d(TAG, "Sending request to Gemini API with key: ${BuildConfig.GEMINI_API_KEY.take(5)}...")
            val response = service.generateContent(request = request)
            Log.d(TAG, "Response received: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            
            if (response.isSuccessful) {
                val geminiResponse = response.body()
                Log.d(TAG, "Response body: $geminiResponse")
                val generatedText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                Log.d(TAG, "Generated text: $generatedText")
                
                generatedText ?: "Oops! Your phone is too cool for this app to handle! 😎"
            } else {
                Log.e(TAG, "API error: ${response.code()} - ${response.errorBody()?.string()}")
                "Sorry, installation failed with error: You're too awesome! 🌟"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in API call", e)
            "App rejected your install request. It said you're too fabulous! ✨"
        }
    }
    
    // Function to generate personalized funny rejection messages
    suspend fun generatePersonalizedMessage(name: String, language: String = "en"): String {
        Log.d(TAG, "Generating personalized message for '$name' in language: $language")
        val promptTemplate = when (language) {
            "en" -> "Generate a funny rejection message for a fake app installation that mentions the name '$name'. Make it humorous and personal. Keep it short (max 120 characters)."
            "es" -> "Genera un mensaje de rechazo divertido para una instalación falsa de aplicación que mencione el nombre '$name'. Hazlo humorístico y personal. Mantenlo breve (máximo 120 caracteres)."
            "fr" -> "Générez un message de rejet amusant pour une fausse installation d'application qui mentionne le nom '$name'. Rendez-le humoristique et personnel. Gardez-le court (max 120 caractères)."
            "hi" -> "एक नकली ऐप इंस्टॉलेशन के लिए एक मजेदार अस्वीकृति संदेश बनाएं जो नाम '$name' का उल्लेख करता है। इसे हास्यपूर्ण और व्यक्तिगत बनाएं। इसे छोटा (अधिकतम 120 अक्षर) रखें।"
            else -> "Generate a funny rejection message for a fake app installation that mentions the name '$name'. Make it humorous and personal. Keep it short (max 120 characters)."
        }
        
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(
                                text = promptTemplate
                            )
                        )
                    )
                )
            )
            
            Log.d(TAG, "Sending personalized request to Gemini API...")
            val response = service.generateContent(request = request)
            Log.d(TAG, "Personalized response received: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            
            if (response.isSuccessful) {
                val geminiResponse = response.body()
                Log.d(TAG, "Personalized response body: $geminiResponse")
                val generatedText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                Log.d(TAG, "Personalized text: $generatedText")
                
                generatedText ?: "Hey $name! Our compatibility test shows you're too awesome for this app! 😎"
            } else {
                Log.e(TAG, "API error in personalized message: ${response.code()} - ${response.errorBody()?.string()}")
                "Sorry $name, this app can't handle your coolness level! 🌟"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in personalized API call", e)
            "$name, the app says you're too fabulous to install it! ✨"
        }
    }
    
    // Function to translate a text to another language
    suspend fun translateText(text: String, targetLanguage: String): String {
        val promptTemplate = "Translate the following text to $targetLanguage: \"$text\""
        
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(
                                text = promptTemplate
                            )
                        )
                    )
                )
            )
            
            val response = service.generateContent(request = request)
            if (response.isSuccessful) {
                val geminiResponse = response.body()
                val translatedText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                
                translatedText ?: text
            } else {
                text
            }
        } catch (e: Exception) {
            text
        }
    }
} 
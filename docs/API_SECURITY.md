# API Key Security

This document explains how API keys and sensitive information are managed securely in the Comeback App.

## Overview

The Comeback App integrates with Google's Gemini API to generate humorous content. The API requires an API key for authentication, which is a sensitive piece of information that should never be exposed publicly or committed to version control.

## Security Implementation

### Local Properties Approach

In this project, we use the `local.properties` file to store the API key. This file is:

1. Listed in `.gitignore` to ensure it's not committed to the repository
2. Specific to each developer's local environment
3. Automatically created by Android Studio but can be manually edited

### How It Works

1. The API key is stored in `local.properties`:
   ```properties
   gemini.api.key=YOUR_API_KEY_HERE
   ```

2. The `build.gradle.kts` file reads this property during build time:
   ```kotlin
   val localProperties = java.util.Properties().apply {
       val localPropertiesFile = rootProject.file("local.properties")
       if (localPropertiesFile.exists()) {
           load(java.io.FileInputStream(localPropertiesFile))
       }
   }
   
   val geminiApiKey = localProperties.getProperty("gemini.api.key") ?: ""
   ```

3. The key is injected into the app via `BuildConfig`:
   ```kotlin
   buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
   ```

4. The API service uses the key from `BuildConfig` instead of hardcoded values:
   ```kotlin
   @Query("key") apiKey: String = BuildConfig.GEMINI_API_KEY
   ```

## New Developer Setup

When a new developer clones the repository, they should:

1. Copy the `local.properties.example` file to `local.properties`
2. Obtain their own API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
3. Add their API key to the `local.properties` file
4. Never commit the `local.properties` file to the repository

## Potential Security Improvements

While this approach prevents API keys from being committed to the repository, there are additional security considerations for a production app:

1. **Encryption**: The API key in BuildConfig is stored as plain text in the APK. For higher security, consider encrypting the key and decrypting it at runtime.

2. **Backend Proxy**: Use a backend server to make API calls, so the keys never need to be in the client app.

3. **Key Rotation**: Regularly rotate API keys to minimize the impact of any potential leaks.

4. **API Key Restrictions**: Set up API key restrictions in the Google Cloud Console to limit usage by IP, app, or other criteria.

5. **Proguard/R8**: Ensure your obfuscation rules protect the BuildConfig class containing the API key.

## Handling API Keys in CI/CD

For continuous integration and deployment, you have a few options:

1. **Environment Variables**: Store the API key as an environment variable in your CI/CD system
2. **Secret Management**: Use the secret management feature of your CI/CD provider (GitHub Secrets, GitLab CI/CD Variables, etc.)
3. **Dummy Keys for Testing**: Use dummy/testing API keys for automated tests

## References

- [Android Security: Storing API Keys](https://developer.android.com/training/articles/keystore)
- [Google Cloud API Key Best Practices](https://cloud.google.com/docs/authentication/api-keys)
- [Securing API Keys in Android](https://medium.com/firebase-developers/securing-api-keys-in-android-57ef6d483414) 
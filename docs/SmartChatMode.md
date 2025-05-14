# Smart Chat Mode Feature Documentation

## Overview
Smart Chat Mode is an interactive chat feature in the Comeback app that generates witty AI responses using the Google Gemini API. It provides users with entertaining comeback responses in various tones, from mild to savage.

## Key Features
- **Multiple Roast Types**: Choose between Mild, Sarcastic, and Savage response styles
- **Rich Text Formatting**: Responses include markdown formatting with bold and italic text
- **Message Sharing**: Share witty comebacks with friends via standard Android sharing
- **Clean Modern UI**: Intuitive chat interface with Jetpack Compose Material3 design

## Technical Implementation

### Components
- **Chat UI**: Built with Jetpack Compose for a responsive and modern interface
- **Gemini API Integration**: Securely connects to Google's Gemini API for AI-generated responses
- **Markdown Rendering**: Uses compose-markdown library to display formatted text

### Dependencies
- Retrofit for API communication
- Compose Markdown for text formatting
- Material3 components for UI elements

### API Configuration
The feature requires a valid Gemini API key stored in local.properties as:
```
gemini.api.key=YOUR_API_KEY
```

## User Experience Flow
1. User selects a roast type (Mild, Sarcastic, or Savage)
2. User enters a message in the input field
3. System sends the message to Gemini API with appropriate prompt engineering
4. Formatted response appears in the chat window
5. User can delete messages or share responses

## Future Enhancements
- Message persistence across app sessions
- Additional personality types
- Voice input/output options
- Image-based comebacks 
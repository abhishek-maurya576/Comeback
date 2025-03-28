# Comeback App

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com/)
[![Language](https://img.shields.io/badge/language-Kotlin-orange.svg)](https://kotlinlang.org/)

A fun Android app that pretends to check if your device is compatible with the app, but always shows humorous rejection messages. Built with Jetpack Compose and Google's Gemini AI.

## 🌟 Features

- **Fake Compatibility Check**: Generates funny rejection messages
- **AI-Generated Humor**: Uses Gemini AI for unique messages each time
- **Personalization**: Create customized messages with names
- **Easter Eggs**: Hidden mini-games (tap the app logo 3 times to discover)
- **Multilingual**: Supports English, Spanish, French, and Hindi
- **Theme Customization**: Light/dark mode and color themes
- **Onboarding Experience**: Smooth introduction for new users

## 📱 Screenshots

[Screenshots will be added here]

## 🚀 Getting Started

### Prerequisites

- Android Studio Flamingo (2023.2.1) or higher
- JDK 11 or higher
- Android SDK 35
- Kotlin 1.9.0+

### Clone the Repository

```bash
git clone https://github.com/yourusername/comeback-app.git
cd comeback-app
```

### API Key Setup

This app uses Google's Gemini API which requires an API key:

1. Get your API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Copy `local.properties.example` to `local.properties`
3. Add your API key to the file: `gemini.api.key=YOUR_API_KEY_HERE`

> ⚠️ **Important**: Never commit your `local.properties` file to the repository

### Build and Run

Open the project in Android Studio and run it on an emulator or physical device.

## 🔨 Project Structure

```
com.org.comeback/
├── data/                      # Data layer
│   ├── api/                   # API service & models
│   └── preferences/           # User preferences
├── ui/                        # UI layer
│   ├── components/            # Reusable UI components
│   ├── navigation/            # Navigation setup
│   ├── screens/               # App screens
│   ├── theme/                 # Theme definition
│   └── viewmodels/            # ViewModels
├── util/                      # Utilities
└── MainActivity.kt            # Entry point
```

## 📚 Documentation

See the [docs](docs/) directory for detailed documentation:

- [User Guide](docs/USER_GUIDE.md)
- [Developer Guide](docs/DEVELOPER_GUIDE.md)
- [API Security](docs/API_SECURITY.md)
- [Full Documentation Index](docs/INDEX.md)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the submission process.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Google Gemini API for providing the AI functionality
- Jetpack Compose for the modern UI toolkit
- All contributors who have helped shape this project 
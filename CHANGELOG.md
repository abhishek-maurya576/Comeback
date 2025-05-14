# Changelog

All notable changes to the Comeback App will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.0.0-beta] - 2023-10-15

### Added
- **Smart Chat Mode**: New interactive AI chat functionality using Google Gemini API
  - Multiple personality options (Mild, Sarcastic, Savage)
  - Rich text formatting with markdown support
  - Message sharing capabilities
  - Clean, modern UI with Material3 design
- Comprehensive documentation in the `docs/` directory

### Improved
- Updated UI components across the application
- Enhanced performance with Jetpack Compose optimizations
- Improved error handling and network request management

### Technical
- Added Gemini API integration for Smart Chat Mode
- Implemented compose-markdown library for text formatting
- Expanded build configuration to support API key management
- Updated target SDK to Android 35

## [1.0.0] - 2023-05-28

### Initial Release
- Core application functionality
- Basic UI components and navigation

### Added
- App onboarding experience for first-time users
- Easter eggs with three mini-games (Color Match, Memory Game, Spin Logo)
- Enhanced logging for API requests using OkHttp interceptor
- Dark/light theme toggle
- Theme color customization with 5 color options
- Multiple language support (English, Spanish, French, Hindi)
- Personalized message feature
- Basic "compatibility check" functionality
- Gemini API integration for generating humorous content
- Splash screen with animated logo
- Material 3 design implementation

### Changed
- Improved error handling for network requests
- Updated navigation structure

### Fixed
- API error handling edge cases
- UI alignment on different screen sizes 
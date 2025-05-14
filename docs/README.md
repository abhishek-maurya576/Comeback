# Comeback App - Developer Documentation

## Smart Chat Mode Setup

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 11
- A valid Google Gemini API key

### Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/abhishek-maurya576/comeback.git
   cd comeback
   ```

2. Add your Gemini API key:
   Create or edit the `local.properties` file in the project root and add:
   ```properties
   gemini.api.key=YOUR_API_KEY
   ```

3. Build and run the application:
   ```bash
   ./gradlew assembleDebug
   ```

### Project Structure

- `app/src/main/java/com/org/comeback/ui/screens/chat/` - Contains the Smart Chat Mode UI components
- `app/src/main/java/com/org/comeback/data/api/` - Contains the Gemini API service and models
- `app/src/main/java/com/org/comeback/viewmodel/` - Contains the ViewModel for the Smart Chat feature

### Key Features Documentation

For detailed information about the Smart Chat Mode feature, see [SmartChatMode.md](./SmartChatMode.md).

### Testing

To run the tests:
```bash
./gradlew test
```

### Troubleshooting

- **API Key Issues**: Ensure your Gemini API key is correctly set in `local.properties`
- **Rendering Problems**: If markdown doesn't render correctly, ensure the compose-markdown dependency is properly included
- **Compilation Errors**: Make sure you're using JDK 11 and the correct Kotlin version

### Contributing

1. Create a feature branch: `git checkout -b feature/your-feature-name`
2. Commit your changes: `git commit -m 'Add some feature'`
3. Push to the branch: `git push origin feature/your-feature-name`
4. Submit a pull request 
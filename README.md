# AI Voice Chat App

A modern Android application that enables users to have natural voice conversations with AI assistants. Built with Clean Architecture, Jetpack Compose, and cutting-edge speech technologies.

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue)
![License](https://img.shields.io/badge/license-MIT-green)

## Features

### 🎤 Voice Input & Output
- **Speech-to-Text**: Real-time speech recognition with visual feedback
- **Text-to-Speech**: Natural-sounding AI responses with configurable voice options

### 💬 Intelligent Conversations
- **AI Integration**: Powered by Google Gemini AI with contextual understanding
- **Chat History**: Persistent conversation storage with session management

### 📱 User Experience
- **Modern UI**: Built with Jetpack Compose and Material 3 Design
- **Network Awareness**: Intelligent handling of connectivity changes

### ⚙️ Customization
- **Voice Settings**: Choose between male/female voice options
- **Session Management**: Continue or delete a selected conversation from your chat history

## Demo Video

*[Add your app screenshots here]*

## Architecture

This project follows **Clean Architecture** principles with **MVI** pattern:

```
├── app/                           # Main application module
├── core/
│   ├── common/                    # Shared utilities and extensions
│   ├── data/                      # Data layer (repositories, APIs, database)
│   ├── domain/                    # Business logic (use cases, models)
│   ├── designsystem/              # UI components and theming
│   └── navigation/                # Navigation logic
└── feature/
    ├── chat/                      # Chat functionality
    ├── history/                   # Conversation history
    └── settings/                  # App settings
```

### Tech Stack

- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: Clean Architecture + MVI
- **Dependency Injection**: Hilt
- **Database**: Room with SQLite
- **Networking**: Retrofit + OkHttp + Moshi
- **AI Integration**: Google Generative AI Client (Gemini models)
- **Speech Recognition**: Android Speech Recognition API
- **Text-to-Speech**: Google Cloud Text-to-Speech API
- **Async Programming**: Kotlin Coroutines + Flow
- **Code Quality**: KtLint, Jacoco for test coverage

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK with API level 24+
- Kotlin 1.9.22 or later

### API Keys Setup

1. **Google Gemini AI API**:
   - Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
   - Generate an API key
   - Add to `secrets.properties`: `GEMINI_GENERATIVE_AI_API_KEY=your_api_key`

2. **Google Cloud Text-to-Speech API**:
   - Enable the API in [Google Cloud Console](https://console.cloud.google.com/)
   - Create credentials and get API key
   - Add to `secrets.properties`: `GOOGLE_CLOUD_TEXT_TO_SPEECH_API_KEY=your_api_key`

### Installation

1. Clone the repository:
```bash
git clone https://github.com/ahmetsirim/ai-voice-chat-app.git
cd ai-voice-chat-app
```

2. Create `secrets.properties` in the root directory:
```properties
GEMINI_GENERATIVE_AI_API_KEY=your_gemini_api_key
GEMINI_GENERATIVE_AI_MODEL_NAME=gemini-pro
GOOGLE_CLOUD_TEXT_TO_SPEECH_API_KEY=your_tts_api_key
```

3. Create `local.defaults.properties` (for fallback values):
```properties
GEMINI_GENERATIVE_AI_API_KEY=debug_dummy_key
GEMINI_GENERATIVE_AI_MODEL_NAME=gemini-pro
GOOGLE_CLOUD_TEXT_TO_SPEECH_API_KEY=debug_dummy_key
```

4. Build and run:
```bash
./gradlew assembleDebug
```

## Permissions

The app requires the following permissions:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Configuration

### Voice Settings

Currently limited to **en-GB voices**:
- **Female**: `en-GB-Standard-F`
- **Male**: `en-GB-Standard-D`

## Database Schema

The app uses Room database with three main entities:

- **chat_sessions**: Stores conversation sessions
- **chat_messages**: Stores individual messages with foreign key relationships
- **app_settings**: Stores user preferences and configuration

## Testing

Currently no unit tests are implemented, but the project is configured with
**JaCoCo** for coverage reporting and ready for test integration.

Run code quality checks:
```bash
./gradlew ktlintCheck
```

## CI Pipeline

The project includes a GitHub Actions pipeline for code quality and testing.

### Continuous Integration

- **Lint Check**: Code style validation with KtLint  
- **Unit Testing**: Jacoco coverage reports  
- **Artifacts**: Upload of test, lint, and coverage reports  
- **Pipeline Summary**: Clear results via GitHub Step Summary  

### Features

- Manual workflow dispatch with configurable options  
- Gradle caching for faster builds  
- Secure API key handling using GitHub secrets 

## Performance Considerations

- **Memory Management**: Automatic cleanup of speech recognition and TTS resources
- **Battery Optimisation**: Efficient coroutine usage with proper lifecycle management
- **Audio Processing**: Optimised audio file handling with temporary file cleanup

## Troubleshooting

### Common Issues

**Speech Recognition Not Working**:
- Ensure microphone permissions are granted
- Check device speech recognition availability

**TTS Audio Issues**:
- Check audio file permissions
- Ensure device audio system is not muted

**AI Response Errors**:
- Check network connectivity
- Review API rate limiting

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Follow code style guidelines
4. Commit your changes (`git commit -m 'Add amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a Pull Request

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use KtLint for code formatting

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, email ahmet.sirim@outlook.com or create an issue on GitHub.

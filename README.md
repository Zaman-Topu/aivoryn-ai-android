# Aivoryn AI Android

<div align="center">

![Android](https://img.shields.io/badge/Android-24%2B-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)
![AGP](https://img.shields.io/badge/AGP-9.1.1-34A853?logo=gradle&logoColor=white)
![License](https://img.shields.io/badge/License-Private-111827)

**A modern native Android AI assistant built with Kotlin, Jetpack Compose, Room, and llama.cpp-ready inference architecture.**

</div>

---

## ✨ Overview

Aivoryn AI is designed as a clean, production-style Android experience for local and model-driven chat workflows.  
It combines a Compose-first UI, local conversation storage, GGUF model metadata support, and streaming-style response generation.

---

## 🚀 Core Features

- 🧠 **Chat interface with streaming responses**
- 💾 **Conversation persistence** with Room + Flow
- 🗂️ **History drawer controls** (search, pin, archive, rename, delete)
- 📦 **Model management** (active model selection and metadata tracking)
- 📄 **Document attachment parsing** (text/PDF extraction utility)
- 🎙️ **Voice input support** with Android SpeechRecognizer
- 🎨 **Theme modes** with Material 3 Compose design system
- ⚙️ **Prompt formatting pipeline** for multiple model template styles
- 🔌 **llama.cpp JNI-ready integration path** with graceful fallback flow

---

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Architecture:** ViewModel + Repository + Room
- **Async:** Kotlin Coroutines + Flow
- **Persistence:** Room, DataStore
- **Networking/Serialization:** OkHttp, Retrofit, Moshi
- **AI/Platform:** Firebase AI SDK (configured dependency), native llama bridge hooks

---

## 📋 Requirements

- Android Studio (latest stable recommended)
- JDK 11+
- Android SDK (compileSdk 36)
- Android device/emulator running API 24+

---

## ⚡ Quick Start

```bash
git clone https://github.com/Zaman-Topu/aivoryn-ai-android.git
cd aivoryn-ai-android
./gradlew assembleDebug
```

APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Run unit tests:

```bash
./gradlew :app:testDebugUnitTest
```

---

## 🔐 Configuration

Environment template is available in:

```text
.env.example
```

If Gemini/Firebase AI key usage is needed, add values in `.env` based on the template notes.

---

## 📁 Project Structure

```text
app/src/main/java/com/example
├── data/          # Room entities, DAOs, repositories
├── inference/     # GGUF parsing, prompt formatting, inference engine
├── ui/            # Compose screens, components, theme
└── util/          # Device, document, speech, download helpers
```

---

## 🔒 Privacy Notes

- Audio permission is used for voice input functionality.
- User conversation data is stored locally via Room database.
- Model execution behavior depends on selected runtime/backend integration.

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Open a pull request

---

## 📌 Status

This project is actively evolving. UI, model runtime, and inference behavior may continue to be refined.

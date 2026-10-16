# Aivoryn AI — Native Android Local AI Assistant

**Aivoryn AI** is a 100% offline-first, native Android application built with Kotlin, Jetpack Compose, Material 3, and Room. It is engineered specifically for local on-device inference using the **GGUF** model format and low-memory targets such as the **OPPO CPH2269** (MediaTek MT6765, ~4 GB RAM).

---

## 📱 Target Device & Low-Memory Architecture

- **Primary Device Profile:** OPPO CPH2269 (Android 12, MediaTek MT6765, ~4 GB RAM)
- **Low-Memory Mitigations:**
  - **Bounded Context Window:** Defaults to 1024 tokens (configurable up to 2048) to prevent memory allocation blowouts.
  - **Bounded Threading:** Defaults to 2-4 CPU threads optimal for the MT6765 big.LITTLE core structure.
  - **Recommended Models:** Q4_K_M or Q3_K_S quantizations under 2.2 GB (e.g. Qwen2.5-1.5B, Llama-3.2-1B, SmolLM2-1.7B).
  - **On-Demand Lifecycle:** Explicit Load/Unload memory management in the Model Manager.
  - **OOM Safety:** Native RAM usage is guarded with clear UI warnings when free memory falls below 800 MB.

---

## 🚀 Key Features

| Feature | Status | Description |
| :--- | :---: | :--- |
| **Native Kotlin & Compose UI** | ✅ Active | Material 3 interface, Light, Dark & AMOLED Black themes |
| **Conversation Persistence** | ✅ Active | Room SQLite database with reactive `Flow` queries |
| **History Navigation Drawer** | ✅ Active | Search, Pin/Unpin, Archive, Rename, and Delete confirmations |
| **GGUF Binary Header Parser** | ✅ Active | Pure Kotlin binary parser reading GGUF magic, tensors, architecture & quant |
| **GGUF Model Manager** | ✅ Active | Android Storage Access Framework (SAF) import, model deletion, RAM status |
| **Document Attachment** | ✅ Active | Text & PDF extraction bounded to 12,000 characters |
| **Speech-to-Text Voice Input** | ✅ Active | Android `SpeechRecognizer` with real-time waveform state |
| **Prompt Templates** | ✅ Active | ChatML (`<\|im_start\|>`), Llama-3, Mistral, and Alpaca formats |
| **Think Mode** | ✅ Active | Expandable step-by-step reasoning view (`<think>...</think>`) |
| **Search Mode** | ✅ Active | Offline knowledge retrieval toggle indicator |
| **JNI llama.cpp Bridge** | ✅ Integrated | Full native interface (`libllama_jni.so`) with token streaming callback |

---

## 🛠️ Building in Android Studio

1. **Prerequisites:**
   - Android Studio Ladybug / Koala or newer
   - JDK 17 or JDK 21
   - Android SDK 36 (Android 12+ compatible)
   - Android NDK (r25c+) & CMake (for building the optional native C++ library)

2. **Clone & Open:**
   ```bash
   git clone <repo-url> AivorynAI
   cd AivorynAI
   ```
   Open the root project in Android Studio.

3. **Build APK via Gradle:**
   ```bash
   gradle assembleDebug
   ```
   The debug APK will be generated at:
   `app/build/outputs/apk/debug/app-debug.apk`

4. **Run Tests:**
   ```bash
   gradle :app:testDebugUnitTest
   ```

---

## 🧩 llama.cpp Native JNI Integration

The application contains the complete JNI bridge architecture in `LlamaCppInferenceEngine.kt`. When `libllama_jni.so` is present in `app/src/main/jniLibs/arm64-v8a/`, the engine dynamically binds to llama.cpp for native SIMD/NEON accelerated execution on the device CPU.

If building without the native library, the app operates in header inspection mode: users can import any standard `.gguf` file, and the app will inspect the magic header, report the quant type, model architecture, tensor counts, and provide honest readiness guidance.

---

## 🔒 Privacy & Permissions

- **`android.permission.RECORD_AUDIO`:** Used strictly when the microphone button is pressed for voice speech-to-text.
- **Storage Access:** Zero-permission Android Storage Access Framework (`OpenDocument`) is used for GGUF and document imports, ensuring complete Google Play compliance.
- **Zero Telemetry:** 100% on-device local execution. No telemetry or analytics.

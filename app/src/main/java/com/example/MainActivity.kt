package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inference.LlamaCppInferenceEngine
import com.example.ui.ChatScreen
import com.example.ui.ChatViewModel
import com.example.ui.ModelManagerViewModel
import com.example.ui.SplashScreen
import com.example.ui.theme.AivorynAITheme

class MainActivity : ComponentActivity() {

    private val inferenceEngine by lazy { LlamaCppInferenceEngine() }

    private val chatViewModel: ChatViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChatViewModel(application, inferenceEngine) as T
            }
        }
    }

    private val modelManagerViewModel: ModelManagerViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ModelManagerViewModel(application, inferenceEngine) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val preferences by chatViewModel.preferences.collectAsStateWithLifecycle()
            var isSplashVisible by remember { mutableStateOf(true) }

            AivorynAITheme(themeMode = preferences.themeMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Crossfade(
                        targetState = isSplashVisible,
                        label = "splashTransition"
                    ) { showSplash ->
                        if (showSplash) {
                            SplashScreen(
                                onAnimationFinished = { isSplashVisible = false }
                            )
                        } else {
                            ChatScreen(
                                chatViewModel = chatViewModel,
                                modelManagerViewModel = modelManagerViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

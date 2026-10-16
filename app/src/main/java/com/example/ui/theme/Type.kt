package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// 1. Anthropic Serif (Lora): Chatted assistant messages, editorial greetings, headlines
val AnthropicSerifFontFamily = FontFamily(
    Font(R.font.lora, FontWeight.Normal)
)

// 2. Anthropic Sans (Plus Jakarta Sans): Menus, navigation bar, UI buttons, headers
val AnthropicSansFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal)
)

// 3. Styrene B (Space Grotesk): Prompt box input field, search inputs, pills
val StyreneBFontFamily = FontFamily(
    Font(R.font.space_grotesk, FontWeight.Normal)
)

// 4. Anthropic Mono (JetBrains Mono): Code blocks, technical stats, tokens/sec
val AnthropicMonoFontFamily = FontFamily(
    Font(R.font.jetbrains_mono, FontWeight.Normal)
)

// Editorial serif typography inspired by Claude
val ClaudeSerifHeadline = TextStyle(
    fontFamily = AnthropicSerifFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 32.sp,
    lineHeight = 38.sp,
    letterSpacing = (-0.3).sp
)

val ClaudeSerifGreeting = TextStyle(
    fontFamily = AnthropicSerifFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 28.sp,
    lineHeight = 34.sp,
    letterSpacing = (-0.2).sp
)

val ClaudeSerifTitle = TextStyle(
    fontFamily = AnthropicSerifFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
)

val Typography = Typography(
    headlineLarge = ClaudeSerifHeadline,
    headlineMedium = ClaudeSerifGreeting,
    titleLarge = ClaudeSerifTitle,
    titleMedium = TextStyle(
        fontFamily = AnthropicSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = AnthropicSerifFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AnthropicSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AnthropicSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontFamily = AnthropicSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.3.sp
    )
)

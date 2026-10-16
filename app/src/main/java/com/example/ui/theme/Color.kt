package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Claude Design System Color Tokens
 * Accurately mapped to the official Claude Color Specification (Light & Dark modes)
 */

// ==========================================
// 1. BACKGROUND / SURFACE TOKENS
// ==========================================

// Primary Surface (Cards, elevated containers)
// Light: R 255, G 255, B 255 | Dark: R 48, G 48, B 46
val ColorBackgroundPrimaryLight = Color(0xFFFFFFFF)
val ColorBackgroundPrimaryDark = Color(0xFF30302E)

// Secondary Surface (Floating composers, inner cards, pill backgrounds)
// Light: R 245, G 244, B 237 | Dark: R 38, G 38, B 36
val ColorBackgroundSecondaryLight = Color(0xFFF5F4ED)
val ColorBackgroundSecondaryDark = Color(0xFF262624)

// Tertiary Background (Main page/canvas background)
// Light: R 250, G 249, B 245 | Dark: R 20, G 20, B 19
val ColorBackgroundTertiaryLight = Color(0xFFFAF9F5)
val ColorBackgroundTertiaryDark = Color(0xFF141413)

// Inverse Background (Contrasting pills, active states)
// Light: R 20, G 20, B 19 | Dark: R 250, G 249, B 245
val ColorBackgroundInverseLight = Color(0xFF141413)
val ColorBackgroundInverseDark = Color(0xFFFAF9F5)

// Ghost Background (Transparent interactive areas)
val ColorBackgroundGhostLight = Color(0x00FFFFFF)
val ColorBackgroundGhostDark = Color(0x0030302E)

// Disabled Background
// Light: R 255, G 255, B 255, A 0.5 | Dark: R 48, G 48, B 46, A 0.5
val ColorBackgroundDisabledLight = Color(0x80FFFFFF)
val ColorBackgroundDisabledDark = Color(0x8030302E)


// ==========================================
// 2. BACKGROUND / ACCENT TOKENS
// ==========================================

// Info Accent Background
// Light: R 44, G 132, B 219 (soft blue tint) | Dark: R 44, G 132, B 219 (dark slate)
val ColorBackgroundInfoLight = Color(0xFFD6E8FA)
val ColorBackgroundInfoDark = Color(0xFF1B324E)

// Danger Accent Background
// Light: R 224, G 67, B 67 | Dark: R 221, G 83, B 83
val ColorBackgroundDangerLight = Color(0xFFE04343)
val ColorBackgroundDangerDark = Color(0xFFDD5353)
val ColorBackgroundDangerContainerDark = Color(0xFF4A1818)

// Success Accent Background
// Light: R 116, G 173, B 42 | Dark: R 101, G 167, B 46
val ColorBackgroundSuccessLight = Color(0xFF74AD2A)
val ColorBackgroundSuccessDark = Color(0xFF65A72E)
val ColorBackgroundSuccessContainerDark = Color(0xFF183B19)

// Warning Accent Background
// Light: R 250, G 167, B 42 | Dark: R 250, G 167, B 42
val ColorBackgroundWarningLight = Color(0xFFFAA72A)
val ColorBackgroundWarningDark = Color(0xFF4A3412)


// ==========================================
// 3. TEXT / SURFACE TOKENS
// ==========================================

// Primary Text (Headlines, body messages, strong titles)
// Light: R 20, G 20, B 19 | Dark: R 250, G 249, B 245
val ColorTextPrimaryLight = Color(0xFF141413)
val ColorTextPrimaryDark = Color(0xFFFAF9F5)

// Secondary Text (Descriptions, subtitles, timestamps, placeholder)
// Light: R 61, G 61, B 58 | Dark: R 194, G 192, B 182
val ColorTextSecondaryLight = Color(0xFF3D3D3A)
val ColorTextSecondaryDark = Color(0xFFC2C0B6)

// Tertiary Text (Muted captions, hints)
// Light: R 115, G 114, B 108 | Dark: R 156, G 154, B 146
val ColorTextTertiaryLight = Color(0xFF73726C)
val ColorTextTertiaryDark = Color(0xFF9C9A92)

// Inverse Text (Text on inverse backgrounds)
// Light: R 255, G 255, B 255 | Dark: R 20, G 20, B 19
val ColorTextInverseLight = Color(0xFFFFFFFF)
val ColorTextInverseDark = Color(0xFF141413)

// Ghost Text (Subtle inactive icons, indicators)
val ColorTextGhostLight = Color(0x8073726C)
val ColorTextGhostDark = Color(0x809C9A92)

// Disabled Text
val ColorTextDisabledLight = Color(0x80141413)
val ColorTextDisabledDark = Color(0x80FAF9F5)


// ==========================================
// 4. TEXT / ACCENT TOKENS
// ==========================================

// Text Info: Light R 50, G 102, B 173 | Dark R 129, G 170, B 221
val ColorTextInfoLight = Color(0xFF3266AD)
val ColorTextInfoDark = Color(0xFF81AADD)

// Text Danger: Light R 127, G 44, B 40 | Dark R 238, G 136, B 132
val ColorTextDangerLight = Color(0xFF7F2C28)
val ColorTextDangerDark = Color(0xFFEE8884)

// Text Success: Light R 29, G 91, B 25 | Dark R 122, G 185, B 72
val ColorTextSuccessLight = Color(0xFF1D5B19)
val ColorTextSuccessDark = Color(0xFF7AB948)

// Text Warning: Light R 90, G 72, B 21 | Dark R 208, G 160, B 66
val ColorTextWarningLight = Color(0xFF5A4815)
val ColorTextWarningDark = Color(0xFFD0A042)


// ==========================================
// 5. BORDER / SURFACE TOKENS
// ==========================================

// Primary Border: Light R 31, G 30, B 29, A 0.40 | Dark R 222, G 220, B 209, A 0.40
val ColorBorderPrimaryLight = Color(0x661F1E1D)
val ColorBorderPrimaryDark = Color(0x66DEDCD1)

// Secondary Border: Light R 31, G 30, B 29, A 0.30 | Dark R 222, G 220, B 209, A 0.30
val ColorBorderSecondaryLight = Color(0x4D1F1E1D)
val ColorBorderSecondaryDark = Color(0x4DDEDCD1)

// Tertiary Border (Standard cards & list dividers): Light R 31, G 30, B 29, A 0.15 | Dark R 222, G 220, B 209, A 0.15
val ColorBorderTertiaryLight = Color(0x261F1E1D)
val ColorBorderTertiaryDark = Color(0x26DEDCD1)

// Inverse Border: Light R 255, G 255, B 255, A 0.30 | Dark R 20, G 20, B 19, A 0.15
val ColorBorderInverseLight = Color(0x4DFFFFFF)
val ColorBorderInverseDark = Color(0x26141413)

// Disabled Border
val ColorBorderDisabledLight = Color(0x1A1F1E1D)
val ColorBorderDisabledDark = Color(0x1ADEDCD1)


// ==========================================
// 6. BORDER / ACCENT TOKENS
// ==========================================

// Border Info: R 70, G 130, B 213
val ColorBorderInfoLight = Color(0xFF4682D5)
val ColorBorderInfoDark = Color(0xFF4682D5)

// Border Danger: Light R 167, G 51, B 57 | Dark R 205, G 92, B 88
val ColorBorderDangerLight = Color(0xFFA73339)
val ColorBorderDangerDark = Color(0xFFCD5C58)

// Border Success: Light R 67, G 116, B 38 | Dark R 89, G 145, B 48
val ColorBorderSuccessLight = Color(0xFF437426)
val ColorBorderSuccessDark = Color(0xFF599130)

// Border Warning: Light R 128, G 92, B 31 | Dark R 164, G 120, B 41
val ColorBorderWarningLight = Color(0xFF805C1F)
val ColorBorderWarningDark = Color(0xFFA47829)


// ==========================================
// 7. RING / OUTLINE TOKENS
// ==========================================

// Ring Primary: Light R 20, G 20, B 19, A 0.70 | Dark R 250, G 249, B 245, A 0.70
val ColorRingPrimaryLight = Color(0xB3141413)
val ColorRingPrimaryDark = Color(0xB3FAF9F5)

// Ring Secondary: Light R 61, G 61, B 58, A 0.70 | Dark R 194, G 192, B 182, A 0.15
val ColorRingSecondaryLight = Color(0xB33D3D3A)
val ColorRingSecondaryDark = Color(0x26C2C0B6)

// Ring Inverse: Light R 255, G 255, B 255, A 0.70 | Dark R 20, G 20, B 19, A 0.70
val ColorRingInverseLight = Color(0xB3FFFFFF)
val ColorRingInverseDark = Color(0xB3141413)


// ==========================================
// 8. CLAUDE BRAND SIGNATURE ACCENTS
// ==========================================

// Terracotta / Coral Radiant Star and Brand Highlight
val ClaudeCoral = Color(0xFFD97757)
val ClaudeCoralLight = Color(0xFFE88F72)
val ClaudeCoralDark = Color(0xFFB85D3F)
val ClaudeCoralContainer = Color(0xFF38231C)

// Legacy Aliases for seamless component compatibility
val ClaudeDarkBackground = ColorBackgroundTertiaryDark   // #141413
val ClaudeDarkSurface = ColorBackgroundPrimaryDark        // #30302E
val ClaudeDarkSurfaceVariant = ColorBackgroundSecondaryDark // #262624
val ClaudeDarkBorder = ColorBorderTertiaryDark           // rgba(222, 220, 209, 0.15)
val ClaudeDarkDivider = ColorBorderDisabledDark

val ClaudeTextPrimary = ColorTextPrimaryDark             // #FAF9F5
val ClaudeTextSecondary = ColorTextSecondaryDark         // #C2C0B6
val ClaudeTextMuted = ColorTextTertiaryDark              // #9C9A92

val ClaudePillBackground = ColorBackgroundSecondaryDark  // #262624
val ClaudePillBorder = ColorBorderTertiaryDark           // rgba(222, 220, 209, 0.15)
val ClaudePillText = ColorTextPrimaryDark                // #FAF9F5
val ClaudeSendButton = ColorTextPrimaryDark              // #FAF9F5
val ClaudeSendIcon = ColorTextPrimaryLight               // #141413

// Light Mode Aliases
val ClaudeLightBackground = ColorBackgroundTertiaryLight // #FAF9F5
val ClaudeLightSurface = ColorBackgroundPrimaryLight     // #FFFFFF
val ClaudeLightSurfaceVariant = ColorBackgroundSecondaryLight // #F5F4ED
val ClaudeLightBorder = ColorBorderTertiaryLight        // rgba(31, 30, 29, 0.15)
val ClaudeLightTextPrimary = ColorTextPrimaryLight       // #141413
val ClaudeLightTextSecondary = ColorTextSecondaryLight   // #3D3D3A

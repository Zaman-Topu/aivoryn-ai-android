package com.example.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Claude Design System - Border & Corner Radius Tokens
 * Mapped to the official Claude "Border" specification sheet:
 * - Radius: XS (4dp), Small (8dp), Medium (12dp), Large (16dp), XL (24dp), Full (Circle)
 * - Width: Regular (1dp)
 */

// ==========================================
// 1. CORNER RADIUS TOKENS
// ==========================================
val BorderRadiusXs: Dp = 4.dp
val BorderRadiusSm: Dp = 8.dp
val BorderRadiusMd: Dp = 12.dp
val BorderRadiusLg: Dp = 16.dp
val BorderRadiusXl: Dp = 24.dp
val BorderRadiusComposer: Dp = 26.dp
val BorderRadiusFull: Dp = 9999.dp

// ==========================================
// 2. BORDER WIDTH TOKENS
// ==========================================
val BorderWidthRegular: Dp = 1.dp

// ==========================================
// 3. COMPOSE SHAPES
// ==========================================
val ShapeXs = RoundedCornerShape(BorderRadiusXs)
val ShapeSm = RoundedCornerShape(BorderRadiusSm)
val ShapeMd = RoundedCornerShape(BorderRadiusMd)
val ShapeLg = RoundedCornerShape(BorderRadiusLg)
val ShapeXl = RoundedCornerShape(BorderRadiusXl)
val ShapeComposer = RoundedCornerShape(BorderRadiusComposer)
val ShapeFull = CircleShape

// Material 3 Shapes theme mapping
val Shapes = Shapes(
    extraSmall = ShapeXs,
    small = ShapeSm,
    medium = ShapeMd,
    large = ShapeLg,
    extraLarge = ShapeXl
)

// Standard Claude Border Stroke (border-width-regular)
fun claudeBorderStroke(
    color: Color = ColorBorderTertiaryDark,
    width: Dp = BorderWidthRegular
): BorderStroke = BorderStroke(width = width, color = color)

fun Modifier.claudeBorder(
    color: Color = ColorBorderTertiaryDark,
    shape: Shape = ShapeMd,
    width: Dp = BorderWidthRegular
): Modifier = this.border(
    border = BorderStroke(width = width, color = color),
    shape = shape
)

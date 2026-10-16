package com.example.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ClaudeCoral
import com.example.ui.theme.ClaudeDarkBackground
import com.example.ui.theme.ClaudeTextPrimary
import com.example.ui.theme.ClaudeTextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAnimationFinished: () -> Unit
) {
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(20f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    LaunchedEffect(Unit) {
        // Step 1: Scale in the emblem with spring
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = spring(dampingRatio = 0.65f, stiffness = 300f)
        )
        alpha.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(400)
        )

        // Step 2: Fade and glide in the editorial title
        textOffsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )
        textAlpha.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(500)
        )

        delay(800)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ClaudeDarkBackground)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onAnimationFinished()
            },
        contentAlignment = Alignment.Center
    ) {
        // Ambient background radial glow
        Box(
            modifier = Modifier
                .size(280.dp)
                .scale(pulseGlow)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            ClaudeCoral.copy(alpha = 0.18f),
                            Color(0xFF2A3A54).copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Radiant Emblem
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(scale.value)
                    .alpha(alpha.value),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_aivoryn_symbol),
                    contentDescription = "Aivoryn AI Logo",
                    tint = ClaudeCoral,
                    modifier = Modifier.size(68.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Editorial Brand Title
            Text(
                text = "Aivoryn",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = com.example.ui.theme.AnthropicSerifFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 34.sp,
                    letterSpacing = (-0.5).sp,
                    color = ClaudeTextPrimary
                ),
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .padding(top = textOffsetY.value.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "Offline-First Intelligence",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = com.example.ui.theme.AnthropicSansFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    letterSpacing = 1.2.sp,
                    color = ClaudeTextSecondary
                ),
                modifier = Modifier.alpha(textAlpha.value * 0.8f)
            )
        }
    }
}

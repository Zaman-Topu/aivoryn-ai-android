package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.theme.ClaudeIcons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ModelEntity
import com.example.ui.theme.ClaudeCoral
import com.example.ui.theme.ClaudeTextPrimary
import com.example.ui.theme.ClaudeTextSecondary
import java.util.Calendar

@Composable
fun BrandingHeader(
    activeModel: ModelEntity?,
    isIncognito: Boolean,
    userName: String = "Topu",
    onOpenModelManager: () -> Unit,
    onLearnMoreIncognito: () -> Unit,
    modifier: Modifier = Modifier
) {
    val greetingText = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 5..11 -> "Good morning, $userName"
            in 12..16 -> "Afternoon, $userName"
            in 17..21 -> "Evening, $userName"
            else -> "Good night, $userName"
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isIncognito) {
            // Incognito Ghost / Private Mode Emblem (Screenshot 2)
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF262624)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👻",
                    fontSize = 34.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Incognito chat",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = com.example.ui.theme.AnthropicSerifFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    color = ClaudeTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Incognito chats stay out of your history and search, and aren't saved to your local storage database.",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = com.example.ui.theme.AnthropicSansFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center
                ),
                color = ClaudeTextSecondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Learn more",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = com.example.ui.theme.AnthropicSansFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color(0xFFA5A59E)
                ),
                modifier = Modifier
                    .clickable { onLearnMoreIncognito() }
                    .padding(4.dp)
            )
        } else {
            // Standard Claude Welcome Screen (Screenshot 1)
            // Terracotta/Coral Radiant Star
            Box(
                modifier = Modifier
                    .size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_aivoryn_symbol),
                    contentDescription = "Aivoryn Star",
                    tint = ClaudeCoral,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Greeting in Claude Editorial Serif Typography (Screenshot 1: "Afternoon, Topu")
            Text(
                text = greetingText,
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = com.example.ui.theme.AnthropicSerifFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 28.sp,
                    letterSpacing = (-0.2).sp,
                    color = ClaudeTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtle On-Device Engine chip
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF20201E),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2C28)),
                modifier = Modifier.clickable { onOpenModelManager() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ClaudeIcons.Memory,
                        contentDescription = null,
                        tint = ClaudeCoral,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (activeModel != null) "${activeModel.name} • ${activeModel.quantType}" else "Local Offline Engine • 4GB Profile",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = ClaudeTextSecondary
                    )
                }
            }
        }
    }
}

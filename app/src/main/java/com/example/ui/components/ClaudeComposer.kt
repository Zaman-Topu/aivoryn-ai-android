package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ModelEntity
import com.example.ui.theme.ClaudeCoral
import com.example.ui.theme.ClaudeDarkBorder
import com.example.ui.theme.ClaudeDarkSurface
import com.example.ui.theme.ClaudeDarkSurfaceVariant
import com.example.ui.theme.ClaudePillBackground
import com.example.ui.theme.ClaudePillBorder
import com.example.ui.theme.ClaudePillText
import com.example.ui.theme.ClaudeSendButton
import com.example.ui.theme.ClaudeSendIcon
import com.example.ui.theme.ClaudeTextPrimary
import com.example.ui.theme.ClaudeTextSecondary
import com.example.util.ExtractedDocument

@Composable
fun ClaudeComposer(
    text: String,
    onTextChanged: (String) -> Unit,
    isGenerating: Boolean,
    isIncognito: Boolean,
    activeModel: ModelEntity?,
    attachedDocument: ExtractedDocument?,
    onRemoveAttachment: () -> Unit,
    isListening: Boolean,
    onOpenAddSheet: () -> Unit,
    onOpenModelSheet: () -> Unit,
    onMicClick: () -> Unit,
    onVoiceModeClick: () -> Unit,
    onSendClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // Attachment chip if present
        AnimatedVisibility(visible = attachedDocument != null) {
            if (attachedDocument != null) {
                Surface(
                    shape = com.example.ui.theme.ShapeMd,
                    color = ClaudeDarkSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(
                        com.example.ui.theme.BorderWidthRegular,
                        ClaudeDarkBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = com.example.ui.theme.ClaudeIcons.Document,
                            contentDescription = null,
                            tint = ClaudeCoral,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = attachedDocument.fileName,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = ClaudeTextPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = "${attachedDocument.fileSizeFormatted}${if (attachedDocument.isTruncated) " (extracted 12k chars)" else ""}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ClaudeTextSecondary
                            )
                        }
                        IconButton(
                            onClick = onRemoveAttachment,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = com.example.ui.theme.ClaudeIcons.Close,
                                contentDescription = "Remove attachment",
                                tint = ClaudeTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Floating Card Container (Exact Claude aesthetic)
        Surface(
            shape = com.example.ui.theme.ShapeComposer,
            color = ClaudeDarkSurface,
            border = androidx.compose.foundation.BorderStroke(
                width = com.example.ui.theme.BorderWidthRegular,
                color = if (isIncognito) Color(0xFF4A4A45) else ClaudeDarkBorder
            ),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Top subtle pro/engine strip inside the card (Screenshot 1 & 2)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isIncognito) "Incognito mode active" else "On-device GGUF Intelligence",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = ClaudeTextSecondary.copy(alpha = 0.8f)
                    )
                    Text(
                        text = if (isIncognito) "Private" else "Local Edge",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isIncognito) Color(0xFFA5A59E) else ClaudeCoral
                        )
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Input TextField (Styrene B font as per Claude interface)
                TextField(
                    value = text,
                    onValueChange = onTextChanged,
                    placeholder = {
                        Text(
                            text = when {
                                isListening -> "Listening... speak now"
                                isIncognito -> "Message Aivoryn or hold to talk"
                                else -> "Chat with Aivoryn..."
                            },
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = com.example.ui.theme.StyreneBFontFamily,
                                fontSize = 16.sp
                            ),
                            color = ClaudeTextSecondary.copy(alpha = 0.7f)
                        )
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = com.example.ui.theme.StyreneBFontFamily,
                        fontSize = 16.sp,
                        color = ClaudeTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("composer_input_field"),
                    maxLines = 5,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = ClaudeCoral,
                        focusedTextColor = ClaudeTextPrimary,
                        unfocusedTextColor = ClaudeTextPrimary
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                    keyboardActions = KeyboardActions(onDone = {
                        if (!isGenerating && text.isNotBlank()) onSendClick()
                    })
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom actions row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // '+' Plus button (Add to chat)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ClaudePillBackground)
                            .border(com.example.ui.theme.BorderWidthRegular, ClaudePillBorder, CircleShape)
                            .clickable { onOpenAddSheet() }
                            .testTag("composer_add_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = com.example.ui.theme.ClaudeIcons.Plus,
                            contentDescription = "Add to chat",
                            tint = ClaudePillText,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Model Selector Pill (e.g. [ Qwen 2.5 Medium ])
                    Surface(
                        shape = com.example.ui.theme.ShapeXl,
                        color = ClaudePillBackground,
                        border = androidx.compose.foundation.BorderStroke(com.example.ui.theme.BorderWidthRegular, ClaudePillBorder),
                        modifier = Modifier
                            .clickable { onOpenModelSheet() }
                            .testTag("composer_model_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val modelTitle = activeModel?.name?.take(14) ?: "Local 1.5B"
                            Text(
                                text = modelTitle,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                ),
                                color = ClaudeTextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = activeModel?.quantType ?: "Medium",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    color = ClaudeTextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // If text is entered or generating: show Send/Stop button
                    if (text.isNotBlank() || isGenerating) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isGenerating) ClaudeCoral else ClaudeSendButton)
                                .clickable {
                                    if (isGenerating) onStopClick() else onSendClick()
                                }
                                .testTag(if (isGenerating) "composer_stop_button" else "composer_send_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isGenerating) com.example.ui.theme.ClaudeIcons.Stop else com.example.ui.theme.ClaudeIcons.SendUp,
                                contentDescription = if (isGenerating) "Stop" else "Send",
                                tint = if (isGenerating) Color.White else ClaudeSendIcon,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        // Microphone Button
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ClaudePillBackground)
                                .border(com.example.ui.theme.BorderWidthRegular, ClaudePillBorder, CircleShape)
                            .clickable { onMicClick() }
                            .testTag("composer_mic_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            val micTint by animateColorAsState(
                                targetValue = if (isListening) ClaudeCoral else ClaudePillText,
                                label = "micTint"
                            )
                            Icon(
                                imageVector = com.example.ui.theme.ClaudeIcons.Mic,
                                contentDescription = "Voice dictation",
                                tint = micTint,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Waveform Voice Mode Button (Screenshot 1 & 2: Solid white circle with audio waveform icon)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ClaudeSendButton)
                                .clickable { onVoiceModeClick() }
                                .testTag("composer_voice_mode_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = com.example.ui.theme.ClaudeIcons.Waveform,
                                contentDescription = "Voice mode",
                                tint = ClaudeSendIcon,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

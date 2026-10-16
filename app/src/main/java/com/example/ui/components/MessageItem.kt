package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MessageEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageItem(
    message: MessageEntity,
    isStreaming: Boolean = false,
    onRegenerate: () -> Unit = {}
) {
    val isUser = message.role == "user"
    val context = LocalContext.current
    var isThinkExpanded by remember { mutableStateOf(message.isThinkExpanded) }

    val timeFormatted = remember(message.timestamp) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag(if (isUser) "user_message_item" else "assistant_message_item"),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // Aivoryn Assistant Avatar (Claude Coral Star)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(com.example.ui.theme.ClaudeCoralContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(com.example.R.drawable.ic_aivoryn_symbol),
                    contentDescription = "Aivoryn Avatar",
                    tint = com.example.ui.theme.ClaudeCoral,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // User Attachment Badge if present
            if (!message.attachedFileName.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = com.example.ui.theme.ClaudeDarkSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = com.example.ui.theme.ClaudeCoral
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = message.attachedFileName,
                            style = MaterialTheme.typography.labelSmall,
                            color = com.example.ui.theme.ClaudeTextSecondary
                        )
                    }
                }
            }

            // Message Bubble
            Surface(
                shape = RoundedCornerShape(
                    topStart = com.example.ui.theme.BorderRadiusLg,
                    topEnd = com.example.ui.theme.BorderRadiusLg,
                    bottomStart = if (isUser) com.example.ui.theme.BorderRadiusLg else com.example.ui.theme.BorderRadiusXs,
                    bottomEnd = if (isUser) com.example.ui.theme.BorderRadiusXs else com.example.ui.theme.BorderRadiusLg
                ),
                color = if (isUser) {
                    com.example.ui.theme.ClaudeDarkSurfaceVariant
                } else {
                    com.example.ui.theme.ClaudeDarkSurface
                },
                tonalElevation = 0.dp,
                border = androidx.compose.foundation.BorderStroke(
                    com.example.ui.theme.BorderWidthRegular,
                    com.example.ui.theme.ClaudeDarkBorder
                ),
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    // Assistant Think Block (if present)
                    if (!isUser && !message.thinkContent.isNullOrBlank()) {
                        Surface(
                            shape = com.example.ui.theme.ShapeSm,
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable { isThinkExpanded = !isThinkExpanded }
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = "Think",
                                        tint = Color(0xFF8B5CF6),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Thought Process",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = Color(0xFF8B5CF6)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = if (isThinkExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Toggle think process",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                AnimatedVisibility(visible = isThinkExpanded) {
                                    Text(
                                        text = message.thinkContent,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            lineHeight = 18.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Search Pill (if search was used)
                    if (!isUser && message.isSearchEnabled) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF06B6D4),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Search Mode (Local Knowledge Base)",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF06B6D4)
                            )
                        }
                    }

                    // Message Content
                    if (isUser) {
                        Text(
                            text = message.content,
                            style = androidx.compose.ui.text.TextStyle(
                                fontFamily = com.example.ui.theme.AnthropicSansFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.5.sp,
                                lineHeight = 22.sp
                            ),
                            color = com.example.ui.theme.ClaudeTextPrimary
                        )
                    } else {
                        MarkdownText(
                            text = message.content,
                            textColor = com.example.ui.theme.ClaudeTextPrimary
                        )

                        // Streaming typing indicator
                        if (isStreaming) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(12.dp),
                                    strokeWidth = 2.dp,
                                    color = com.example.ui.theme.ClaudeCoral
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Generating…",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = com.example.ui.theme.AnthropicSansFontFamily
                                    ),
                                    color = com.example.ui.theme.ClaudeCoral
                                )
                            }
                        }
                    }
                }
            }

            // Bottom metadata & action row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = com.example.ui.theme.AnthropicSansFontFamily
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )

                if (!isUser) {
                    if (message.tokensPerSecond != null && message.tokensPerSecond > 0f) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• ${String.format(Locale.US, "%.1f", message.tokensPerSecond)} t/s",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = com.example.ui.theme.AnthropicMonoFontFamily
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Aivoryn Message", message.content))
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy message",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    IconButton(
                        onClick = onRegenerate,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Regenerate response",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

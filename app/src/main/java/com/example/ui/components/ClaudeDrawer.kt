package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.theme.ClaudeIcons
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ConversationEntity
import com.example.ui.theme.ClaudeCoral
import com.example.ui.theme.ClaudeDarkBackground
import com.example.ui.theme.ClaudeDarkBorder
import com.example.ui.theme.ClaudeDarkSurface
import com.example.ui.theme.ClaudeTextPrimary
import com.example.ui.theme.ClaudeTextSecondary
import com.example.ui.theme.ColorBackgroundInverseDark
import com.example.ui.theme.ColorTextInverseDark

@Composable
fun ClaudeDrawerContent(
    conversations: List<ConversationEntity>,
    currentConversationId: String?,
    onSelectConversation: (String) -> Unit,
    onNewChat: () -> Unit,
    onOpenProjects: () -> Unit,
    onOpenCode: () -> Unit,
    onOpenArtifacts: () -> Unit,
    onOpenSettings: () -> Unit,
    onDeleteConversation: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(320.dp)
            .statusBarsPadding(),
        color = ClaudeDarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Large editorial brand title (Matching Screenshot 4)
            Text(
                text = "Aivoryn",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = com.example.ui.theme.AnthropicSerifFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 32.sp,
                    letterSpacing = (-0.4).sp,
                    color = ClaudeTextPrimary
                ),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Top Primary Links
            DrawerLinkItem(
                icon = ClaudeIcons.Chat,
                title = "Chats",
                isSelected = true,
                onClick = {}
            )
            DrawerLinkItem(
                icon = ClaudeIcons.Folder,
                title = "Projects",
                isSelected = false,
                onClick = onOpenProjects
            )
            DrawerLinkItem(
                icon = ClaudeIcons.Code,
                title = "Code",
                isSelected = false,
                onClick = onOpenCode
            )
            DrawerLinkItem(
                icon = ClaudeIcons.Artifacts,
                title = "Artifacts",
                isSelected = false,
                onClick = onOpenArtifacts
            )

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = Color(0xFF262624))

            Spacer(modifier = Modifier.height(16.dp))

            // Recents section header
            Text(
                text = "Recents",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                ),
                color = ClaudeTextSecondary,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Recents list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (conversations.isEmpty()) {
                    item {
                        Text(
                            text = "No recent chats yet",
                            style = MaterialTheme.typography.bodySmall,
                            color = ClaudeTextSecondary.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                } else {
                    items(conversations, key = { it.id }) { conv ->
                        RecentChatItem(
                            conversation = conv,
                            isSelected = conv.id == currentConversationId,
                            onClick = { onSelectConversation(conv.id) },
                            onDelete = { onDeleteConversation(conv.id) }
                        )
                    }
                }
            }

            // Bottom bar: Profile Avatar + New Chat Floating Pill Button (Screenshot 4)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Avatar (e.g. Coral/Terracotta circle with 'TP' initials)
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(ClaudeCoral)
                        .clickable { onOpenSettings() }
                        .testTag("drawer_profile_avatar"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "TP",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }

                // White "+ New chat" Pill button (Screenshot 4)
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = ColorBackgroundInverseDark,
                    modifier = Modifier
                        .clickable { onNewChat() }
                        .testTag("drawer_new_chat_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ClaudeIcons.Plus,
                            contentDescription = null,
                            tint = ColorTextInverseDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "New chat",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ColorTextInverseDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerLinkItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 11.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) ClaudeTextPrimary else ClaudeTextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 16.sp
            ),
            color = if (isSelected) ClaudeTextPrimary else ClaudeTextSecondary
        )
    }
}

@Composable
private fun RecentChatItem(
    conversation: ConversationEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ClaudeIcons.Chat,
            contentDescription = null,
            tint = if (isSelected) ClaudeTextPrimary else ClaudeTextSecondary,
            modifier = Modifier.size(17.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = conversation.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 15.sp
            ),
            color = if (isSelected) ClaudeTextPrimary else ClaudeTextPrimary.copy(alpha = 0.85f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Box {
            IconButton(
                onClick = { menuOpen = true },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = ClaudeIcons.More,
                    contentDescription = "Options",
                    tint = ClaudeTextSecondary.copy(alpha = 0.6f),
                    modifier = Modifier.size(14.dp)
                )
            }

            DropdownMenu(
                expanded = menuOpen,
                onDismissRequest = { menuOpen = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Delete chat", color = Color(0xFFEF4444)) },
                    onClick = {
                        menuOpen = false
                        onDelete()
                    }
                )
            }
        }
    }
}

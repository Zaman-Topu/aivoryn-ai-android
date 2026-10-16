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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.theme.ClaudeIcons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ModelEntity
import com.example.ui.theme.ClaudeCoral
import com.example.ui.theme.ClaudeDarkBorder
import com.example.ui.theme.ClaudeDarkSurface
import com.example.ui.theme.ClaudeDarkSurfaceVariant
import com.example.ui.theme.ClaudeTextPrimary
import com.example.ui.theme.ClaudeTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectorSheet(
    models: List<ModelEntity>,
    activeModel: ModelEntity?,
    effortLevel: String,
    onEffortLevelCycle: () -> Unit,
    onSelectModel: (String) -> Unit,
    onOpenMoreModels: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ClaudeDarkSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF42423E))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {
            // Header with Close 'X' and Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = ClaudeIcons.Close,
                        contentDescription = "Close",
                        tint = ClaudeTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "Select model",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = ClaudeTextPrimary,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Models Card Container (Claude styled list)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ClaudeDarkSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    if (models.isEmpty()) {
                        // Default built-in profile choices
                        ModelRowItem(
                            title = "Qwen 2.5 1.5B",
                            badge = "Recommended",
                            description = "Most efficient for simpler tasks",
                            isSelected = true,
                            onClick = { onDismiss() }
                        )
                        HorizontalDivider(color = Color(0xFF33332E), modifier = Modifier.padding(horizontal = 16.dp))
                        ModelRowItem(
                            title = "Llama 3.2 1B",
                            badge = "Fast",
                            description = "Fastest for quick answers",
                            isSelected = false,
                            onClick = { onDismiss() }
                        )
                        HorizontalDivider(color = Color(0xFF33332E), modifier = Modifier.padding(horizontal = 16.dp))
                        ModelRowItem(
                            title = "SmolLM2 1.7B",
                            badge = "Low-RAM",
                            description = "For 4GB memory devices (OPPO CPH2269)",
                            isSelected = false,
                            onClick = { onDismiss() }
                        )
                    } else {
                        models.forEachIndexed { index, model ->
                            val isSelected = model.id == activeModel?.id
                            ModelRowItem(
                                title = model.name,
                                badge = model.quantType,
                                description = "${model.architecture} • ${model.fileSizeBytes / (1024 * 1024)} MB",
                                isSelected = isSelected,
                                onClick = {
                                    onSelectModel(model.id)
                                    onDismiss()
                                }
                            )
                            if (index < models.size - 1) {
                                HorizontalDivider(color = Color(0xFF33332E), modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Effort & More Models rows (Matching Claude Screenshot 3)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ClaudeDarkSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Effort Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEffortLevelCycle() }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Effort",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = ClaudeTextPrimary,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = effortLevel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ClaudeTextSecondary
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = ClaudeIcons.ChevronRight,
                            contentDescription = null,
                            tint = ClaudeTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    HorizontalDivider(color = Color(0xFF33332E), modifier = Modifier.padding(horizontal = 16.dp))

                    // More Models Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDismiss()
                                onOpenMoreModels()
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "More models & GGUF import",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = ClaudeTextPrimary,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = ClaudeIcons.ChevronRight,
                            contentDescription = null,
                            tint = ClaudeTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ModelRowItem(
    title: String,
    badge: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = ClaudeTextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF2E2E3A)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium),
                        color = Color(0xFF88A0D0),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = ClaudeTextSecondary
            )
        }

        if (isSelected) {
            Icon(
                imageVector = ClaudeIcons.Check,
                contentDescription = "Selected",
                tint = Color(0xFF3B82F6),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

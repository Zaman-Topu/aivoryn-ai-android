package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Claude Custom Iconography System
 * Custom geometric vector icons matching the official Claude Icon Sheet (Stroke 1.5–1.75dp)
 * Replaces default Material 3 icons for a completely custom, distinctive Anthropic Claude aesthetic.
 */
object ClaudeIcons {

    private fun strokePath(
        builder: ImageVector.Builder,
        strokeWidth: Float = 1.75f,
        block: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit
    ) {
        builder.path(
            fill = null,
            fillAlpha = 1f,
            stroke = SolidColor(Color.White),
            strokeAlpha = 1f,
            strokeLineWidth = strokeWidth,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 4f,
            pathFillType = androidx.compose.ui.graphics.PathFillType.NonZero,
            pathBuilder = block
        )
    }

    private fun fillPath(
        builder: ImageVector.Builder,
        block: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit
    ) {
        builder.path(
            fill = SolidColor(Color.White),
            fillAlpha = 1f,
            stroke = null,
            pathFillType = androidx.compose.ui.graphics.PathFillType.NonZero,
            pathBuilder = block
        )
    }

    private fun androidx.compose.ui.graphics.vector.PathBuilder.cubicTo(
        x1: Float, y1: Float,
        x2: Float, y2: Float,
        x3: Float, y3: Float
    ) {
        curveTo(x1, y1, x2, y2, x3, y3)
    }

    // Plus (+)
    val Plus: ImageVector = ImageVector.Builder("ClaudePlus", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 5f); lineTo(12f, 19f)
            moveTo(5f, 12f); lineTo(19f, 12f)
        }
    }.build()

    // Send (Paper plane / direction arrow)
    val Send: ImageVector = ImageVector.Builder("ClaudeSend", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(21f, 3f); lineTo(10f, 14f)
            moveTo(21f, 3f); lineTo(14.5f, 21f); lineTo(10f, 14f); lineTo(3f, 9.5f); close()
        }
    }.build()

    // Up Arrow Send variant
    val SendUp: ImageVector = ImageVector.Builder("ClaudeSendUp", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 19f); lineTo(12f, 5f)
            moveTo(5f, 12f); lineTo(12f, 5f); lineTo(19f, 12f)
        }
    }.build()

    // Stop (Square stop button)
    val Stop: ImageVector = ImageVector.Builder("ClaudeStop", 24.dp, 24.dp, 24f, 24f).apply {
        fillPath(this) {
            moveTo(7.5f, 7.5f)
            lineTo(16.5f, 7.5f)
            lineTo(16.5f, 16.5f)
            lineTo(7.5f, 16.5f)
            close()
        }
    }.build()

    // Mic (Clean microphone outline)
    val Mic: ImageVector = ImageVector.Builder("ClaudeMic", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 3f)
            cubicTo(10.3f, 3f, 9f, 4.3f, 9f, 6f)
            lineTo(9f, 12f)
            cubicTo(9f, 13.7f, 10.3f, 15f, 12f, 15f)
            cubicTo(13.7f, 15f, 15f, 13.7f, 15f, 12f)
            lineTo(15f, 6f)
            cubicTo(15f, 4.3f, 13.7f, 3f, 12f, 3f)
            close()
            moveTo(6f, 10.5f)
            lineTo(6f, 11.5f)
            cubicTo(6f, 14.8f, 8.7f, 17.5f, 12f, 17.5f)
            cubicTo(15.3f, 17.5f, 18f, 14.8f, 18f, 11.5f)
            lineTo(18f, 10.5f)
            moveTo(12f, 17.5f); lineTo(12f, 21f)
            moveTo(8.5f, 21f); lineTo(15.5f, 21f)
        }
    }.build()

    // Waveform (Audio voice mode with 5 distinct bars)
    val Waveform: ImageVector = ImageVector.Builder("ClaudeWaveform", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this, strokeWidth = 2f) {
            moveTo(4.5f, 10f); lineTo(4.5f, 14f)
            moveTo(8.25f, 6.5f); lineTo(8.25f, 17.5f)
            moveTo(12f, 3.5f); lineTo(12f, 20.5f)
            moveTo(15.75f, 6.5f); lineTo(15.75f, 17.5f)
            moveTo(19.5f, 10f); lineTo(19.5f, 14f)
        }
    }.build()

    // Paperclip (Attachment)
    val Paperclip: ImageVector = ImageVector.Builder("ClaudePaperclip", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(21.4f, 11f)
            lineTo(12.2f, 20.2f)
            cubicTo(9.9f, 22.5f, 6.1f, 22.5f, 3.8f, 20.2f)
            cubicTo(1.5f, 17.9f, 1.5f, 14.1f, 3.8f, 11.8f)
            lineTo(13f, 2.6f)
            cubicTo(14.5f, 1.1f, 17f, 1.1f, 18.5f, 2.6f)
            cubicTo(20f, 4.1f, 20f, 6.6f, 18.5f, 8.1f)
            lineTo(9.3f, 17.3f)
            cubicTo(8.5f, 18.1f, 7.3f, 18.1f, 6.5f, 17.3f)
            cubicTo(5.7f, 16.5f, 5.7f, 15.3f, 6.5f, 14.5f)
            lineTo(15f, 6f)
        }
    }.build()

    // Camera
    val Camera: ImageVector = ImageVector.Builder("ClaudeCamera", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(4f, 7f); lineTo(7.5f, 7f); lineTo(9f, 5f); lineTo(15f, 5f); lineTo(16.5f, 7f); lineTo(20f, 7f)
            cubicTo(21.1f, 7f, 22f, 7.9f, 22f, 9f); lineTo(22f, 18f); cubicTo(22f, 19.1f, 21.1f, 20f, 20f, 20f)
            lineTo(4f, 20f); cubicTo(2.9f, 20f, 2f, 19.1f, 2f, 18f); lineTo(2f, 9f); cubicTo(2f, 7.9f, 2.9f, 7f, 4f, 7f)
            close()
            moveTo(12f, 9.5f)
            cubicTo(14.2f, 9.5f, 16f, 11.3f, 16f, 13.5f)
            cubicTo(16f, 15.7f, 14.2f, 17.5f, 12f, 17.5f)
            cubicTo(9.8f, 17.5f, 8f, 15.7f, 8f, 13.5f)
            cubicTo(8f, 11.3f, 9.8f, 9.5f, 12f, 9.5f)
            close()
        }
    }.build()

    // Photos / Gallery
    val Photos: ImageVector = ImageVector.Builder("ClaudePhotos", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(4f, 4f); lineTo(20f, 4f); cubicTo(21.1f, 4f, 22f, 4.9f, 22f, 6f); lineTo(22f, 18f)
            cubicTo(22f, 19.1f, 21.1f, 20f, 20f, 20f); lineTo(4f, 20f); cubicTo(2.9f, 20f, 2f, 19.1f, 2f, 18f); lineTo(2f, 6f)
            cubicTo(2f, 4.9f, 2.9f, 4f, 4f, 4f); close()
            moveTo(4f, 16f); lineTo(9f, 11f); lineTo(13f, 15f); lineTo(16f, 12f); lineTo(20f, 16f)
            moveTo(8.5f, 8.5f); lineTo(8.51f, 8.5f)
        }
    }.build()

    // Document
    val Document: ImageVector = ImageVector.Builder("ClaudeDocument", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(6f, 3f); lineTo(14f, 3f); lineTo(19f, 8f); lineTo(19f, 21f); lineTo(6f, 21f); close()
            moveTo(14f, 3f); lineTo(14f, 8f); lineTo(19f, 8f)
            moveTo(9f, 13f); lineTo(15f, 13f)
            moveTo(9f, 17f); lineTo(13f, 17f)
        }
    }.build()

    // Ghost (Claude Incognito Mode)
    val Ghost: ImageVector = ImageVector.Builder("ClaudeGhost", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(5f, 19f); lineTo(5f, 10f)
            cubicTo(5f, 6.1f, 8.1f, 3f, 12f, 3f)
            cubicTo(15.9f, 3f, 19f, 6.1f, 19f, 10f)
            lineTo(19f, 19f)
            lineTo(16.5f, 17.2f); lineTo(14f, 19f); lineTo(12f, 17.2f); lineTo(10f, 19f); lineTo(7.5f, 17.2f); close()
            moveTo(9.5f, 10f); lineTo(9.51f, 10f)
            moveTo(14.5f, 10f); lineTo(14.51f, 10f)
        }
    }.build()

    // Search
    val Search: ImageVector = ImageVector.Builder("ClaudeSearch", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(10.5f, 3f)
            cubicTo(14.6f, 3f, 18f, 6.4f, 18f, 10.5f)
            cubicTo(18f, 12.3f, 17.3f, 14f, 16.2f, 15.3f)
            lineTo(21.4f, 20.5f); lineTo(20f, 21.9f); lineTo(14.8f, 16.7f)
            cubicTo(13.5f, 17.5f, 12.1f, 18f, 10.5f, 18f)
            cubicTo(6.4f, 18f, 3f, 14.6f, 3f, 10.5f)
            cubicTo(3f, 6.4f, 6.4f, 3f, 10.5f, 3f)
            close()
        }
    }.build()

    // Menu (3 horizontal lines)
    val Menu: ImageVector = ImageVector.Builder("ClaudeMenu", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(4f, 7f); lineTo(20f, 7f)
            moveTo(4f, 12f); lineTo(20f, 12f)
            moveTo(4f, 17f); lineTo(20f, 17f)
        }
    }.build()

    // Close (×)
    val Close: ImageVector = ImageVector.Builder("ClaudeClose", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(6f, 6f); lineTo(18f, 18f)
            moveTo(18f, 6f); lineTo(6f, 18f)
        }
    }.build()

    // Arrow Back (←)
    val ArrowBack: ImageVector = ImageVector.Builder("ClaudeArrowBack", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(19f, 12f); lineTo(5f, 12f)
            moveTo(11f, 6f); lineTo(5f, 12f); lineTo(11f, 18f)
        }
    }.build()

    // Arrow Right (→)
    val ArrowRight: ImageVector = ImageVector.Builder("ClaudeArrowRight", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(5f, 12f); lineTo(19f, 12f)
            moveTo(13f, 6f); lineTo(19f, 12f); lineTo(13f, 18f)
        }
    }.build()

    // Chevron Right (>)
    val ChevronRight: ImageVector = ImageVector.Builder("ClaudeChevronRight", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(9f, 6f); lineTo(15f, 12f); lineTo(9f, 18f)
        }
    }.build()

    // Chevron Down (v)
    val ChevronDown: ImageVector = ImageVector.Builder("ClaudeChevronDown", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(6f, 9f); lineTo(12f, 15f); lineTo(18f, 9f)
        }
    }.build()

    // Chevron Up (^)
    val ChevronUp: ImageVector = ImageVector.Builder("ClaudeChevronUp", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(6f, 15f); lineTo(12f, 9f); lineTo(18f, 15f)
        }
    }.build()

    // Copy
    val Copy: ImageVector = ImageVector.Builder("ClaudeCopy", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(8f, 5f); lineTo(19f, 5f); cubicTo(20.1f, 5f, 21f, 5.9f, 21f, 7f); lineTo(21f, 18f)
            cubicTo(21f, 19.1f, 20.1f, 20f, 19f, 20f); lineTo(8f, 20f); cubicTo(6.9f, 20f, 6f, 19.1f, 6f, 18f)
            lineTo(6f, 7f); cubicTo(6f, 5.9f, 6.9f, 5f, 8f, 5f); close()
            moveTo(4f, 8f); lineTo(4f, 20f); cubicTo(4f, 21.1f, 4.9f, 22f, 6f, 22f); lineTo(17f, 22f)
        }
    }.build()

    // Check (✓)
    val Check: ImageVector = ImageVector.Builder("ClaudeCheck", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(4.5f, 12.5f); lineTo(9.5f, 17.5f); lineTo(19.5f, 6.5f)
        }
    }.build()

    // Regenerate / Refresh
    val Refresh: ImageVector = ImageVector.Builder("ClaudeRefresh", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(21f, 4f); lineTo(21f, 9.5f); lineTo(15.5f, 9.5f)
            moveTo(21f, 9.5f); cubicTo(19.5f, 5.5f, 16f, 3f, 12f, 3f); cubicTo(7f, 3f, 3f, 7f, 3f, 12f)
            cubicTo(3f, 17f, 7f, 21f, 12f, 21f); cubicTo(16.5f, 21f, 20.2f, 17.7f, 20.9f, 13.5f)
        }
    }.build()

    // Lightbulb / Thought Process
    val Lightbulb: ImageVector = ImageVector.Builder("ClaudeLightbulb", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(9f, 18f); lineTo(15f, 18f)
            moveTo(10f, 21f); lineTo(14f, 21f)
            moveTo(12f, 3f)
            cubicTo(7.6f, 3f, 4f, 6.6f, 4f, 11f)
            cubicTo(4f, 13.8f, 5.4f, 16.3f, 7.6f, 17.7f)
            lineTo(8.5f, 18f); lineTo(15.5f, 18f); lineTo(16.4f, 17.7f)
            cubicTo(18.6f, 16.3f, 20f, 13.8f, 20f, 11f)
            cubicTo(20f, 6.6f, 16.4f, 3f, 12f, 3f)
            close()
        }
    }.build()

    // Folder / Projects
    val Folder: ImageVector = ImageVector.Builder("ClaudeFolder", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(3f, 6f); lineTo(9f, 6f); lineTo(11f, 8f); lineTo(21f, 8f)
            lineTo(21f, 18f); cubicTo(21f, 19.1f, 20.1f, 20f, 19f, 20f); lineTo(3f, 20f); close()
        }
    }.build()

    // Code (< / >)
    val Code: ImageVector = ImageVector.Builder("ClaudeCode", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(8f, 8f); lineTo(4f, 12f); lineTo(8f, 16f)
            moveTo(16f, 8f); lineTo(20f, 12f); lineTo(16f, 16f)
            moveTo(14f, 5f); lineTo(10f, 19f)
        }
    }.build()

    // Artifacts / Blocks
    val Artifacts: ImageVector = ImageVector.Builder("ClaudeArtifacts", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(4f, 4f); lineTo(10f, 4f); lineTo(10f, 10f); lineTo(4f, 10f); close()
            moveTo(14f, 4f); lineTo(20f, 4f); lineTo(20f, 10f); lineTo(14f, 10f); close()
            moveTo(4f, 14f); lineTo(10f, 14f); lineTo(10f, 20f); lineTo(4f, 20f); close()
            moveTo(14f, 14f); lineTo(20f, 14f); lineTo(20f, 20f); lineTo(14f, 20f); close()
        }
    }.build()

    // Settings / Gear
    val Settings: ImageVector = ImageVector.Builder("ClaudeSettings", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 15f); cubicTo(13.66f, 15f, 15f, 13.66f, 15f, 12f); cubicTo(15f, 10.34f, 13.66f, 9f, 12f, 9f); cubicTo(10.34f, 9f, 9f, 10.34f, 9f, 12f); cubicTo(9f, 13.66f, 10.34f, 15f, 12f, 15f); close()
            moveTo(19.4f, 13f); lineTo(20.8f, 14.1f); lineTo(19.3f, 16.7f); lineTo(17.6f, 16.1f); cubicTo(17.1f, 16.5f, 16.5f, 16.9f, 15.9f, 17.1f); lineTo(15.5f, 18.9f); lineTo(12.5f, 18.9f); lineTo(12.1f, 17.1f); cubicTo(11.5f, 16.9f, 10.9f, 16.5f, 10.4f, 16.1f); lineTo(8.7f, 16.7f); lineTo(7.2f, 14.1f); lineTo(8.6f, 13f); cubicTo(8.5f, 12.7f, 8.5f, 12.3f, 8.5f, 12f); cubicTo(8.5f, 11.7f, 8.5f, 11.3f, 8.6f, 11f); lineTo(7.2f, 9.9f); lineTo(8.7f, 7.3f); lineTo(10.4f, 7.9f); cubicTo(10.9f, 7.5f, 11.5f, 7.1f, 12.1f, 6.9f); lineTo(12.5f, 5.1f); lineTo(15.5f, 5.1f); lineTo(15.9f, 6.9f); cubicTo(16.5f, 7.1f, 17.1f, 7.5f, 17.6f, 7.9f); lineTo(19.3f, 7.3f); lineTo(20.8f, 9.9f); lineTo(19.4f, 11f); cubicTo(19.5f, 11.3f, 19.5f, 11.7f, 19.5f, 12f); cubicTo(19.5f, 12.3f, 19.5f, 12.7f, 19.4f, 13f); close()
        }
    }.build()

    // Trash / Delete
    val Trash: ImageVector = ImageVector.Builder("ClaudeTrash", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(4f, 6f); lineTo(20f, 6f)
            moveTo(10f, 11f); lineTo(10f, 16f)
            moveTo(14f, 11f); lineTo(14f, 16f)
            moveTo(5f, 6f); lineTo(6.5f, 20f); cubicTo(6.6f, 20.6f, 7.1f, 21f, 7.7f, 21f); lineTo(16.3f, 21f); cubicTo(16.9f, 21f, 17.4f, 20.6f, 17.5f, 20f); lineTo(19f, 6f)
            moveTo(9f, 6f); lineTo(9.5f, 3.5f); lineTo(14.5f, 3.5f); lineTo(15f, 6f)
        }
    }.build()

    // Download
    val Download: ImageVector = ImageVector.Builder("ClaudeDownload", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 4f); lineTo(12f, 15f)
            moveTo(7f, 11f); lineTo(12f, 16f); lineTo(17f, 11f)
            moveTo(4f, 19f); lineTo(20f, 19f)
        }
    }.build()

    // Memory / Chip
    val Memory: ImageVector = ImageVector.Builder("ClaudeMemory", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(8f, 8f); lineTo(16f, 8f); lineTo(16f, 16f); lineTo(8f, 16f); close()
            moveTo(5f, 5f); lineTo(19f, 5f); lineTo(19f, 19f); lineTo(5f, 19f); close()
            moveTo(9f, 2f); lineTo(9f, 5f)
            moveTo(15f, 2f); lineTo(15f, 5f)
            moveTo(9f, 19f); lineTo(9f, 22f)
            moveTo(15f, 19f); lineTo(15f, 22f)
            moveTo(2f, 9f); lineTo(5f, 9f)
            moveTo(2f, 15f); lineTo(5f, 15f)
            moveTo(19f, 9f); lineTo(22f, 9f)
            moveTo(19f, 15f); lineTo(22f, 15f)
        }
    }.build()

    // Storage
    val Storage: ImageVector = ImageVector.Builder("ClaudeStorage", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(4f, 5f); lineTo(20f, 5f); lineTo(20f, 10f); lineTo(4f, 10f); close()
            moveTo(4f, 14f); lineTo(20f, 14f); lineTo(20f, 19f); lineTo(4f, 19f); close()
            moveTo(7f, 7.5f); lineTo(7.01f, 7.5f)
            moveTo(7f, 16.5f); lineTo(7.01f, 16.5f)
        }
    }.build()

    // Link
    val Link: ImageVector = ImageVector.Builder("ClaudeLink", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(10f, 13f); cubicTo(10.5f, 13.8f, 11.4f, 14.3f, 12.3f, 14.4f); cubicTo(13.3f, 14.5f, 14.2f, 14.2f, 14.9f, 13.5f); lineTo(17.9f, 10.5f); cubicTo(19.3f, 9.1f, 19.3f, 6.9f, 17.9f, 5.5f); cubicTo(16.5f, 4.1f, 14.3f, 4.1f, 12.9f, 5.5f); lineTo(11.4f, 7f)
            moveTo(14f, 11f); cubicTo(13.5f, 10.2f, 12.6f, 9.7f, 11.7f, 9.6f); cubicTo(10.7f, 9.5f, 9.8f, 9.8f, 9.1f, 10.5f); lineTo(6.1f, 13.5f); cubicTo(4.7f, 14.9f, 4.7f, 17.1f, 6.1f, 18.5f); cubicTo(7.5f, 19.9f, 9.7f, 19.9f, 11.1f, 18.5f); lineTo(12.6f, 17f)
        }
    }.build()

    // More Options (Vertical 3 dots)
    val More: ImageVector = ImageVector.Builder("ClaudeMore", 24.dp, 24.dp, 24f, 24f).apply {
        fillPath(this) {
            moveTo(12f, 6.5f); cubicTo(12.8f, 6.5f, 13.5f, 5.8f, 13.5f, 5f); cubicTo(13.5f, 4.2f, 12.8f, 3.5f, 12f, 3.5f); cubicTo(11.2f, 3.5f, 10.5f, 4.2f, 10.5f, 5f); cubicTo(10.5f, 5.8f, 11.2f, 6.5f, 12f, 6.5f); close()
            moveTo(12f, 13.5f); cubicTo(12.8f, 13.5f, 13.5f, 12.8f, 13.5f, 12f); cubicTo(13.5f, 11.2f, 12.8f, 10.5f, 12f, 10.5f); cubicTo(11.2f, 10.5f, 10.5f, 11.2f, 10.5f, 12f); cubicTo(10.5f, 12.8f, 11.2f, 13.5f, 12f, 13.5f); close()
            moveTo(12f, 20.5f); cubicTo(12.8f, 20.5f, 13.5f, 19.8f, 13.5f, 19f); cubicTo(13.5f, 18.2f, 12.8f, 17.5f, 12f, 17.5f); cubicTo(11.2f, 17.5f, 10.5f, 18.2f, 10.5f, 19f); cubicTo(10.5f, 19.8f, 11.2f, 20.5f, 12f, 20.5f); close()
        }
    }.build()

    // Star (Anthropic Claude 8-point emblem)
    val ClaudeStar: ImageVector = ImageVector.Builder("ClaudeStar", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this, strokeWidth = 2.2f) {
            // 8 radiating spokes
            moveTo(12f, 3f); lineTo(12f, 21f)
            moveTo(3f, 12f); lineTo(21f, 12f)
            moveTo(5.6f, 5.6f); lineTo(18.4f, 18.4f)
            moveTo(18.4f, 5.6f); lineTo(5.6f, 18.4f)
        }
    }.build()

    // Chat / Message Bubble
    val Chat: ImageVector = ImageVector.Builder("ClaudeChat", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(21f, 11.5f)
            cubicTo(21f, 16.2f, 16.97f, 20f, 12f, 20f)
            cubicTo(10.38f, 20f, 8.86f, 19.58f, 7.55f, 18.84f)
            lineTo(3f, 20f)
            lineTo(4.28f, 16.05f)
            cubicTo(3.48f, 14.73f, 3f, 13.18f, 3f, 11.5f)
            cubicTo(3f, 6.8f, 7.03f, 3f, 12f, 3f)
            cubicTo(16.97f, 3f, 21f, 6.8f, 21f, 11.5f)
            close()
        }
    }.build()

    // Globe / Web
    val Globe: ImageVector = ImageVector.Builder("ClaudeGlobe", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 22f)
            cubicTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
            cubicTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
            cubicTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            cubicTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
            close()
            moveTo(2f, 12f); lineTo(22f, 12f)
            moveTo(12f, 2f)
            cubicTo(14.5f, 5.5f, 16f, 8.6f, 16f, 12f)
            cubicTo(16f, 15.4f, 14.5f, 18.5f, 12f, 22f)
            moveTo(12f, 2f)
            cubicTo(9.5f, 5.5f, 8f, 8.6f, 8f, 12f)
            cubicTo(8f, 15.4f, 9.5f, 18.5f, 12f, 22f)
        }
    }.build()

    // CheckCircle (Success state)
    val CheckCircle: ImageVector = ImageVector.Builder("ClaudeCheckCircle", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 22f)
            cubicTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
            cubicTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
            cubicTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            cubicTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
            close()
            moveTo(8f, 12f); lineTo(11f, 15f); lineTo(16f, 9f)
        }
    }.build()

    // Warning (Amber alert)
    val Warning: ImageVector = ImageVector.Builder("ClaudeWarning", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 3f); lineTo(22f, 20f); lineTo(2f, 20f); close()
            moveTo(12f, 9f); lineTo(12f, 14f)
            moveTo(12f, 17f); lineTo(12.01f, 17f)
        }
    }.build()

    // Tune / Sliders
    val Tune: ImageVector = ImageVector.Builder("ClaudeTune", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(3f, 8f); lineTo(14f, 8f)
            moveTo(18f, 8f); lineTo(21f, 8f)
            moveTo(14f, 5f); lineTo(14f, 11f); lineTo(18f, 11f); lineTo(18f, 5f); close()
            moveTo(3f, 16f); lineTo(6f, 16f)
            moveTo(10f, 16f); lineTo(21f, 16f)
            moveTo(6f, 13f); lineTo(6f, 19f); lineTo(10f, 19f); lineTo(10f, 13f); close()
        }
    }.build()

    // Security / Shield
    val Security: ImageVector = ImageVector.Builder("ClaudeSecurity", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 2f); lineTo(20f, 5.5f); lineTo(20f, 11.5f)
            cubicTo(20f, 16.5f, 16.5f, 20.8f, 12f, 22f)
            cubicTo(7.5f, 20.8f, 4f, 16.5f, 4f, 11.5f)
            lineTo(4f, 5.5f)
            close()
        }
    }.build()

    // Info
    val Info: ImageVector = ImageVector.Builder("ClaudeInfo", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(12f, 22f)
            cubicTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
            cubicTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
            cubicTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            cubicTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
            close()
            moveTo(12f, 8f); lineTo(12.01f, 8f)
            moveTo(12f, 11.5f); lineTo(12f, 16.5f)
        }
    }.build()

    // Moon (Dark mode theme)
    val Moon: ImageVector = ImageVector.Builder("ClaudeMoon", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(21f, 12.79f)
            cubicTo(20.84f, 17.84f, 16.74f, 21.94f, 11.69f, 21.99f)
            cubicTo(6.44f, 22.04f, 2.13f, 17.74f, 2.01f, 12.49f)
            cubicTo(1.92f, 8.42f, 4.49f, 4.88f, 8.35f, 3.56f)
            cubicTo(8.79f, 3.41f, 9.24f, 3.77f, 9.17f, 4.23f)
            cubicTo(8.93f, 5.8f, 9.19f, 7.42f, 9.93f, 8.87f)
            cubicTo(11.23f, 11.41f, 13.79f, 13.02f, 16.64f, 13.06f)
            cubicTo(17.65f, 13.08f, 18.66f, 12.83f, 19.57f, 12.36f)
            cubicTo(19.98f, 12.15f, 20.48f, 12.38f, 20.57f, 12.83f)
            close()
        }
    }.build()

    // Pin (Conversation pin)
    val Pin: ImageVector = ImageVector.Builder("ClaudePin", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(16f, 4f); lineTo(8f, 4f); lineTo(9f, 9f); lineTo(6f, 12f); lineTo(18f, 12f); lineTo(15f, 9f); close()
            moveTo(12f, 12f); lineTo(12f, 21f)
        }
    }.build()

    // Archive
    val Archive: ImageVector = ImageVector.Builder("ClaudeArchive", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(3f, 4f); lineTo(21f, 4f); lineTo(21f, 8f); lineTo(3f, 8f); close()
            moveTo(4f, 8f); lineTo(4f, 20f); lineTo(20f, 20f); lineTo(20f, 8f)
            moveTo(10f, 12f); lineTo(14f, 12f)
        }
    }.build()

    // Unarchive
    val Unarchive: ImageVector = ImageVector.Builder("ClaudeUnarchive", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(3f, 4f); lineTo(21f, 4f); lineTo(21f, 8f); lineTo(3f, 8f); close()
            moveTo(4f, 8f); lineTo(4f, 20f); lineTo(20f, 20f); lineTo(20f, 8f)
            moveTo(12f, 11f); lineTo(12f, 17f)
            moveTo(9f, 14f); lineTo(12f, 11f); lineTo(15f, 14f)
        }
    }.build()

    // Edit / Pencil
    val Edit: ImageVector = ImageVector.Builder("ClaudeEdit", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(17f, 3f); lineTo(21f, 7f); lineTo(8f, 20f); lineTo(3f, 21f); lineTo(4f, 16f); close()
            moveTo(14f, 6f); lineTo(18f, 10f)
        }
    }.build()

    // Clean / Brush
    val Clean: ImageVector = ImageVector.Builder("ClaudeClean", 24.dp, 24.dp, 24f, 24f).apply {
        strokePath(this) {
            moveTo(15f, 4f); lineTo(20f, 9f); lineTo(14f, 15f); lineTo(9f, 10f); close()
            moveTo(9f, 10f); lineTo(4f, 15f); lineTo(7f, 20f); lineTo(12f, 17f)
            moveTo(4f, 15f); lineTo(2f, 22f); lineTo(9f, 20f)
        }
    }.build()
}

package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.theme.ClaudeIcons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val context = LocalContext.current
    val blocks = remember(text) { parseMarkdownBlocks(text) }

    Column(modifier = modifier) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.CodeBlock -> {
                    CodeBlockView(block = block, context = context)
                }
                is MarkdownBlock.Paragraph -> {
                    val annotated = remember(block.text, textColor) {
                        parseInlineMarkdown(block.text, textColor)
                    }
                    Text(
                        text = annotated,
                        style = TextStyle(
                            fontFamily = com.example.ui.theme.AnthropicSerifFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.5.sp,
                            lineHeight = 26.sp,
                            letterSpacing = 0.2.sp
                        ),
                        color = textColor,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
                is MarkdownBlock.Header -> {
                    Text(
                        text = block.text,
                        style = when (block.level) {
                            1 -> TextStyle(
                                fontFamily = com.example.ui.theme.AnthropicSerifFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                                lineHeight = 30.sp
                            )
                            2 -> TextStyle(
                                fontFamily = com.example.ui.theme.AnthropicSerifFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                lineHeight = 26.sp
                            )
                            else -> TextStyle(
                                fontFamily = com.example.ui.theme.AnthropicSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            )
                        },
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                    )
                }
                is MarkdownBlock.ListItem -> {
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = block.prefix,
                            style = TextStyle(
                                fontFamily = com.example.ui.theme.AnthropicSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(20.dp)
                        )
                        val itemAnnotated = remember(block.text, textColor) {
                            parseInlineMarkdown(block.text, textColor)
                        }
                        Text(
                            text = itemAnnotated,
                            style = TextStyle(
                                fontFamily = com.example.ui.theme.AnthropicSerifFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            ),
                            color = textColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CodeBlockView(
    block: MarkdownBlock.CodeBlock,
    context: Context
) {
    var isCopied by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1B1B19),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E2E2A)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column {
            // Header bar with language tag and copy button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF222220))
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = block.language.ifBlank { "code" }.lowercase(),
                    style = TextStyle(
                        fontFamily = com.example.ui.theme.AnthropicMonoFontFamily,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color(0xFFA5A59E)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Code", block.code))
                        Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
                        isCopied = true
                        scope.launch {
                            delay(2000)
                            isCopied = false
                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isCopied) ClaudeIcons.Check else ClaudeIcons.Copy,
                        contentDescription = "Copy code",
                        tint = if (isCopied) Color(0xFF34D399) else Color(0xFFCBD5E1),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Code content with horizontal scroll
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(14.dp)
            ) {
                Text(
                    text = block.code,
                    style = TextStyle(
                        fontFamily = com.example.ui.theme.AnthropicMonoFontFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 21.sp
                    ),
                    color = Color(0xFFECEBE6)
                )
            }
        }
    }
}

private sealed class MarkdownBlock {
    data class Paragraph(val text: String) : MarkdownBlock()
    data class Header(val level: Int, val text: String) : MarkdownBlock()
    data class CodeBlock(val language: String, val code: String) : MarkdownBlock()
    data class ListItem(val prefix: String, val text: String) : MarkdownBlock()
}

private fun parseMarkdownBlocks(text: String): List<MarkdownBlock> {
    val blocks = mutableListOf<MarkdownBlock>()
    val lines = text.lines()
    var i = 0

    while (i < lines.size) {
        val line = lines[i]

        // Code block starts
        if (line.trimStart().startsWith("```")) {
            val language = line.trimStart().removePrefix("```").trim()
            val codeLines = mutableListOf<String>()
            i++
            while (i < lines.size && !lines[i].trimStart().startsWith("```")) {
                codeLines.add(lines[i])
                i++
            }
            blocks.add(MarkdownBlock.CodeBlock(language, codeLines.joinToString("\n")))
            i++
            continue
        }

        // Headers
        if (line.startsWith("# ")) {
            blocks.add(MarkdownBlock.Header(1, line.removePrefix("# ").trim()))
            i++
            continue
        } else if (line.startsWith("## ")) {
            blocks.add(MarkdownBlock.Header(2, line.removePrefix("## ").trim()))
            i++
            continue
        } else if (line.startsWith("### ")) {
            blocks.add(MarkdownBlock.Header(3, line.removePrefix("### ").trim()))
            i++
            continue
        }

        // Bullet lists
        if (line.trimStart().startsWith("- ") || line.trimStart().startsWith("* ")) {
            val content = line.trimStart().substring(2).trim()
            blocks.add(MarkdownBlock.ListItem("•", content))
            i++
            continue
        }

        // Numbered lists (e.g. 1. )
        val numberedMatch = Regex("^\\s*(\\d+)\\.\\s+(.*)").find(line)
        if (numberedMatch != null) {
            val number = numberedMatch.groupValues[1]
            val content = numberedMatch.groupValues[2]
            blocks.add(MarkdownBlock.ListItem("$number.", content))
            i++
            continue
        }

        // Normal paragraph
        if (line.isNotBlank()) {
            blocks.add(MarkdownBlock.Paragraph(line))
        }
        i++
    }

    return blocks
}

private fun parseInlineMarkdown(text: String, defaultColor: Color) = buildAnnotatedString {
    var index = 0
    while (index < text.length) {
        // Bold: **text**
        if (text.startsWith("**", index)) {
            val end = text.indexOf("**", index + 2)
            if (end != -1) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = defaultColor)) {
                    append(text.substring(index + 2, end))
                }
                index = end + 2
                continue
            }
        }

        // Inline code: `code`
        if (text[index] == '`') {
            val end = text.indexOf('`', index + 1)
            if (end != -1) {
                withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        background = Color(0x2264748B),
                        color = Color(0xFF2563EB),
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append(text.substring(index + 1, end))
                }
                index = end + 1
                continue
            }
        }

        // Italic: *text*
        if (text[index] == '*' && index + 1 < text.length && text[index + 1] != '*') {
            val end = text.indexOf('*', index + 1)
            if (end != -1) {
                withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = defaultColor)) {
                    append(text.substring(index + 1, end))
                }
                index = end + 1
                continue
            }
        }

        withStyle(SpanStyle(color = defaultColor)) {
            append(text[index].toString())
        }
        index++
    }
}

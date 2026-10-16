package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.theme.ClaudeIcons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ModelEntity
import com.example.data.model.HuggingFaceModel
import com.example.ui.theme.ClaudeCoral
import com.example.ui.theme.ClaudeDarkBackground
import com.example.ui.theme.ClaudeDarkBorder
import com.example.ui.theme.ClaudeDarkSurface
import com.example.ui.theme.ClaudeDarkSurfaceVariant
import com.example.ui.theme.ClaudeTextPrimary
import com.example.ui.theme.ClaudeTextSecondary
import com.example.util.DeviceDiagnostics
import com.example.util.DownloadProgress
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelManagerScreen(
    models: List<ModelEntity>,
    activeModel: ModelEntity?,
    downloadStates: Map<String, DownloadProgress>,
    huggingFaceCatalog: List<HuggingFaceModel>,
    isImporting: Boolean,
    importError: String?,
    statusMessage: String?,
    onImportClick: () -> Unit,
    onSelectActiveModel: (String) -> Unit,
    onLoadModelIntoRam: (ModelEntity) -> Unit,
    onUnloadModel: () -> Unit,
    onDeleteModel: (ModelEntity) -> Unit,
    onDownloadHfModel: (HuggingFaceModel) -> Unit,
    onCancelDownload: (String) -> Unit,
    onDownloadCustomUrl: (url: String, name: String) -> Unit,
    onDismissError: () -> Unit,
    onDismissStatus: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val deviceInfo = remember { DeviceDiagnostics.getInfo(context) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var modelToDelete by remember { mutableStateOf<ModelEntity?>(null) }
    var showCustomUrlDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ClaudeDarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AI Model Hub & RAM Manager",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ClaudeTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(ClaudeIcons.ArrowBack, contentDescription = "Back", tint = ClaudeTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ClaudeDarkSurface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab Header (Installed vs Hugging Face Hub)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = ClaudeDarkSurface,
                contentColor = ClaudeCoral
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(ClaudeIcons.Memory, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Installed (${models.size})")
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(ClaudeIcons.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hugging Face Hub")
                        }
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(6.dp)) }

                // Status banner if any
                if (!statusMessage.isNullOrBlank()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF064E3B).copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(ClaudeIcons.CheckCircle, contentDescription = null, tint = Color(0xFF34D399))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = statusMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFD1FAE5),
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = onDismissStatus) {
                                    Text("OK", color = Color(0xFF34D399))
                                }
                            }
                        }
                    }
                }

                // Error banner if any
                if (!importError.isNullOrBlank()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF7F1D1D).copy(alpha = 0.4f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(ClaudeIcons.Warning, contentDescription = null, tint = Color(0xFFF87171))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = importError,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFFEE2E2),
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = onDismissError) {
                                    Text("Dismiss", color = Color(0xFFF87171))
                                }
                            }
                        }
                    }
                }

                // Device Hardware & Live RAM Bar
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ClaudeDarkSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ClaudeDarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Device Memory & Hardware",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = ClaudeTextPrimary
                                )

                                val isAnyLoaded = models.any { it.isLoadedInRam }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isAnyLoaded) Color(0xFF065F46) else Color(0xFF3F3F3E)
                                ) {
                                    Text(
                                        text = if (isAnyLoaded) "⚡ RAM ACTIVE" else "RAM IDLE",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        color = if (isAnyLoaded) Color(0xFF34D399) else ClaudeTextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // RAM Column
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Device RAM", style = MaterialTheme.typography.labelSmall, color = ClaudeTextSecondary)
                                    Text(
                                        text = "${deviceInfo.availableRamMb} MB free / ${deviceInfo.totalRamMb} MB",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = ClaudeTextPrimary
                                    )
                                    Text(
                                        text = if (deviceInfo.isLowRamDevice) "4GB Target Profile (Safe)" else "Standard Profile",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (deviceInfo.isLowRamDevice) Color(0xFFF59E0B) else Color(0xFF10B981)
                                    )
                                }

                                // Storage Column
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Free Storage", style = MaterialTheme.typography.labelSmall, color = ClaudeTextSecondary)
                                    Text(
                                        text = "${String.format(Locale.US, "%.1f", deviceInfo.freeStorageGb)} GB Available",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = ClaudeTextPrimary
                                    )
                                    Text(
                                        text = "Internal Models Storage",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ClaudeTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // TAB 0: INSTALLED MODELS
                if (selectedTab == 0) {
                    item {
                        Button(
                            onClick = onImportClick,
                            enabled = !isImporting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("import_gguf_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = ClaudeCoral),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isImporting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Importing & Verifying GGUF…")
                            } else {
                                Icon(ClaudeIcons.Folder, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Import GGUF from Device Storage")
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Installed GGUF Models (${models.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ClaudeTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (models.isEmpty()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ClaudeDarkSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = ClaudeIcons.Folder,
                                        contentDescription = null,
                                        tint = ClaudeTextSecondary,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "No models installed yet",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = ClaudeTextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Download a Hugging Face model from the 'Hugging Face Hub' tab or import from storage.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ClaudeTextSecondary
                                    )
                                }
                            }
                        }
                    } else {
                        items(models, key = { it.id }) { model ->
                            val isActive = model.id == activeModel?.id
                            val isLoaded = model.isLoadedInRam
                            val sizeMb = model.fileSizeBytes / (1024 * 1024)
                            val sizeFormatted = if (sizeMb > 1024) {
                                "${String.format(Locale.US, "%.2f", sizeMb / 1024f)} GB"
                            } else {
                                "$sizeMb MB"
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isLoaded) Color(0xFF1E2822) else ClaudeDarkSurface,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isLoaded) Color(0xFF10B981) else if (isActive) ClaudeCoral else ClaudeDarkBorder
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = ClaudeIcons.Memory,
                                            contentDescription = null,
                                            tint = if (isLoaded) Color(0xFF34D399) else if (isActive) ClaudeCoral else ClaudeTextSecondary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = model.name,
                                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                                color = ClaudeTextPrimary,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = "${model.quantType} • ${model.architecture} • $sizeFormatted",
                                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                                color = ClaudeTextSecondary
                                            )
                                        }

                                        // Status badge
                                        if (isLoaded) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF065F46)
                                            ) {
                                                Text(
                                                    text = "⚡ LOADED IN RAM",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 10.sp
                                                    ),
                                                    color = Color(0xFF34D399),
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        } else if (isActive) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = ClaudeDarkSurfaceVariant
                                            ) {
                                                Text(
                                                    text = "ACTIVE (OFFLINE)",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 10.sp
                                                    ),
                                                    color = ClaudeCoral,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Action buttons (Load into RAM / Unload / Set Active)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (isLoaded) {
                                            Button(
                                                onClick = onUnloadModel,
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Unload from RAM")
                                            }
                                        } else {
                                            Button(
                                                onClick = { onLoadModelIntoRam(model) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Load into RAM")
                                            }
                                        }

                                        if (!isActive) {
                                            OutlinedButton(
                                                onClick = { onSelectActiveModel(model.id) },
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text("Select", color = ClaudeTextPrimary)
                                            }
                                        }

                                        IconButton(
                                            onClick = { modelToDelete = model },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = ClaudeIcons.Trash,
                                                contentDescription = "Delete model",
                                                tint = Color(0xFFEF4444),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // TAB 1: HUGGING FACE MODEL HUB
                if (selectedTab == 1) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ClaudeDarkSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ClaudeDarkBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable { showCustomUrlDialog = true }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(ClaudeIcons.Link, contentDescription = null, tint = ClaudeCoral)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Download from Custom HuggingFace URL",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = ClaudeTextPrimary
                                    )
                                    Text(
                                        text = "Paste any direct .gguf resolve URL from huggingface.co",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ClaudeTextSecondary
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Recommended Mobile GGUF Models",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ClaudeTextPrimary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    items(huggingFaceCatalog, key = { it.id }) { hfModel ->
                        val isInstalled = models.any { it.name.contains(hfModel.name.take(12), ignoreCase = true) }
                        val downloadProgress = downloadStates[hfModel.id]
                        val isDownloading = downloadProgress != null && !downloadProgress.isComplete && downloadProgress.error == null

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = ClaudeDarkSurface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isDownloading) ClaudeCoral else ClaudeDarkBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = ClaudeIcons.Download,
                                        contentDescription = null,
                                        tint = if (isDownloading) ClaudeCoral else Color(0xFFF59E0B),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = hfModel.name,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                            color = ClaudeTextPrimary
                                        )
                                        Text(
                                            text = "${hfModel.repoName} • ${hfModel.quant}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                            color = ClaudeTextSecondary
                                        )
                                    }

                                    if (hfModel.isRecommendedForLowRam) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF065F46)
                                        ) {
                                            Text(
                                                text = "4GB RAM BEST",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp
                                                ),
                                                color = Color(0xFF34D399),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = hfModel.description,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    color = ClaudeTextSecondary
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Size: ${hfModel.sizeFormatted} | RAM: ~${hfModel.ramRequirementMb} MB",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                        color = ClaudeTextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Download progress or button
                                if (isDownloading && downloadProgress != null) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Downloading: ${downloadProgress.progressPercent}% (${String.format(Locale.US, "%.1f", downloadProgress.bytesDownloaded / (1024f * 1024f))} MB / ${String.format(Locale.US, "%.1f", downloadProgress.totalBytes / (1024f * 1024f))} MB)",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = ClaudeCoral
                                            )
                                            Text(
                                                text = "${String.format(Locale.US, "%.1f", downloadProgress.speedMbPerSec)} MB/s",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = ClaudeTextSecondary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        LinearProgressIndicator(
                                            progress = { downloadProgress.progressPercent / 100f },
                                            modifier = Modifier.fillMaxWidth(),
                                            color = ClaudeCoral
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        OutlinedButton(
                                            onClick = { onCancelDownload(hfModel.id) },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Cancel Download", color = Color(0xFFEF4444))
                                        }
                                    }
                                } else if (isInstalled) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(ClaudeIcons.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Installed & ready in local library",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = Color(0xFF10B981)
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = { onDownloadHfModel(hfModel) },
                                        colors = ButtonDefaults.buttonColors(containerColor = ClaudeCoral),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(ClaudeIcons.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Download from Hugging Face (${hfModel.sizeFormatted})")
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(28.dp)) }
            }
        }
    }

    // Custom Hugging Face URL Dialog
    if (showCustomUrlDialog) {
        var customUrl by remember { mutableStateOf("") }
        var customName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showCustomUrlDialog = false },
            containerColor = ClaudeDarkSurface,
            title = {
                Text(
                    text = "Download from Hugging Face",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ClaudeTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Paste a direct download URL from Hugging Face (e.g. resolve/main/*.gguf):",
                        style = MaterialTheme.typography.bodySmall,
                        color = ClaudeTextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = customUrl,
                        onValueChange = { customUrl = it },
                        label = { Text("Hugging Face GGUF URL") },
                        placeholder = { Text("https://huggingface.co/.../resolve/main/model.gguf") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Model Display Name (Optional)") },
                        placeholder = { Text("e.g. Qwen2.5 0.5B Custom") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customUrl.isNotBlank()) {
                            onDownloadCustomUrl(customUrl.trim(), customName.trim())
                            showCustomUrlDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ClaudeCoral)
                ) {
                    Text("Download")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomUrlDialog = false }) {
                    Text("Cancel", color = ClaudeTextSecondary)
                }
            }
        )
    }

    // Delete Model Confirmation Dialog
    if (modelToDelete != null) {
        AlertDialog(
            onDismissRequest = { modelToDelete = null },
            containerColor = ClaudeDarkSurface,
            title = {
                Text(
                    text = stringResource(R.string.action_delete_model),
                    color = ClaudeTextPrimary
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove \"${modelToDelete?.name}\" and delete its GGUF weights from device storage?",
                    color = ClaudeTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        modelToDelete?.let { onDeleteModel(it) }
                        modelToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { modelToDelete = null }) {
                    Text(stringResource(R.string.action_cancel), color = ClaudeTextSecondary)
                }
            }
        )
    }
}

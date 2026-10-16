package com.example.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs

data class DeviceInfo(
    val deviceModel: String,
    val manufacturer: String,
    val androidVersion: String,
    val totalRamMb: Long,
    val availableRamMb: Long,
    val isLowMemory: Boolean,
    val freeStorageGb: Float,
    val supportedAbis: String,
    val isLowRamDevice: Boolean
)

object DeviceDiagnostics {
    fun getInfo(context: Context): DeviceInfo {
        val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)

        val totalRamMb = memInfo.totalMem / (1024 * 1024)
        val availRamMb = memInfo.availMem / (1024 * 1024)

        val stat = StatFs(Environment.getDataDirectory().path)
        val freeBytes = stat.availableBlocksLong * stat.blockSizeLong
        val freeStorageGb = freeBytes.toFloat() / (1024 * 1024 * 1024)

        return DeviceInfo(
            deviceModel = Build.MODEL,
            manufacturer = Build.MANUFACTURER,
            androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
            totalRamMb = totalRamMb,
            availableRamMb = availRamMb,
            isLowMemory = memInfo.lowMemory,
            freeStorageGb = freeStorageGb,
            supportedAbis = Build.SUPPORTED_ABIS.joinToString(", "),
            isLowRamDevice = totalRamMb <= 4500 // Targets ~4GB devices like OPPO CPH2269
        )
    }
}

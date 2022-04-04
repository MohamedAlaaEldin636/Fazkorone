package com.grand.ezkorone.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat

@SuppressLint("HardwareIds")
fun Context.getDeviceIdWithoutPermission(fallbackOfNullOrEmpty: String = "device_id"): String {
    return kotlin.runCatching {
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }.getOrNull().let {
        if (it.isNullOrEmpty()) fallbackOfNullOrEmpty else it
    }
}

fun Context.checkSelfPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

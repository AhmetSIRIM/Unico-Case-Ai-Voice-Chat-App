package com.ahmetsirim.common.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Context.launchAppDetailsSettings() {
    startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

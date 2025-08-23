package com.ahmetsirim.chat

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.app.ActivityCompat

internal fun handleMicrophonePermissionRequest(
    context: Context,
    onEvent: (ChatContract.UiEvent) -> Unit,
    permissionRequesterActivityResultLauncher: ManagedActivityResultLauncher<String, Boolean>,
    activity: Activity?,
) {
    when {
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED -> {
            TODO()
        }

        activity?.let { nonNullActivity ->
            ActivityCompat.shouldShowRequestPermissionRationale(
                nonNullActivity,
                Manifest.permission.RECORD_AUDIO
            )
        } == true -> {
            onEvent(ChatContract.UiEvent.OnShowMicrophonePermissionRationale)
        }

        else -> {
            permissionRequesterActivityResultLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
}
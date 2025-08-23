package com.ahmetsirim.data.di

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import com.ahmetsirim.common.log.logError
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@ViewModelScoped
class FileOperationsHelper @Inject constructor(
    @param:ApplicationContext
    private val applicationContext: Context
) {
    fun createImageFile(
        prefix: String = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date()),
        suffix: String,
        storageDirectory: File
    ): File {
        return File.createTempFile(
            prefix,
            suffix,
            storageDirectory
        )
    }

    fun createTempAudioFile(audioBytes: ByteArray): File {
        val tempFile = File.createTempFile(
            AUDIO_PREFIX,
            AUDIO_EXTENSION,
            applicationContext.cacheDir
        )
        tempFile.writeBytes(audioBytes)
        return tempFile
    }

    fun cleanupTempFile(filePath: String) {
        try {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
                Log.d("FileOperationsHelper", "Temp file deleted: $filePath")
            }
        } catch (e: Exception) {
            Log.e("FileOperationsHelper", "Error deleting temp file: ${e.message}")
        }
    }

    fun getTemporaryImageUri(): Uri? {
        return try {
            val temporaryFile = createImageFile(
                prefix = "JPEG_${System.currentTimeMillis()}_",
                suffix = JPEG_EXTENSION,
                storageDirectory = applicationContext.cacheDir
            )

            FileProvider.getUriForFile(
                applicationContext,
                "${applicationContext.packageName}.fileprovider",
                temporaryFile
            )
        } catch (ioException: IOException) {
            logError(
                throwable = ioException,
                message = ioException.message.toString()
            )
            null
        }
    }

    fun saveImageToDirectory(
        sourceUri: Uri,
        destinationDir: File
    ): Uri {
        require(sourceUri.toString().isNotBlank()) { "Source URI cannot be blank" }

        destinationDir.takeIf { !it.exists() }?.mkdirs()

        val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val destinationFile = File(destinationDir, "$IMG_PREFIX$timeStamp$JPEG_EXTENSION")

        applicationContext.contentResolver.openInputStream(sourceUri)?.use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return Uri.fromFile(destinationFile)
    }

    fun compressImage(
        file: File,
        sourceUri: Uri,
        maxSizeKB: Long = 500,
        quality: Int = 80
    ): Uri {
        val bitmap = applicationContext.contentResolver.openInputStream(sourceUri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: throw IllegalStateException("Failed to decode bitmap")

        var currentQuality = quality
        val byteArrayOutputStream = ByteArrayOutputStream()

        do {
            byteArrayOutputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, byteArrayOutputStream)
            currentQuality -= 10
        } while (byteArrayOutputStream.size() > maxSizeKB * 1024 && currentQuality > 0)

        val compressedFile = file.apply {
            outputStream().use {
                it.write(byteArrayOutputStream.toByteArray())
            }
        }

        return Uri.fromFile(compressedFile)
    }

    fun getFileNameFromUri(uri: Uri): String {
        if (uri.scheme == "content") {
            applicationContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME).takeIf { it >= 0 }?.let { index ->
                    if (cursor.moveToFirst()) {
                        return cursor.getString(index)
                    }
                }
            }
        }

        return uri.path?.let { File(it).name } ?: "file"
    }

    fun getFileSizeInMegabytes(uri: Uri): Double {
        val size = if (uri.scheme == "content") {
            applicationContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                cursor.getColumnIndex(OpenableColumns.SIZE).takeIf { it >= 0 }?.let { index ->
                    if (cursor.moveToFirst()) cursor.getLong(index) else 0L
                }
            } ?: 0L
        } else {
            uri.path?.let { File(it).length() } ?: 0L
        }

        return size / (1024.0 * 1024.0)
    }

    fun getMimeType(uri: Uri): String? = applicationContext.contentResolver.getType(uri)

    fun openInputStream(uri: Uri) = applicationContext.contentResolver.openInputStream(uri)

    companion object {
        private const val IMG_PREFIX = "IMG_"
        private const val JPEG_EXTENSION = ".jpg"
        private const val AUDIO_PREFIX = "tts_audio"
        private const val AUDIO_EXTENSION = ".mp3"
        private const val DATE_FORMAT = "yyyyMMdd_HHmmss"
    }
}

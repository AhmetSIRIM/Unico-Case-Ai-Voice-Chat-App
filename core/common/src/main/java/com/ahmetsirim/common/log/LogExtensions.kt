package com.ahmetsirim.common.log

import android.util.Log
import timber.log.Timber

/**
 * Logs a debug message and returns the object for method chaining.
 * Useful for debugging in method chains without breaking the flow.
 *
 * @param message The message to log (defaults to object's toString())
 * @param tag The log tag (defaults to "UnicoCaseDebug")
 * @return The original object for chaining
 */
fun <T> T.logDebugThenReturnSimply(
    message: String = this.toString(),
    tag: String = "UnicoCaseDebug"
) = also {
    Timber.tag(
        tag
    ).d(message = message)
}

/**
 * Logs a message with specified log level using the class name as tag.
 * Uses Android's Log constants for level specification.
 *
 * @param message The message to log
 * @param level The log level (Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR)
 * @param tag The log tag (defaults to class simple name)
 */
inline fun <reified T> T.log(
    message: String,
    level: Int = Log.DEBUG,
    tag: String = T::class.java.simpleName
) {
    when (level) {
        Log.VERBOSE -> Timber.tag(tag).v(message)
        Log.DEBUG -> Timber.tag(tag).d(message)
        Log.INFO -> Timber.tag(tag).i(message)
        Log.WARN -> Timber.tag(tag).w(message)
        Log.ERROR -> Timber.tag(tag).e(message)
    }
}

inline fun <reified T> T.logError(
    throwable: Throwable,
    message: String,
    tag: String = T::class.java.simpleName
) {
    Timber.tag(tag).e(throwable, message)
}

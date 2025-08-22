package com.ahmetsirim.data.exceptionhandling

import android.util.Log
import com.ahmetsirim.common.log.log
import com.ahmetsirim.domain.model.common.ErrorState
import com.ahmetsirim.domain.model.common.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import com.ahmetsirim.designsystem.R as coreR

/**
 * Converts a Throwable into a Response.Error, preserving CancellationExceptions.
 *
 * This extension function is used in network request flows to transform exceptions
 * into the Response sealed class format. CancellationExceptions are re-thrown
 * rather than wrapped to properly respect coroutine cancellation.
 *
 * @return Response.Error containing this throwable, or re-throws if it's a CancellationException
 */
internal fun Throwable.handleThrowable(): Response<Nothing> {

    printStackTrace()

    log(message = message ?: this::class.simpleName.toString(), level = Log.ERROR)

    return when (this) {
        is CancellationException -> throw this

        is IOException -> Response.Error(errorState = connectionErrorState)

        else -> Response.Error(errorState = unexpectedErrorState)
    }
}

private val unexpectedErrorState = ErrorState(
    exceptionMessageResId = coreR.string.there_was_an_unexpected_error_please_try_again_soon,
)

private val connectionErrorState = ErrorState(
    exceptionMessageResId = coreR.string.please_check_your_connection_and_try_again,
)
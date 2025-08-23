package com.ahmetsirim.data.utility

import com.ahmetsirim.data.exception.handleThrowable
import com.ahmetsirim.domain.model.common.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

/**
 * Executes a network request within a [Flow], managing loading, success, and error states.
 *
 * This function wraps the provided [manipulateTheSuccessResultData] in a [Flow], emitting [Response.Loading]
 * at the start. On success, it emits the result as [Response.Success]. If an exception occurs, it transforms
 * the error into [Response.Error] using [com.ahmetsirim.data.exception.handleThrowable] and emits it.
 *
 * @param coroutineDispatcher The [CoroutineDispatcher] to run the flow on, typically for I/O operations
 * @param manipulateTheSuccessResultData A suspend function that executes the network request and returns a [Response]
 * @return A [Flow] emitting [Response] states: [Response.Loading], [Response.Success], or [Response.Error]
 *
 * ```kotlin
 * // Fetching user details
 * fun getUserDetails() = handleNetworkFlowRequest(ioDispatcher) {
 *     Response.Success(remoteDataSource.getUserDetails())
 * }
 *
 * // Resending an OTP with conditional logic
 * fun resendOtp(phoneNumber: String) = handleNetworkFlowRequest(ioDispatcher) {
 *     val response = remoteOneTimePasswordDataSource.resendTheOneTimePassword(phoneNumber).value?.response ?: false
 *     if (response) Response.Success(remoteOneTimePasswordDataSource.getTheOneTimePassword(phoneNumber))
 *     else Response.Error(Exception("Failed to resend OTP"))
 * }
 * ```
 */
internal inline fun <T : Any> handleNetworkFlowRequest(
    coroutineDispatcher: CoroutineDispatcher,
    crossinline manipulateTheSuccessResultData: suspend () -> Response<T>
): Flow<Response<T>> {
    return flow {
        emit(manipulateTheSuccessResultData())
    }.onStart {
        emit(Response.Loading)
    }.catch {
        emit(it.handleThrowable())
    }.flowOn(coroutineDispatcher)
}

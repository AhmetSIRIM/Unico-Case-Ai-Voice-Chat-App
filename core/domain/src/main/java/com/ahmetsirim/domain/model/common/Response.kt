package com.ahmetsirim.domain.model.common

sealed class Response<out T : Any> {
    data object Loading : Response<Nothing>()
    data class Success<out T : Any>(val result: T) : Response<T>()
    data class Error(val errorState: ErrorState) : Response<Nothing>()
}
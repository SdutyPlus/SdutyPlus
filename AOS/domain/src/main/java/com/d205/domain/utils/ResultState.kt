package com.d205.domain.utils

import java.io.IOException

sealed class ResultState<out T> {
    object Uninitialized : ResultState<Nothing>()

    object Loading : ResultState<Nothing>()

    object Empty : ResultState<Nothing>()

    data class Success<T>(val data: T) : ResultState<T>()

    data class Error(val exception: Throwable) : ResultState<Nothing>() {
        val isNetworkError = exception is IOException
    }
}

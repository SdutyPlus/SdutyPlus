package com.d108.sduty.utils

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val errorCode: Int?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(errorMessage: String?, errorCode: Int?, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, errorMessage, errorCode)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null, null)
        }
    }
}

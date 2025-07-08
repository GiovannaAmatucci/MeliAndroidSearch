package com.giovanna.amatucci.melisearch.domain.util

sealed class ResultWrapper<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : ResultWrapper<T>(data)
    class Error<T>(message: String? = null, data: T? = null) : ResultWrapper<T>(data, message)
    class Loading<T>(data: T? = null) : ResultWrapper<T>(data)

}
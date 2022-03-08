package com.wac.labcollect.util

class Utils {
}

enum class Status {
    SUCCESS, ERROR, LOADING
}

data class Resource<out T>(val status: Status, val data: T? = null, val message: String? = null) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data)
        fun <T> error(data: T? = null, message: String) = Resource(Status.ERROR, data, message)
        fun <T> loading(data: T? = null) = Resource(Status.LOADING, data)
    }
}

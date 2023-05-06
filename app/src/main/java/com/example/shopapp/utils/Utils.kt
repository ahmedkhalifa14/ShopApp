package com.example.shopapp.utils


object Utils {
    inline fun <T> tryCatch(action: () -> Resource<T>): Resource<T> {
        return try {
            action()
        } catch (e: Exception) {
            Resource.Error(e.message ?:"An unknown error occurred")
        }
    }

}
package com.marvel.stark.shared.retorift

import android.util.Log
import retrofit2.Response

@Suppress("MemberVisibilityCanBePrivate")
class ApiResponse<T> {
    val code: Int
    val body: T?
    val message: String?

    val isSuccessful: Boolean
        get() = code in 200..300

    constructor(error: Throwable) {
        Log.e("ApiResponse", "Exception:", error)
        if (error is ApiException) {
            this.code = error.errorCode
            this.message = error.errorMessage
        } else {
            this.code = 500
            this.message = error.message
        }
        this.body = null
    }

    constructor(response: Response<T>) {
        this.code = response.code()

        if (response.isSuccessful) {
            this.body = response.body()
            this.message = null
        } else {
            var errorMessage: String? = response.errorBody()?.string()
            errorMessage?.apply {
                if (isNullOrEmpty() || trim { it <= ' ' }.isEmpty()) {
                    errorMessage = response.message()
                }
            }
            this.body = null
            this.message = when (this.code) {
                429 -> "Too Many Requests"
                else -> errorMessage
            }
        }
    }
}
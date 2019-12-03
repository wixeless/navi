package com.marvel.stark.shared.retorift

import retrofit2.Response

/**Created by Jahongir on 10/9/2019.*/

suspend fun <T> suspendApiCall(apiService: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        ApiResponse(apiService.invoke())
    } catch (ex: Exception) {
        ApiResponse(ex)
    }
}
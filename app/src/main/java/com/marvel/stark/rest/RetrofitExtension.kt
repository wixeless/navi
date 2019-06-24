package com.marvel.stark.rest

import com.marvel.stark.rest.livedata.ApiResponse
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume

/**Created by Jahongir on 6/21/2019.*/

suspend fun <T : Any> Call<T>.awaitResult(): ApiResponse<T> {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>) {
                continuation.resumeWith(runCatching {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            ApiResponse(NullPointerException("Response body is null"))
                        } else {
                            ApiResponse(response)
                        }
                    } else {
                        ApiResponse(HttpException(response))
                    }
                })
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                // Don't bother with resuming the continuation if it is already cancelled.
                if (continuation.isCancelled) return
                continuation.resume(ApiResponse(t))
            }
        })

        registerOnCompletion(continuation)
    }
}

private fun Call<*>.registerOnCompletion(continuation: CancellableContinuation<*>) {
    continuation.invokeOnCancellation {
        try {
            cancel()
        } catch (ex: Throwable) {
            //Ignore cancel exception
        }
    }
}
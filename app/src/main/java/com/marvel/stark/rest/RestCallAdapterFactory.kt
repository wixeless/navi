package com.marvel.stark.rest

/**Created by Jahongir on 6/17/2019.*/

import android.util.Log
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RestCallAdapterFactory private constructor() : CallAdapter.Factory() {
    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = RestCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (Deferred::class.java != getRawType(returnType)) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                    "Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>")
        }
        val responseType = getParameterUpperBound(0, returnType)

        val rawDeferredType = getRawType(responseType)
        return if (rawDeferredType == RestResponse::class.java) {
            if (responseType !is ParameterizedType) {
                throw IllegalStateException("Response must be parameterized as RestResponse<Foo> or RestResponse<out Foo>")
            }
            ResponseCallAdapter<Any>(getParameterUpperBound(0, responseType))
        } else {
            BodyCallAdapter<Any>(responseType)
        }
    }

    private class BodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Deferred<T>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Deferred<T> {
            val deferred = CompletableDeferred<T>()
            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                }
            }
            call.enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    Log.d("BodyCallAdapter", "onFailure: ")
                    deferred.completeExceptionally(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    Log.d("BodyCallAdapter", "onResponse: ")
                    if (response.isSuccessful) {
                        deferred.complete(response.body()!!)
                    } else {
                        deferred.completeExceptionally(HttpException(response))
                    }
                }
            })

            return deferred
        }
    }

    private class ResponseCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Deferred<RestResponse<T>>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Deferred<RestResponse<T>> {
            val deferred = CompletableDeferred<RestResponse<T>>()

            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                }
            }

            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    deferred.complete(RestResponse(response))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    deferred.complete(RestResponse(t))
                }
            })

            return deferred
        }
    }
}
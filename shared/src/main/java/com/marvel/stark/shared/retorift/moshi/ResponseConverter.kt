package com.marvel.stark.shared.retorift.moshi

import com.marvel.stark.shared.retorift.ApiException
import com.squareup.moshi.Moshi
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**Created by Jahongir on 6/17/2019.*/

class ResponseConverterFactory(moshi: Moshi) : Converter.Factory() {

    private val moshiConverterFactory: MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        val wrappedType = object : ParameterizedType {
            override fun getActualTypeArguments(): Array<Type> = arrayOf(type)
            override fun getOwnerType(): Type? = null
            override fun getRawType(): Type = ApiWrapData::class.java
        }
        val moshiConverter: Converter<ResponseBody, *>? = moshiConverterFactory.responseBodyConverter(wrappedType, annotations, retrofit)
        @Suppress("UNCHECKED_CAST")
        return ResponseBodyConverter(moshiConverter as Converter<ResponseBody, ApiWrapData<Any>>)
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>,
                                      methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        return moshiConverterFactory.requestBodyConverter(type!!, parameterAnnotations, methodAnnotations, retrofit)
    }
}

class ResponseBodyConverter<T>(private val converter: Converter<ResponseBody, ApiWrapData<T>>) : Converter<ResponseBody, T> {

    @Throws(IOException::class, ApiException::class)
    override fun convert(responseBody: ResponseBody): T {
        val response = converter.convert(responseBody)
        if (response?.status != "OK") {
            throw ApiException(response?.error)
        }
        return response.data
        //return if (response.statusCode == 0) response.data? else throw ApiException(response.statusCode, response.message?)
    }
}
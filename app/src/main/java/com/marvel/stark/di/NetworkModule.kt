package com.marvel.stark.di

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.marvel.stark.rest.EthermineService
import com.marvel.stark.rest.ResponseConverterFactory
import com.marvel.stark.rest.RestCallAdapterFactory
import com.marvel.stark.rest.livedata.LiveDataCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi, baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RestCallAdapterFactory())
                .addConverterFactory(ResponseConverterFactory(moshi))
                //.addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpProfileInterceptor: OkHttpProfilerInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(httpProfileInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Singleton
    @Provides
    internal fun provideInterceptor(): OkHttpProfilerInterceptor {
        return OkHttpProfilerInterceptor()
    }

    @Singleton
    @Provides
    internal fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .build()
    }

    //API
    @Provides
    @Singleton
    fun provideEthermineService(retrofit: Retrofit): EthermineService {
        return retrofit.create(EthermineService::class.java)
    }

}
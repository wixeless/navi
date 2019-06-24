package com.marvel.stark.rest

import androidx.lifecycle.LiveData
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.rest.livedata.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**Created by Jahongir on 6/15/2019.*/

interface EthermineService {
    @GET("/miner/{address}/dashboard")
    fun fetchDashboardLiveData(@Path("address") address: String): LiveData<ApiResponse<DashboardDto>>

    @GET("/miner/{address}/dashboard")
    fun fetchDashboardDeferred(@Path("address") address: String): Deferred<ApiResponse<DashboardDto>>

    @GET("/miner/{address}/dashboard")
    fun fetchDashboard(@Path("address") address: String): Call<DashboardDto>

    @GET("/miner/{address}/dashboard")
    suspend fun fetchDashboardTest(@Path("address") address: String): Response<DashboardDto>


}
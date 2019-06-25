package com.marvel.stark.rest.service

import androidx.lifecycle.LiveData
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.rest.livedata.ApiResponse
import com.marvel.stark.room.Payout
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**Created by Jahongir on 6/15/2019.*/

interface EthermineService {
    @GET("/miner/{address}/dashboard")
    fun fetchDashboardLiveData(@Path("address") address: String?): LiveData<ApiResponse<DashboardDto>>

    @GET("/miner/{address}/dashboard")
    fun fetchDashboardDeferred(@Path("address") address: String): Deferred<ApiResponse<DashboardDto>>

    @GET("/miner/{address}/dashboard")
    fun fetchDashboard(@Path("address") address: String): Call<DashboardDto>

    @GET("/miner/{address}/payouts")
    fun fetchPayoutsLiveData(@Path("address") address: String?): LiveData<ApiResponse<List<Payout>>>

    @GET("/miner/{address}/payouts")
    fun fetchPayouts(@Path("address") address: String?): Call<List<Payout>>
}
package com.marvel.stark.rest.service

import androidx.lifecycle.LiveData
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.room.Payout
import com.marvel.stark.shared.retorift.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**Created by Jahongir on 6/15/2019.*/

interface EthermineService {
    @GET("/miner/{address}/dashboard")
    fun fetchDashboard(@Path("address") address: String?): LiveData<ApiResponse<DashboardDto>>

    @GET("/miner/{address}/payouts")
    fun fetchPayouts(@Path("address") address: String?): LiveData<ApiResponse<List<Payout>>>

    @GET("/miner/{address}/dashboard")
    suspend fun fetchDashboardSuspend(@Path("address") address: String?): DashboardDto

    @GET("/miner/{address}/payouts")
    suspend fun fetchPayoutsSuspend(@Path("address") address: String?): DashboardDto

}
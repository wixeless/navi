package com.marvel.stark.models

import com.marvel.stark.room.Worker
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**Created by Jahongir on 6/19/2019.*/
@JsonClass(generateAdapter = true)
data class DashboardDto(@field:Json(name = "currentStatistics") val statistics: StatisticsDto,
                        @field:Json(name = "workers") val workers: List<Worker>)

@JsonClass(generateAdapter = true)
data class StatisticsDto(val unpaid: Long = 0L,
                         val currentHashrate: Long = 0L,
                         val reportedHashrate: Long = 0L,
                         val activeWorkers: Int = 0)

@JsonClass(generateAdapter = true)
data class WalletAddEntity(val address: String = "", val name: String = "")
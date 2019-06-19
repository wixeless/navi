package com.marvel.stark.ui.wallets

import androidx.annotation.WorkerThread
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.rest.EthermineService
import com.marvel.stark.room.DashboardDao
import javax.inject.Inject

/**Created by Jahongir on 6/17/2019.*/

class WalletsRepository @Inject constructor(
        private val ethermineService: EthermineService,
        private val dashboardDao: DashboardDao) {

    @WorkerThread
    suspend fun insert(dashboardDto: DashboardDto) {
        dashboardDao.insert(dashboardDto)
    }
}
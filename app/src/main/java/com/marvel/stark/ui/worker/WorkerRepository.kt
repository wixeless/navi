package com.marvel.stark.ui.worker

import androidx.lifecycle.LiveData
import com.marvel.stark.common.BaseRepository
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.models.updateThreshold
import com.marvel.stark.repository.NetworkBoundResource
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import com.marvel.stark.room.Worker
import com.marvel.stark.room.WorkerDao
import com.marvel.stark.shared.result.Resource
import com.marvel.stark.shared.retorift.ApiResponse
import javax.inject.Inject

/**Created by Jahongir on 6/24/2019.*/

class WorkerRepository @Inject constructor(private val ethermineService: EthermineService,
                                           private val walletDao: WalletDao,
                                           private val workerDao: WorkerDao) : BaseRepository() {

    fun fetchWorkers(wallet: Wallet): LiveData<Resource<List<Worker>>> {
        return object : NetworkBoundResource<List<Worker>, DashboardDto>(viewModelScope) {

            override suspend fun saveCallResult(requestItem: DashboardDto?) {
                requestItem?.let { body ->
                    wallet.setStatistics(statisticsDto = body.statistics)
                    walletDao.update(wallet)
                    workerDao.cleanInsert(walledId = wallet.id, workers = body.workers)
                }
            }

            override fun shouldFetch(data: List<Worker>?): Boolean {
                val now = System.currentTimeMillis()
                val diff = now - wallet.lastUpdate
                return diff > updateThreshold
            }

            override fun loadFromDb(): LiveData<List<Worker>> {
                return workerDao.getWorkers(walledId = wallet.id)
            }

            override fun createCall(): LiveData<ApiResponse<DashboardDto>> {
                return ethermineService.fetchDashboard(wallet.address)
            }

        }.asLiveData()
    }

}
package com.marvel.stark.ui.home

import androidx.lifecycle.LiveData
import com.marvel.stark.common.BaseRepository
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.models.updateThreshold
import com.marvel.stark.repository.NetworkBoundResource
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import com.marvel.stark.room.WorkerDao
import com.marvel.stark.shared.result.Resource
import com.marvel.stark.shared.retorift.ApiResponse
import javax.inject.Inject

/**Created by Jahongir on 6/24/2019.*/

class HomeRepository @Inject constructor(private val ethermineService: EthermineService,
                                         private val walletDao: WalletDao,
                                         private val workerDao: WorkerDao) : BaseRepository() {

    fun fetchWallet(wallet: Wallet): LiveData<Resource<Wallet>> {
        return object : NetworkBoundResource<Wallet, DashboardDto>(viewModelScope) {

            override suspend fun saveCallResult(requestItem: DashboardDto?) {
                requestItem?.let { body ->
                    wallet.setStatistics(statisticsDto = body.statistics)
                    walletDao.update(wallet)
                    workerDao.cleanInsert(walledId = wallet.id, workers = body.workers)
                }
            }

            override fun shouldFetch(data: Wallet?): Boolean {
                return data?.let {
                    val now = System.currentTimeMillis()
                    val diff = now - it.lastUpdate
                    diff > updateThreshold
                } ?: true
            }

            override fun loadFromDb(): LiveData<Wallet> {
                return walletDao.getWalletLiveData(walledId = wallet.id)
            }

            override fun createCall(): LiveData<ApiResponse<DashboardDto>> {
                return ethermineService.fetchDashboard(wallet.address)
            }

        }.asLiveData()
    }
}
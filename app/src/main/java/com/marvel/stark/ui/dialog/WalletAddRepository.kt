package com.marvel.stark.ui.dialog

import androidx.lifecycle.LiveData
import com.marvel.stark.common.BaseRepository
import com.marvel.stark.models.Coin
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.models.WalletAddEntity
import com.marvel.stark.repository.NetworkResource
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import com.marvel.stark.room.WorkerDao
import com.marvel.stark.shared.result.Resource
import com.marvel.stark.shared.retorift.ApiResponse
import javax.inject.Inject

/**Created by Jahongir on 6/18/2019.*/

class WalletAddRepository @Inject constructor(private val ethermineService: EthermineService,
                                              private val walletDao: WalletDao,
                                              private val workerDao: WorkerDao) : BaseRepository() {

    fun onAddWallet(walletEntity: WalletAddEntity): LiveData<Resource<DashboardDto>> {
        return object : NetworkResource<DashboardDto>(viewModelScope) {

            override suspend fun saveCallResult(requestItem: DashboardDto?) {
                val newWallet = Wallet().apply {
                    this.address = walletEntity.address
                    this.name = walletEntity.name
                    this.coin = Coin.ETH
                }
                requestItem?.let {
                    newWallet.setStatistics(it.statistics)
                }
                val newWalletId = walletDao.insert(newWallet)
                requestItem?.let {
                    workerDao.cleanInsert(newWalletId, it.workers)
                }
            }

            override fun createCall(): LiveData<ApiResponse<DashboardDto>> {
                return ethermineService.fetchDashboard(walletEntity.address)
            }


        }.asLiveData()
    }
}
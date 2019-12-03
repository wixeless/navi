package com.marvel.stark.ui.wallets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.marvel.stark.common.BaseRepository
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.models.bgDispatcher
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import com.marvel.stark.room.WorkerDao
import com.marvel.stark.shared.result.Resource
import com.marvel.stark.shared.retorift.ApiResponse
import com.marvel.stark.shared.retorift.suspendApiCall
import javax.inject.Inject

/**Created by Jahongir on 6/17/2019.*/

class WalletsRepository @Inject constructor(private val ethermineService: EthermineService,
                                            private val walletDao: WalletDao,
                                            private val workerDao: WorkerDao) : BaseRepository() {


    val errorMessages = MutableLiveData<String?>()

    fun fetchWallets(): LiveData<Resource<List<Wallet>>> {
        return liveData(viewModelScope.coroutineContext + bgDispatcher) {
            emit(Resource.loading(null))
            val data = walletDao.getWalletsSuspend()
            emit(Resource.loading(data))
            data.forEach { wlt ->
                val dashboardResult: ApiResponse<DashboardDto> = suspendApiCall { ethermineService.fetchDashboardSuspend(wlt.address) }
                if (dashboardResult.isSuccessful) {
                    saveCallResult(wallet = wlt, dashboardDto = dashboardResult.body)
                } else {
                    errorMessages.postValue(dashboardResult.message)
                }
            }
            val newData = walletDao.getWalletsSuspend()
            emit(Resource.success(newData))
        }
    }

    private suspend fun saveCallResult(wallet: Wallet, dashboardDto: DashboardDto?) {
        dashboardDto?.let { body ->
            wallet.setStatistics(statisticsDto = body.statistics)
            walletDao.update(wallet)
            workerDao.cleanInsert(walledId = wallet.id, workers = body.workers)
        }
    }


}
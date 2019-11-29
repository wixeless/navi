package com.marvel.stark.ui.walletinfo.worker

import androidx.lifecycle.LiveData
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.repository.Resource
import com.marvel.stark.repository.WalletBoundResource
import com.marvel.stark.shared.retorift.ApiResponse
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.room.*
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**Created by Jahongir on 6/24/2019.*/

class WorkerRepository @Inject constructor(private val ethermineService: EthermineService,
                                           private val walletDao: WalletDao,
                                           private val dashboardDao: DashboardDao,
                                           private val workerDao: WorkerDao) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = coroutineScope.coroutineContext

    private lateinit var coroutineScope: CoroutineScope

    fun initWithCoroutine(scope: CoroutineScope) {
        this.coroutineScope = scope
    }


    fun getWorkers(walledId: Long): LiveData<Resource<List<Worker>>> {
        return object : WalletBoundResource<List<Worker>, DashboardDto, String>(coroutineScope) {
            override suspend fun saveCallResult(requestItem: DashboardDto, wallet: Wallet) {
                dashboardDao.update(wallet, requestItem)
            }

            override fun shouldFetch(data: List<Worker>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Worker>> {
                return workerDao.getWorkers(walledId)
            }

            override fun getParams(wallet: Wallet): String? {
                return wallet.address
            }

            override fun loadWalletFromDb(): Wallet {
                return walletDao.getWallet(walledId)
            }

            override fun createCall(params: String?): LiveData<ApiResponse<DashboardDto>> {
                return ethermineService.fetchDashboardLiveData(params)
            }
        }.asLiveData()
    }

}
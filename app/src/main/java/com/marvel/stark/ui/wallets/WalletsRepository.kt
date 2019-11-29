package com.marvel.stark.ui.wallets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.repository.ListBoundResource
import com.marvel.stark.shared.result.Resource
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.room.DashboardDao
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**Created by Jahongir on 6/17/2019.*/

class WalletsRepository @Inject constructor(private val ethermineService: EthermineService,
                                            private val walletDao: WalletDao,
                                            private val dashboardDao: DashboardDao) {


    private lateinit var coroutineScope: CoroutineScope

    val errorMessages = MutableLiveData<String?>()

    fun initWithScope(scope: CoroutineScope) {
        this.coroutineScope = scope
    }

    fun getWallets(): LiveData<Resource<List<Wallet>>> {
        return object : ListBoundResource<Wallet, DashboardDto, String>(coroutineScope, errorMessages) {
            override suspend fun saveCallResult(requestItem: DashboardDto, resultItem: Wallet) {
                dashboardDao.update(resultItem, requestItem)
            }

            override fun shouldFetch(data: Wallet?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Wallet>> {
                return walletDao.getWallets()
            }

            override fun getParams(data: Wallet): String? {
                return data.address
            }

            override suspend fun createCall(params: String?): DashboardDto {
                return ethermineService.fetchDashboardSuspend(params)
            }
        }.asLiveData()
    }

}
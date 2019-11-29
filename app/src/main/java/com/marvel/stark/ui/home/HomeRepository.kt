package com.marvel.stark.ui.home

import androidx.lifecycle.LiveData
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.repository.NetworkResourceWithParams
import com.marvel.stark.shared.retorift.ApiResponse
import com.marvel.stark.room.DashboardDao
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import com.marvel.stark.shared.result.Resource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**Created by Jahongir on 6/24/2019.*/

class HomeRepository @Inject constructor(private val ethermineService: EthermineService,
                                         private val walletDao: WalletDao,
                                         private val dashboardDao: DashboardDao){

    private lateinit var coroutineScope: CoroutineScope

    fun initWithCoroutine(scope: CoroutineScope) {
        this.coroutineScope = scope
    }

    fun getWallet(walletId: Long): LiveData<Resource<Wallet>> {
        return object : NetworkResourceWithParams<Wallet, DashboardDto, String>(coroutineScope) {

            override suspend fun saveCallResult(requestItem: DashboardDto, resultItem: Wallet) {
                dashboardDao.update(resultItem, requestItem)
            }

            override fun shouldFetch(data: Wallet?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<Wallet> {
                return walletDao.getWalletLiveData(walletId)
            }

            override fun getParams(data: Wallet): String? {
                return data.address
            }

            override fun createCall(params: String?): LiveData<ApiResponse<DashboardDto>> {
                return ethermineService.fetchDashboardLiveData(params)
            }

        }.asLiveData()
    }

}
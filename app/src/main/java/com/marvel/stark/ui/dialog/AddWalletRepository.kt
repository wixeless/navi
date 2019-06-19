package com.marvel.stark.ui.dialog

import androidx.lifecycle.LiveData
import com.marvel.stark.models.Coin
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.models.AddWalletEntity
import com.marvel.stark.rest.EthermineService
import com.marvel.stark.rest.NetworkBoundResource
import com.marvel.stark.rest.Resource
import com.marvel.stark.room.DashboardDao
import com.marvel.stark.utils.AbsentLiveData
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**Created by Jahongir on 6/18/2019.*/

class AddWalletRepository @Inject constructor(
        private val ethermineService: EthermineService,
        private val dashboardDao: DashboardDao) {

    fun onAddWallet(walletEntity: AddWalletEntity, coroutineScope: CoroutineScope): LiveData<Resource<DashboardDto>> {
        return object : NetworkBoundResource<DashboardDto, DashboardDto>(coroutineScope) {

            override suspend fun saveCallResult(item: DashboardDto) {
                item.wallet.address = walletEntity.address
                item.wallet.name = walletEntity.name
                //TODO:REMOVE DEFAULT VALUE
                item.wallet.coin = Coin.ETH
                dashboardDao.insert(item)
            }

            override fun shouldFetch(data: DashboardDto?) = true

            override fun createCall() = ethermineService.fetchDashboard(walletEntity.address)

            override fun loadFromDb(): LiveData<DashboardDto> {
                return AbsentLiveData.create()
            }
        }.asLiveData()
    }
}
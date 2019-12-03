package com.marvel.stark.ui.payout

import androidx.lifecycle.LiveData
import com.marvel.stark.common.BaseRepository
import com.marvel.stark.repository.NetworkBoundResource
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.room.Payout
import com.marvel.stark.room.PayoutDao
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import com.marvel.stark.shared.result.Resource
import com.marvel.stark.shared.retorift.ApiResponse
import javax.inject.Inject

/**Created by Jahongir on 6/25/2019.*/

class PayoutRepository @Inject constructor(private val ethermineService: EthermineService,
                                           private val payoutDao: PayoutDao) : BaseRepository() {

    fun fetchPayouts(wallet: Wallet): LiveData<Resource<List<Payout>>> {
        return object : NetworkBoundResource<List<Payout>, List<Payout>>(viewModelScope) {

            override suspend fun saveCallResult(requestItem: List<Payout>?) {
                requestItem?.let { payouts ->
                    payoutDao.insert(walletId = wallet.id, payouts = payouts)
                }
            }

            override fun loadFromDb(): LiveData<List<Payout>> {
                return payoutDao.getPayouts(wallet.id)
            }


            override fun createCall(): LiveData<ApiResponse<List<Payout>>> {
                return ethermineService.fetchPayouts(wallet.address)
            }

        }.asLiveData()
    }
}
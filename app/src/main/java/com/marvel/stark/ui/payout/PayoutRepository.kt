package com.marvel.stark.ui.payout

import androidx.lifecycle.LiveData
import com.marvel.stark.repository.Resource
import com.marvel.stark.repository.WalletBoundResource
import com.marvel.stark.shared.retorift.ApiResponse
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.room.Payout
import com.marvel.stark.room.PayoutDao
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**Created by Jahongir on 6/25/2019.*/

class PayoutRepository @Inject constructor(private val ethermineService: EthermineService,
                                           private val walletDao: WalletDao,
                                           private val payoutDao: PayoutDao) {

    private lateinit var coroutineScope: CoroutineScope

    fun initWithCoroutine(scope: CoroutineScope) {
        this.coroutineScope = scope
    }

    fun getPayouts(walletId: Long): LiveData<Resource<List<Payout>>> {
        return object : WalletBoundResource<List<Payout>, List<Payout>, String>(coroutineScope) {
            override suspend fun saveCallResult(requestItem: List<Payout>, wallet: Wallet) {
                requestItem.forEach { it.walletId = wallet.id }
                payoutDao.insert(wallet.id, requestItem)
            }

            override fun shouldFetch(data: List<Payout>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Payout>> {
                return payoutDao.getPayouts(walletId)
            }

            override fun getParams(wallet: Wallet): String? {
                return wallet.address
            }

            override fun loadWalletFromDb(): Wallet {
                return walletDao.getWallet(walletId)
            }

            override fun createCall(params: String?): LiveData<ApiResponse<List<Payout>>> {
                return ethermineService.fetchPayoutsLiveData(params)
            }

        }.asLiveData()
    }
}
package com.marvel.stark.ui.wallets

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.rest.service.EthermineService
import com.marvel.stark.repository.Resource
import com.marvel.stark.rest.awaitResult
import com.marvel.stark.rest.livedata.ApiResponse
import com.marvel.stark.room.DashboardDao
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import com.marvel.stark.utils.bgDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**Created by Jahongir on 6/17/2019.*/

class WalletsRepository @Inject constructor(private val ethermineService: EthermineService,
                                            private val walletDao: WalletDao,
                                            private val dashboardDao: DashboardDao) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = coroutineScope.coroutineContext

    private lateinit var coroutineScope: CoroutineScope

    private val wallets = MediatorLiveData<Resource<List<Wallet>>>()
    val errorMessages = MutableLiveData<String?>()

    fun initWithScope(scope: CoroutineScope) {
        this.coroutineScope = scope
        val dbSource = this.loadFromDb()
        wallets.addSource(dbSource) { data ->
            wallets.removeSource(dbSource)
            wallets.postValue(Resource.loading(null))
            fetchFromNetwork(data, dbSource)
        }
    }

    fun getWallets() = wallets as LiveData<Resource<List<Wallet>>>

    private fun fetchFromNetwork(walletsList: List<Wallet>, dbSource: LiveData<List<Wallet>>) = launch {
        wallets.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
            wallets.removeSource(dbSource)
        }
        for (wallet in walletsList) {
            //val apiResponse = ethermineService.fetchDashboardDeferred(wallet.address).await()
            val apiResponse = ethermineService.fetchDashboard(wallet.address).awaitResult()
            if (apiResponse.isSuccessful) {
                val response = processResponse(apiResponse)
                response?.let {
                    withContext(bgDispatcher) {
                        saveCallResult(it, wallet)
                    }
                }
            } else {
                errorMessages.postValue(apiResponse.message)
            }
        }
        wallets.addSource(loadFromDb()) { newData ->
            setValue(Resource.success(newData))
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<List<Wallet>>) {
        if (wallets.value != newValue) {
            wallets.value = newValue
        }
    }

    private fun processResponse(response: ApiResponse<DashboardDto>) = response.body

    @WorkerThread
    private fun saveCallResult(item: DashboardDto, wallet: Wallet) {
        dashboardDao.update(wallet, item)
    }

    @MainThread
    private fun loadFromDb() = walletDao.getWallets()

}
package com.marvel.stark.ui.wallets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.marvel.stark.common.BaseViewModel
import com.marvel.stark.room.Wallet
import com.marvel.stark.shared.result.Resource
import javax.inject.Inject

/**Created by Jahongir on 6/19/2019.*/

class WalletsViewModel @Inject constructor(walletsRepository: WalletsRepository) : BaseViewModel(walletsRepository) {

    private val lastUpdate = MutableLiveData<Long>()

    val errorMessages = walletsRepository.errorMessages

    init {
        lastUpdate.value = System.currentTimeMillis()
    }


    val wallets: LiveData<Resource<List<Wallet>>> = lastUpdate.switchMap {
        walletsRepository.fetchWallets()
    }

    fun refresh() {
        lastUpdate.value?.let {
            lastUpdate.value = System.currentTimeMillis()
        }
    }
}
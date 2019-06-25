package com.marvel.stark.ui.walletinfo.home

import androidx.lifecycle.*
import com.marvel.stark.repository.Resource
import com.marvel.stark.room.Wallet
import javax.inject.Inject

/**Created by Jahongir on 6/24/2019.*/

class HomeViewModel @Inject constructor(homeRepository: HomeRepository) : ViewModel() {

    private val walletId = MutableLiveData<Long>()

    init {
        homeRepository.initWithCoroutine(viewModelScope)
    }


    fun setWalletId(walletId: Long) {
        this.walletId.value = walletId
    }

    val wallets: LiveData<Resource<Wallet>> = Transformations
            .switchMap(walletId) { id ->
                homeRepository.getWallet(id)
            }

}
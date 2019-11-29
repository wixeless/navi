package com.marvel.stark.ui.wallets

import androidx.lifecycle.*
import com.marvel.stark.shared.result.Resource
import com.marvel.stark.room.Wallet
import javax.inject.Inject

/**Created by Jahongir on 6/19/2019.*/

class WalletsViewModel @Inject constructor(walletsRepository: WalletsRepository) : ViewModel() {

    private val lastUpdate = MutableLiveData<Long>()

    val errorMessages = walletsRepository.errorMessages

    init {
        lastUpdate.value = System.currentTimeMillis()
        walletsRepository.initWithScope(viewModelScope)
    }


    val wallets: LiveData<Resource<List<Wallet>>> = Transformations
            .switchMap(lastUpdate) {
                walletsRepository.getWallets()
            }

    fun refresh() {
        lastUpdate.value?.let {
            lastUpdate.value = System.currentTimeMillis()
        }
    }
}
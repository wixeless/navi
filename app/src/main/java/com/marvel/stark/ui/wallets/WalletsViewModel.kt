package com.marvel.stark.ui.wallets

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvel.stark.repository.Resource
import com.marvel.stark.room.Wallet
import javax.inject.Inject

/**Created by Jahongir on 6/19/2019.*/

class WalletsViewModel @Inject constructor(walletsRepository: WalletsRepository) : ViewModel() {

    init {
        walletsRepository.initWithScope(viewModelScope)
    }

    val wallets: LiveData<Resource<List<Wallet>>> = walletsRepository.getWallets()
    val errorMessages = walletsRepository.errorMessages
}
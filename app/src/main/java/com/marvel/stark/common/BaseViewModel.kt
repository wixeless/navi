package com.marvel.stark.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvel.stark.room.Wallet

/**Created by Jahongir on 9/9/2019.*/

abstract class BaseViewModel(repository: BaseRepository) : ViewModel() {

    protected val _wallet = MutableLiveData<Wallet>()

    init {
        repository.initScope(viewModelScope)
    }

    fun setWallet(wallet: Wallet) {
        this._wallet.value = wallet
    }
}


package com.marvel.stark.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**Created by Jahongir on 11/29/2019.*/

class SharedViewModel @Inject constructor() : ViewModel() {
    private val _walletId = MutableLiveData<Long>()

    val walletId: LiveData<Long>
        get() = _walletId

    fun setWalletId(walletId: Long) {
        this._walletId.value = walletId
    }
}
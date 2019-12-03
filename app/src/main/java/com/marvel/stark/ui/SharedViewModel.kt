package com.marvel.stark.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.marvel.stark.room.Wallet
import com.marvel.stark.room.WalletDao
import javax.inject.Inject

/**Created by Jahongir on 11/29/2019.*/

class SharedViewModel @Inject constructor(walletDao: WalletDao) : ViewModel() {
    private val _walletId = MutableLiveData<Long>()

    val walletId: LiveData<Long>
        get() = _walletId

    val wallet: LiveData<Wallet> = _walletId.switchMap {
        walletDao.getWalletLiveData(it)
    }

    fun setWalletId(walletId: Long) {
        this._walletId.value = walletId
    }
}
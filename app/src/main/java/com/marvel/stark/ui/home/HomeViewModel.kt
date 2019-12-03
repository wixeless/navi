package com.marvel.stark.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.marvel.stark.common.BaseViewModel
import com.marvel.stark.room.Wallet
import com.marvel.stark.shared.result.Resource
import javax.inject.Inject

/**Created by Jahongir on 6/24/2019.*/

class HomeViewModel @Inject constructor(homeRepository: HomeRepository) : BaseViewModel(homeRepository) {

    val wallet: LiveData<Resource<Wallet>> = _wallet.switchMap {
        homeRepository.fetchWallet(wallet = it)
    }
}
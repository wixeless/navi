package com.marvel.stark.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.marvel.stark.common.BaseViewModel
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.models.WalletAddEntity
import com.marvel.stark.shared.result.Resource
import javax.inject.Inject

/**Created by Jahongir on 6/18/2019.*/

class WalletAddViewModel @Inject constructor(walletAddRepository: WalletAddRepository) : BaseViewModel(walletAddRepository) {

    private val walletEntity = MutableLiveData<WalletAddEntity>()

    val addWalletResult: LiveData<Resource<DashboardDto>> = walletEntity.switchMap {
        walletAddRepository.onAddWallet(it)
    }

    fun onAddWallet(newWalletEntity: WalletAddEntity) = walletEntity.postValue(newWalletEntity)
}
package com.marvel.stark.ui.dialog

import androidx.lifecycle.*
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.models.WalletAddEntity
import com.marvel.stark.repository.Resource
import com.marvel.stark.utils.AbsentLiveData
import javax.inject.Inject

/**Created by Jahongir on 6/18/2019.*/

class WalletAddViewModel @Inject constructor(private val walletAddRepository: WalletAddRepository) : ViewModel() {

    private val walletEntity = MutableLiveData<WalletAddEntity>()

    val addWalletResult: LiveData<Resource<DashboardDto>>

    init {
        addWalletResult = walletEntity.switchMap {
            walletEntity.value?.let { walletAddRepository.onAddWallet(it, viewModelScope) }
                    ?: AbsentLiveData.create()
        }
    }

    fun onAddWallet(newWalletEntity: WalletAddEntity) = walletEntity.postValue(newWalletEntity)
}
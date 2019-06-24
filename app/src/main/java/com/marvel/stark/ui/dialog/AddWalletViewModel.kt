package com.marvel.stark.ui.dialog

import androidx.lifecycle.*
import com.marvel.stark.models.DashboardDto
import com.marvel.stark.models.AddWalletEntity
import com.marvel.stark.rest.Resource
import com.marvel.stark.utils.AbsentLiveData
import javax.inject.Inject

/**Created by Jahongir on 6/18/2019.*/

class AddWalletViewModel @Inject constructor(private val addWalletRepository: AddWalletRepository) : ViewModel() {

    private val walletEntity = MutableLiveData<AddWalletEntity>()

    val addWalletResult: LiveData<Resource<DashboardDto>>

    init {
        addWalletResult = walletEntity.switchMap {
            walletEntity.value?.let { addWalletRepository.onAddWallet(it, viewModelScope) }
                    ?: AbsentLiveData.create()
        }
    }

    fun onAddWallet(newWalletEntity: AddWalletEntity) = walletEntity.postValue(newWalletEntity)
}
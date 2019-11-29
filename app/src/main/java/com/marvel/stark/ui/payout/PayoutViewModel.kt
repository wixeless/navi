package com.marvel.stark.ui.payout

import androidx.lifecycle.*
import com.marvel.stark.repository.Resource
import com.marvel.stark.room.Payout
import javax.inject.Inject

/**Created by Jahongir on 6/25/2019.*/

class PayoutViewModel @Inject constructor(payoutRepository: PayoutRepository) : ViewModel() {

    private val walletId = MutableLiveData<Long>()

    init {
        payoutRepository.initWithCoroutine(viewModelScope)
    }

    fun setWalletId(walletId: Long) {
        this.walletId.value = walletId
    }

    val payouts: LiveData<Resource<List<Payout>>> = Transformations
            .switchMap(walletId) { id ->
                payoutRepository.getPayouts(id)
            }


}
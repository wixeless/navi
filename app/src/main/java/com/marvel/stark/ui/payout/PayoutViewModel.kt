package com.marvel.stark.ui.payout

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.marvel.stark.common.BaseViewModel
import com.marvel.stark.room.Payout
import com.marvel.stark.shared.result.Resource
import javax.inject.Inject

/**Created by Jahongir on 6/25/2019.*/

class PayoutViewModel @Inject constructor(payoutRepository: PayoutRepository) : BaseViewModel(payoutRepository) {

    val payouts: LiveData<Resource<List<Payout>>> = _wallet.switchMap {
        payoutRepository.fetchPayouts(wallet = it)
    }
}
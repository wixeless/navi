package com.marvel.stark.ui.walletinfo.worker

import androidx.lifecycle.*
import com.marvel.stark.repository.Resource
import com.marvel.stark.room.Worker
import javax.inject.Inject

/**Created by Jahongir on 6/24/2019.*/

class WorkerViewModel @Inject constructor(workerRepository: WorkerRepository) : ViewModel() {
    private val walletId = MutableLiveData<Long>()

    init {
        workerRepository.initWithCoroutine(viewModelScope)
    }

    fun setWalletId(walletId: Long) {
        this.walletId.value = walletId
    }

    val workers: LiveData<Resource<List<Worker>>> = Transformations
            .switchMap(walletId) { id ->
                workerRepository.getWorkers(id)
            }
}
package com.marvel.stark.ui.worker

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.marvel.stark.common.BaseViewModel
import com.marvel.stark.room.Worker
import com.marvel.stark.shared.result.Resource
import javax.inject.Inject

/**Created by Jahongir on 6/24/2019.*/

class WorkerViewModel @Inject constructor(workerRepository: WorkerRepository) : BaseViewModel(workerRepository) {

    val workers: LiveData<Resource<List<Worker>>> = _wallet.switchMap {
        workerRepository.fetchWorkers(it)
    }
}
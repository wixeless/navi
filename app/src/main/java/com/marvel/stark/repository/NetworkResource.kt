package com.marvel.stark.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.marvel.stark.shared.result.Resource
import com.marvel.stark.shared.retorift.ApiResponse
import com.marvel.stark.utils.uiDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkResource<ResultType>
@MainThread constructor(private val coroutineScope: CoroutineScope) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.postValue(Resource.loading(null))
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response.isSuccessful) {
                true -> {
                    //Dispatcher IO
                    coroutineScope.launch(Dispatchers.IO) {
                        saveCallResult(response.body)
                        withContext(uiDispatcher) {
                            setValue(Resource.success(response.body))
                        }
                    }
                }
                false -> {
                    onFetchFailed()
                    setValue(Resource.error(response.message, null))
                }
            }
        }
    }

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class
    fun asLiveData() = result as LiveData<Resource<ResultType>>

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract suspend fun saveCallResult(requestItem: ResultType?)

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<ResultType>>
}
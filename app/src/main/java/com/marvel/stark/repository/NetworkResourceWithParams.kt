package com.marvel.stark.repository

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.marvel.stark.shared.retorift.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkResourceWithParams<ResultType, RequestType, ServiceParams>
@MainThread constructor(private val coroutineScope: CoroutineScope) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        val dbSource = this.loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                result.postValue(Resource.loading(null))
                fetchFromNetwork(dbSource, data)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>, data: ResultType) {
        val params = getParams(data)
        val apiResponse = createCall(params)

        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response.isSuccessful) {
                true -> {
                    //Dispatcher IO
                    coroutineScope.launch(Dispatchers.IO) {
                        val processedResponse = processResponse(response)
                        processedResponse?.let { body ->
                            saveCallResult(body, data)
                        }
                        //Dispatcher Main
                        coroutineScope.launch {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                false -> {
                    Log.d("NetworkResource", "fetchFromNetwork: ${response.message}")
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.message, newData))
                    }
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

    @WorkerThread
    protected open fun processResponse(response: ApiResponse<RequestType>) = response.body

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract suspend fun saveCallResult(requestItem: RequestType, resultItem: ResultType)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    protected abstract fun getParams(data: ResultType): ServiceParams?

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(params: ServiceParams?): LiveData<ApiResponse<RequestType>>
}
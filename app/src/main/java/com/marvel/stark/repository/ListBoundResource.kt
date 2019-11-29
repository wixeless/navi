package com.marvel.stark.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.marvel.stark.models.bgDispatcher
import com.marvel.stark.shared.result.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class ListBoundResource<ResultType, RequestType, ServiceParams>
@MainThread constructor(private val coroutineScope: CoroutineScope,
                        private val errorMessage: MutableLiveData<String?>?) {

    private val result = MediatorLiveData<Resource<List<ResultType>>>()

    init {
        val dbSource = this.loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            result.postValue(Resource.loading(null))
            fetchFromNetwork(dbSource, data)
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<List<ResultType>>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<List<ResultType>>, data: List<ResultType>) = coroutineScope.launch {
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
            result.removeSource(dbSource)
        }
        withContext(bgDispatcher) {
            for (item in data) {
                if (shouldFetch(item)) {
                    val params = getParams(item)
                    try {
                        val requestItem = createCall(params)
                        saveCallResult(requestItem, item)
                    } catch (ex: Exception) {
                        errorMessage?.postValue(ex.message)
                    }
                }
            }
        }
        result.addSource(loadFromDb()) { newData ->
            setValue(Resource.success(newData))
        }
    }


    // Returns a LiveData object that represents the resource that's implemented
    // in the base class
    fun asLiveData() = result as LiveData<Resource<List<ResultType>>>

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract suspend fun saveCallResult(requestItem: RequestType, resultItem: ResultType)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<List<ResultType>>

    @MainThread
    protected abstract fun getParams(data: ResultType): ServiceParams?

    // Called to create the API call.
    @MainThread
    protected abstract suspend fun createCall(params: ServiceParams?): RequestType
}
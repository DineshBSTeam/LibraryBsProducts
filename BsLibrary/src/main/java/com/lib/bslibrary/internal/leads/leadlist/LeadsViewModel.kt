package com.lib.bslibrary.internal.leads.leadlist

import ApiRepository
import NetworkState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lib.bslibrary.internal.helper.Constants
import com.lib.bslibrary.internal.leads.leadlist.models.*
import kotlinx.coroutines.*

class LeadsViewModel constructor(private val mainRepository: ApiRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val selectedStatus = MutableLiveData<Int>()

    val leadsResponse = MutableLiveData<LeadListingData>()

    var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    var currentPage = 1
    val loading = MutableLiveData<Boolean>()
    val loadMore = MutableLiveData<Boolean>()

    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun getLeadsListing() {
        currentPage = 1
        var pId: String? = null
        if (selectedStatus.value != 0) pId = selectedStatus.value.toString()

        val requestBody = LeadListingRequest(Constants.agentMobile, currentPage, pId)

        viewModelScope.launch {
            Log.d("Thread Inside", Thread.currentThread().name)
            loading.value = true
            when (val response = mainRepository.getLeadsListing(requestBody)) {
                is NetworkState.Success -> {
                    leadsResponse.postValue(response.data.data)
                    loading.value = false
                }
                is NetworkState.Error -> {
                    loading.value = false
                    if (response.response.code() == 401) {} else {}
                }
                else -> {}
            }
        }
    }
    fun getLeadsLoadMore() {
        var pId: String? = null
        if (selectedStatus.value != 0) pId = selectedStatus.value.toString()

        val requestBody = LeadListingRequest(Constants.agentMobile, currentPage, pId)

        viewModelScope.launch {
            Log.d("Thread Inside", Thread.currentThread().name)
            loadMore.value = true
            when (val response = mainRepository.getLeadsListing(requestBody)) {
                is NetworkState.Success -> {
                    leadsResponse.postValue(response.data.data)
                    loadMore.value = false
                }
                is NetworkState.Error -> {
                    loadMore.value = false
                    if (response.response.code() == 401) {} else {}
                }
                else -> {}
            }
        }
    }
}

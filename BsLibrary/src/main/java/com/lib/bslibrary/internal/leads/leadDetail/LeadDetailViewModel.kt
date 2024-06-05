package com.lib.bslibrary.internal.leads.leadDetail

import ApiRepository
import NetworkState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lib.bslibrary.internal.leads.leadDetail.models.LeadDetailData
import com.lib.bslibrary.internal.leads.leadDetail.models.LeadDetailRequest
import kotlinx.coroutines.*

class LeadDetailViewModel constructor(private val mainRepository: ApiRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val leadDetail = MutableLiveData<LeadDetailData>()

    var job: Job? = null

    var leadId : Int = 0

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val loading = MutableLiveData<Boolean>()

    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun getLeadDetail(leadId: Int) {
        Log.d("Thread Outside", Thread.currentThread().name)

        val requestBody = LeadDetailRequest(
            leadId
        )
        loading.value = true
        viewModelScope.launch {
            Log.d("Thread Inside", Thread.currentThread().name)
            when (val response = mainRepository.getLeadDetail(requestBody)) {
                is NetworkState.Success -> {
                    leadDetail.postValue(response.data.data)
                    loading.value = false
                }
                is NetworkState.Error -> {
                    loading.value = false
                    if (response.response.code() == 401) {
                    } else {
                    }
                }
                else -> {}
            }
        }
    }
}
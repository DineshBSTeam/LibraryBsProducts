package com.lib.bslibrary.internal.products.productDetail

import ApiRepository
import NetworkState
import ProductDetailData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lib.bslibrary.internal.products.productDetail.models.ProductDetailRequest
import kotlinx.coroutines.*

class ProductDetailViewModel constructor(private val mainRepository: ApiRepository) : ViewModel() {

    var productId : Int = 0

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val productDetail = MutableLiveData<ProductDetailData>()

    var job: Job? = null

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

    fun getProductDetail(productId: Int) {
        Log.d("Thread Outside", Thread.currentThread().name)
        val requestBody = ProductDetailRequest(
            productId = productId
        )
        viewModelScope.launch {
            Log.d("Thread Inside", Thread.currentThread().name)
            when (val response = mainRepository.getProductDetail(requestBody)) {
                is NetworkState.Success -> {
                    productDetail.postValue(response.data.data)
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
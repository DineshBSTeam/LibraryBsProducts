package com.lib.bslibrary.internal.products.productList

import ApiRepository
import NetworkState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lib.bslibrary.internal.products.productList.models.CreditCardBenefits
import com.lib.bslibrary.internal.products.productList.models.ProductCategoriesData
import com.lib.bslibrary.internal.products.productList.models.ProductListingData
import com.lib.bslibrary.internal.products.productList.models.ProductListingRequest
import kotlinx.coroutines.*

class ProductsViewModel constructor(private val mainRepository: ApiRepository) : ViewModel() {

    var selectedCategoryId: Int = 0

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val productCategories = MutableLiveData<List<ProductCategoriesData>>()
    val copyProductList = MutableLiveData<List<ProductListingData>>()
    val productList = MutableLiveData<List<ProductListingData>>()
    val cardBenefitsList = MutableLiveData<List<CreditCardBenefits>>()

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
    }

    fun getProductCategories() {
        loading.value = true
        viewModelScope.launch {
            when (val response = mainRepository.getProductCategories()) {
                is NetworkState.Success -> {
                    productCategories.postValue(response.data.data)
                    if(response.data.data.isNotEmpty()){
                        selectedCategoryId = response.data.data[0].categoryId
                    }
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

    fun getProductListing(searchString: String) {
        Log.d("Thread ProductListing", searchString)
        if (loading.value == false) {
            loading.value = true
            val responseBody =
                ProductListingRequest(categoryId = selectedCategoryId, searchString = searchString)
            viewModelScope.launch {
                Log.d("Thread Inside", Thread.currentThread().name)
                when (val response = mainRepository.getProductListing(responseBody)) {
                    is NetworkState.Success -> {
                        loading.value = false
                        copyProductList.postValue(response.data.data?.products)
                        productList.postValue(response.data.data?.products)
                        cardBenefitsList.postValue(response.data.data?.creditCardBenefits)
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
    fun searchProducts(searchString: String) {
        loading.value = true
        val responseBody =
            ProductListingRequest(categoryId = selectedCategoryId, searchString = searchString)
        viewModelScope.launch {
            Log.d("Thread Inside", Thread.currentThread().name)
            when (val response = mainRepository.getProductListing(responseBody)) {
                is NetworkState.Success -> {
                    loading.value = false
                    copyProductList.postValue(response.data.data?.products)
                    productList.postValue(response.data.data?.products)
                    cardBenefitsList.postValue(response.data.data?.creditCardBenefits)
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

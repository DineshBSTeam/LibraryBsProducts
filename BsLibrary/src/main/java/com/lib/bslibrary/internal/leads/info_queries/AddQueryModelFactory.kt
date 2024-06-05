package com.lib.bslibrary.internal.leads.info_queries

import ApiRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddQueryModelFactory constructor(private val repository: ApiRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddQueryViewModel::class.java)) {
            AddQueryViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
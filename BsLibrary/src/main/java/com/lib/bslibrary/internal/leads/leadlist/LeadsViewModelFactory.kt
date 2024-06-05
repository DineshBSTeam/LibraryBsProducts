package com.lib.bslibrary.internal.leads.leadlist

import ApiRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LeadsViewModelFactory constructor(private val repository: ApiRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LeadsViewModel::class.java)) {
            LeadsViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
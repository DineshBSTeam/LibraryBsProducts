package com.lib.bslibrary.internal.leads.leadDetail

import ApiRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LeadDetailModelFactory constructor(private val repository: ApiRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LeadDetailViewModel::class.java)) {
            LeadDetailViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
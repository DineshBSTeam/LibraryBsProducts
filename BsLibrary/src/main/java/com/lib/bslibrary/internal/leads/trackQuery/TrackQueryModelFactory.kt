package com.lib.bslibrary.internal.leads.trackQuery

import ApiRepository
import TrackQueryViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrackQueryModelFactory constructor(private val repository: ApiRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TrackQueryViewModel::class.java)) {
            TrackQueryViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
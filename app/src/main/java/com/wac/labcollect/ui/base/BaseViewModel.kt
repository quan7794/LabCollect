package com.wac.labcollect.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wac.labcollect.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus

abstract class BaseViewModel : ViewModel() {
    val ioScope = viewModelScope + Dispatchers.IO
    private var _currentStatus = MutableLiveData<Resource<Nothing>>()
    val currentStatus: LiveData<Resource<Nothing>>
        get() = _currentStatus

    fun updateProgress(status: Resource<Nothing>) {
        _currentStatus.postValue(status)
    }
}
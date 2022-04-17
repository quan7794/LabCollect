package com.wac.labcollect.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wac.labcollect.utils.StatusControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus

abstract class BaseViewModel : ViewModel() {
    val ioScope = viewModelScope + Dispatchers.IO
    private var _currentStatus = MutableLiveData<StatusControl<Nothing>>()
    val currentStatus: LiveData<StatusControl<Nothing>>
        get() = _currentStatus

    fun updateProgress(status: StatusControl<Nothing>) {
        _currentStatus.postValue(status)
    }
}
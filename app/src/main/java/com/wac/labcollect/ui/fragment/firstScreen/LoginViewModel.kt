package com.wac.labcollect.ui.fragment.firstScreen

import androidx.lifecycle.*
import com.wac.labcollect.ui.base.BaseViewModel
import com.wac.labcollect.utils.Resource
import com.wac.labcollect.utils.Status

class LoginViewModel : BaseViewModel() {
    private var _currentStatus = MutableLiveData<Resource<Nothing>>()
    val currentStatus: LiveData<Resource<Nothing>>
        get() = _currentStatus

    fun updateProgress(status: Resource<Nothing>) {
        _currentStatus.postValue(status)
    }
}

//class LoginViewModelFactory() : ViewModelProvider.Factory {
//    @Suppress("Unchecked_cast")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(LoginViewModelFactory::class.java))
//            return LoginViewModelFactory() as T
//        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
//    }
//}
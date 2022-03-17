package com.wac.labcollect.ui.fragment.firstScreen.homeTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.wac.labcollect.data.repository.test.TestRepository

class HomeTabViewModel(val repository: TestRepository) : ViewModel() {
    val tests = repository.tests.asLiveData()
}

class HomeTabViewModelFactory(val repository: TestRepository): ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeTabViewModel::class.java))
            return HomeTabViewModel(repository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }
}
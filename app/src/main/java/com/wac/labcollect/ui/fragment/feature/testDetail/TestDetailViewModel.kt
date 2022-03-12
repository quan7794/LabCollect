package com.wac.labcollect.ui.fragment.feature.testDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.wac.labcollect.data.repository.TestRepository

class ManageTestViewModel(val repository: TestRepository) : ViewModel() {
    fun getTest(testUniqueName: String) = repository.getTest(testUniqueName).asLiveData()
}

class TestDetailViewModelFactory(val repository: TestRepository): ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageTestViewModel::class.java))
            return ManageTestViewModel(repository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }
}
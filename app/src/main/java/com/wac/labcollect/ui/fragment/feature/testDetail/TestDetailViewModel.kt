package com.wac.labcollect.ui.fragment.feature.testDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wac.labcollect.data.repository.test.TestRepository

class ManageTestViewModel(val repository: TestRepository) : ViewModel() {
    suspend fun getTest(testUniqueName: String) = repository.getTest(testUniqueName)
}

class TestDetailViewModelFactory(val repository: TestRepository): ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageTestViewModel::class.java))
            return ManageTestViewModel(repository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }
}
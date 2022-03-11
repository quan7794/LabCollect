package com.wac.labcollect.ui.fragment.feature.createTest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wac.labcollect.data.repository.TestRepository
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseViewModel

class CreateTestViewModel(val repository: TestRepository) : BaseViewModel() {
    suspend fun createTest(test: Test) {
        repository.createTest(test)
    }
}

class CreateTestViewModelFactory(val repository: TestRepository) : ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTestViewModel::class.java))
            return CreateTestViewModel(repository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }

}
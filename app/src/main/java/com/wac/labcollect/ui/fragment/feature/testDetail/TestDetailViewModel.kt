package com.wac.labcollect.ui.fragment.feature.testDetail

import androidx.lifecycle.*
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageTestViewModel(val testRepository: TestRepository) : ViewModel() {
    private var _currentTest = MutableLiveData<Test>()
    val currentTest: LiveData<Test>
        get() = _currentTest

    fun init(spreadId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            testRepository.getTestBySpreadId(spreadId).let { _currentTest.postValue(it) }
        }
    }
}

class TestDetailViewModelFactory(val repository: TestRepository): ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageTestViewModel::class.java))
            return ManageTestViewModel(repository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }
}
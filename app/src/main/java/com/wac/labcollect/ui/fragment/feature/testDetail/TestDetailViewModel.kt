package com.wac.labcollect.ui.fragment.feature.testDetail

import androidx.lifecycle.*
import com.wac.labcollect.data.repository.sheet.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TestDetailViewModel(val testRepository: TestRepository, val googleApiRepository: GoogleApiRepository) : ViewModel() {
    private var _currentTest = MutableLiveData<Test?>()
    val currentTest: LiveData<Test?>
        get() = _currentTest

    fun init(spreadId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var test = testRepository.getTestBySpreadId(spreadId)
            if (test == null) {
                Timber.e("Current test from local database null, getting data from spreadsheet")
                test = googleApiRepository.initTestInfoFromSpread(spreadId)
                test?.let {testRepository.createTest(it)}
            }
            _currentTest.postValue(test)
        }
    }
}

class TestDetailViewModelFactory(val repository: TestRepository, val googleApiRepository: GoogleApiRepository) : ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestDetailViewModel::class.java))
            return TestDetailViewModel(repository, googleApiRepository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }
}
package com.wac.labcollect.ui.fragment.feature.testDetail

import androidx.lifecycle.*
import com.wac.labcollect.data.repository.googleApi.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TestDetailViewModel(val testRepository: TestRepository, val googleApiRepository: GoogleApiRepository) : ViewModel() {
    private var _currentTest = MutableLiveData<Test?>()
    val currentTest: LiveData<Test?>
        get() = _currentTest

    private var _testData = MutableLiveData<List<List<Any>>>()
    val testData: LiveData<List<List<Any>>>
        get() = _testData

    fun init(spreadId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var test = testRepository.getTestBySpreadId(spreadId) //get test from local first.
            if (test == null) {
                Timber.e("Current test from local database null, getting data from spreadsheet")
                test = googleApiRepository.getTestInfoFromSpread(spreadId)
                test?.let { testRepository.createTest(test)}
            }
            _currentTest.postValue(test)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val spreadData = googleApiRepository.readSpreadSheet(spreadId, "Data", "")
            spreadData?.getValues()?.let {
                Timber.d("Spread data: $it")
                _testData.postValue(it as List<List<Any>>)
            }
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
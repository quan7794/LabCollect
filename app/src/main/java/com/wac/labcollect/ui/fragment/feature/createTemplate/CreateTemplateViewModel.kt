package com.wac.labcollect.ui.fragment.feature.createTemplate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant
import com.wac.labcollect.data.repository.googleApi.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseViewModel
import timber.log.Timber

class CreateTemplateViewModel(private val testRepository: TestRepository, private val googleApiRepository: GoogleApiRepository) : BaseViewModel() {
    private var _currentTest = MutableLiveData<Test>()
    private val currentTest: LiveData<Test>
    get() = _currentTest

    suspend fun createLocalTest(test: Test) = testRepository.createTest(test)

    suspend fun createSpread(test: Test): String {
        var spreadId: String
        googleApiRepository.apply {
            spreadId = createSpreadAtDir(test.title, GoogleApiConstant.ROOT_DIR_ID).second
            updateSheetInformation(spreadId, test)
        }
        return spreadId
    }

    suspend fun insert(template: Template) = testRepository.insertTemplate(template)

    suspend fun getTestBySpreadId(spreadId: String) = testRepository.getTestBySpreadId(spreadId)

    fun setCurrentTest(test: Test) {
        _currentTest.postValue(test)
    }

    suspend fun addTemplateToNewTest(temp: Template): Boolean {
        currentTest.value?.let { test ->
            try {
                val range = "A1:Z1"
                val inputOption = "RAW"
                googleApiRepository.updateSpread(test.spreadId, range, inputOption, temp.fields)
                testRepository.updateTest(test.copy(template = temp))
                Timber.e("Done to create. Go to test page.")
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e("Create error. Detail: $e")
            }
        }
        return false
    }
}

@Suppress("Unchecked_cast")
class CreateTemplateViewModelFactory(private val testRepository: TestRepository, private val googleApiRepository: GoogleApiRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTemplateViewModel::class.java)) {
            return CreateTemplateViewModel(testRepository, googleApiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.simpleName}")
    }
}
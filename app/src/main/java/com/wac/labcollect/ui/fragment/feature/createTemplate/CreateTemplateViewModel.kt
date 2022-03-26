package com.wac.labcollect.ui.fragment.feature.createTemplate

import androidx.lifecycle.*
import com.wac.labcollect.data.repository.googleApi.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseViewModel
import timber.log.Timber

class CreateTemplateViewModel(private val testRepository: TestRepository, private val googleApiRepository: GoogleApiRepository) : BaseViewModel() {
    private var _parentTest = MutableLiveData<Test>()
    private val parentTest: LiveData<Test>
    get() = _parentTest

    suspend fun insert(template: Template) = testRepository.insertTemplate(template)

    suspend fun getTestBySpreadId(spreadId: String) = testRepository.getTestBySpreadId(spreadId)

    fun setParentTest(test: Test) {
        _parentTest.postValue(test)
    }

    suspend fun addTemplateToNewTest(temp: Template): Boolean {
        parentTest.value?.let { test ->
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
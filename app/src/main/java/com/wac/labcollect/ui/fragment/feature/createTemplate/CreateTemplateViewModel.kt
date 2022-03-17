package com.wac.labcollect.ui.fragment.feature.createTemplate

import androidx.lifecycle.*
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseViewModel
import timber.log.Timber

class CreateTemplateViewModel(private val repository: TestRepository) : BaseViewModel() {
    private var _parentTest = MutableLiveData<Test>()
    private val parentTest: LiveData<Test>
    get() = _parentTest

    suspend fun insert(template: Template) = repository.insertTemplate(template)

    suspend fun getTest(testUniqueName: String) = repository.getTest(testUniqueName)

    fun setParentTest(test: Test) {
        _parentTest.value = test
    }

    suspend fun addTemplateToNewTest(temp: Template): Boolean {
        parentTest.value?.let {
            repository.updateTest(it.copy(template = temp))
            Timber.e("Done to create. Go to test page.")
            return true
        }
        return false
    }
}

@Suppress("Unchecked_cast")
class CreateTemplateViewModelFactory(private val repository: TestRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTemplateViewModel::class.java)) {
            return CreateTemplateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.simpleName}")
    }
}
package com.wac.labcollect.ui.fragment.feature.manageTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.wac.labcollect.data.repository.TestRepository
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.ui.base.BaseViewModel

class ManageTemplateViewModel(private val repository: TestRepository) : BaseViewModel() {

    suspend fun updateTest(testUniqueName: String, template: Template) {
        val test = repository.getTest(testUniqueName)
        test.template = template
        repository.updateTest(test)
    }

    val templates = repository.templates.asLiveData()

}

class ManageTemplateViewModelFactory(private val testRepository: TestRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageTemplateViewModel::class.java)) return ManageTemplateViewModel(testRepository) as T
        throw IllegalArgumentException("I don't know this ViewModel: ${modelClass.simpleName} ")
    }
}
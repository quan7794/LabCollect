package com.wac.labcollect.ui.fragment.feature.templateManage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.wac.labcollect.data.repository.TemplateRepository
import com.wac.labcollect.ui.base.BaseViewModel

class TemplateManagerFragmentViewModel(repository: TemplateRepository) : BaseViewModel() {

    val templates = repository.allTemplates.asLiveData()

}

class TemplateManagerFragmentViewModelFactory(private val templateRepository: TemplateRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemplateManagerFragmentViewModel::class.java)) return TemplateManagerFragmentViewModel(templateRepository) as T
        throw IllegalArgumentException("I don't know this ViewModel: ${modelClass.simpleName} ")
    }
}
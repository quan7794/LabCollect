package com.wac.labcollect.ui.fragment.feature.createTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wac.labcollect.data.repository.TemplateRepository
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CreateTemplateViewModel(private val repository: TemplateRepository) : BaseViewModel() {
    fun insert(template: Template) = ioScope.launch {
        repository.insertTemplate(template)
    }
}

@Suppress("Unchecked_cast")
class CreateTemplateViewModelFactory(private val repository: TemplateRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTemplateViewModel::class.java)) {
            return CreateTemplateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.simpleName}")
    }
}
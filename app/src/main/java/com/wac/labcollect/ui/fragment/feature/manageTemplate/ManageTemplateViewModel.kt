package com.wac.labcollect.ui.fragment.feature.manageTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.wac.labcollect.data.repository.TestRepository
import com.wac.labcollect.ui.base.BaseViewModel

class ManageTemplateViewModel(repository: TestRepository) : BaseViewModel() {

    val templates = repository.allTemplates.asLiveData()

}

class ManageTemplateViewModelFactory(private val testRepository: TestRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageTemplateViewModel::class.java)) return ManageTemplateViewModel(testRepository) as T
        throw IllegalArgumentException("I don't know this ViewModel: ${modelClass.simpleName} ")
    }
}
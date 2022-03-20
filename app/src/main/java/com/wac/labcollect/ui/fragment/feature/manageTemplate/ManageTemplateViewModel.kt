package com.wac.labcollect.ui.fragment.feature.manageTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.wac.labcollect.data.repository.sheet.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.ui.base.BaseViewModel
import timber.log.Timber

class ManageTemplateViewModel(private val testRepository: TestRepository, private val googleApiRepository: GoogleApiRepository) : BaseViewModel() {

    suspend fun updateTest(spreadId: String, template: Template) : Boolean{
        testRepository.getTestBySpreadId(spreadId)?.let { test ->
            try {
                val range = "A1:Z1"
                val inputOption = "RAW"
                googleApiRepository.updateSpread(test.spreadId, range, inputOption, template.fields)
                testRepository.updateTest(test.copy(template = template))
                Timber.e("Done to add template into test. Go to test page.")
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e("update template to test error. Detail: $e")
            }
        }
        return false
    }

    val templates = testRepository.templates.asLiveData()

}

class ManageTemplateViewModelFactory(private val testRepository: TestRepository, private val googleApiRepository: GoogleApiRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageTemplateViewModel::class.java)) return ManageTemplateViewModel(testRepository, googleApiRepository) as T
        throw IllegalArgumentException("I don't know this ViewModel: ${modelClass.simpleName} ")
    }
}
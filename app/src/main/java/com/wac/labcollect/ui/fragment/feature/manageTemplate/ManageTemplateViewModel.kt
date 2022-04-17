package com.wac.labcollect.ui.fragment.feature.manageTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant
import com.wac.labcollect.data.repository.googleApi.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseViewModel
import timber.log.Timber

class ManageTemplateViewModel(private val testRepository: TestRepository, private val googleApiRepository: GoogleApiRepository) : BaseViewModel() {

    val templates = testRepository.templates.asLiveData()

    suspend fun createLocalTest(test: Test) = testRepository.createTest(test)

    suspend fun createSpread(test: Test): String {
        var spreadId: String
        googleApiRepository.apply {
            spreadId = createSpreadAtDir(test.title, GoogleApiConstant.ROOT_DIR_ID).second
            updateSheetInformation(spreadId, test)
        }
        return spreadId
    }

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


}

class ManageTemplateViewModelFactory(private val testRepository: TestRepository, private val googleApiRepository: GoogleApiRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageTemplateViewModel::class.java)) return ManageTemplateViewModel(testRepository, googleApiRepository) as T
        throw IllegalArgumentException("I don't know this ViewModel: ${modelClass.simpleName} ")
    }
}
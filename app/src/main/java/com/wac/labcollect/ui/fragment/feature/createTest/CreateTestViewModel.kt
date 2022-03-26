package com.wac.labcollect.ui.fragment.feature.createTest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant
import com.wac.labcollect.data.repository.googleApi.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.ui.base.BaseViewModel

class CreateTestViewModel(val testRepository: TestRepository, private val googleApiRepository: GoogleApiRepository) : BaseViewModel() {

    suspend fun createTest(test: Test) {
        testRepository.createTest(test)
    }

    suspend fun createSpread(test: Test): String {
        var spreadId: String
        googleApiRepository.apply {
            spreadId = createSpreadAtDir(test.title, GoogleApiConstant.ROOT_DIR_ID).second
            updateSheetInformation(spreadId, test)
        }
        return spreadId
    }
}

class CreateTestViewModelFactory(val repository: TestRepository, private val googleApiRepository: GoogleApiRepository) : ViewModelProvider.Factory {
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTestViewModel::class.java))
            return CreateTestViewModel(repository, googleApiRepository) as T
        throw IllegalArgumentException("Unknown ${modelClass.simpleName}")
    }

}
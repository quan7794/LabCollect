package com.wac.labcollect.data.repository.test

import androidx.annotation.WorkerThread
import com.wac.labcollect.data.database.TestDao
import com.wac.labcollect.data.network.TestAPI
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.domain.models.Test
import com.wac.labcollect.utils.Constants
import kotlinx.coroutines.flow.Flow

class TestRepository(private val testDao: TestDao, private val networkTestAPI: TestAPI) {

    @WorkerThread
    suspend fun createTest(test: Test) {
        testDao.createTest(test)
    }

    @WorkerThread
    suspend fun updateTest(test: Test) {
        testDao.updateTest(test)
    }

    @WorkerThread
    suspend fun getTemplates() = networkTestAPI.getTemplates(Constants.API_KEY_VALUE)

    @WorkerThread
    suspend fun insertTemplate(template: Template) {
        testDao.insertTemplate(template)
    }

    @WorkerThread
    suspend fun updateTemplate(template: Template) {
        testDao.updateTemplate(template)
    }

    @WorkerThread
    suspend fun deleteTemplate(template: Template) {
        testDao.deleteTemplate(template)
    }

    @WorkerThread
    suspend fun getTest(testUniqueName: String) = testDao.getTest(testUniqueName)

    val tests: Flow<List<Test>> = testDao.getAllTest()
    val templates: Flow<List<Template>> = testDao.getAllTemplates()
    val allPublicTemplates: Flow<List<Template>> = testDao.getPublicTemplates()
//    fun getTemplateByFilter(key: String, value: String): Flow<List<Template>> = templateDao.getTemplateByFilter(key, value)
}
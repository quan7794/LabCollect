package com.wac.labcollect.data.repository

import androidx.annotation.WorkerThread
import com.wac.labcollect.data.database.TemplateDao
import com.wac.labcollect.data.network.TemplateAPI
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.util.Constants
import kotlinx.coroutines.flow.Flow

class TemplateRepository(private val templateDao: TemplateDao, private val networkTemplateAPI: TemplateAPI) {
    @WorkerThread
    suspend fun getTemplates() = networkTemplateAPI.getTemplates(Constants.API_KEY_VALUE)

    @WorkerThread
    suspend fun insertTemplate(template: Template) {
        templateDao.insertTemplate(template)
    }

    @WorkerThread
    suspend fun updateTemplate(template: Template) {
        templateDao.updateTemplate(template)
    }

    @WorkerThread
    suspend fun deleteTemplate(template: Template) {
        templateDao.deleteTemplate(template)
    }

    val allTemplates: Flow<List<Template>> = templateDao.getAllTemplates()
    val allPublicTemplates: Flow<List<Template>> = templateDao.getPublicTemplates()
    fun getTemplateByFilter(key: String, value: String): Flow<List<Template>> = templateDao.getTemplateByFilter(key, value)
}
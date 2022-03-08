package com.wac.labcollect.data.database

import androidx.room.*
import com.wac.labcollect.domain.models.Template
import com.wac.labcollect.domain.models.Test
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {

    @Insert
    suspend fun insertTemplate(template: Template)

    @Query("Select * from template_table order by id")
    fun getAllTemplates(): Flow<List<Template>>

    @Update
    suspend fun updateTemplate(test: Template)

    @Query("Select * from template_table where isPublic = 1")
    fun getPublicTemplates(): Flow<List<Template>>

    @Delete
    suspend fun deleteTemplate(dish: Template)

//    @Query("""Select * from template_table where
//            case :key
//                when 'type' then type
//                when 'isPublic' then isPublic
//            end = :value""")
//    fun getTemplateByFilter(key: String, value: String): Flow<List<Template>>
}
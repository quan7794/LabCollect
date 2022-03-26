package com.wac.labcollect.data.repository.googleApi

import androidx.annotation.WorkerThread
import com.google.api.services.sheets.v4.model.ValueRange

interface GoogleApiDataSource {

    @WorkerThread
    suspend fun readSpreadSheet(spreadSheetId : String, tabName: String, range : String): ValueRange?

    @WorkerThread
    suspend fun createSpreadsheet(title : String): String?

    @WorkerThread
    suspend fun getFilesAtRoot(): List<Triple<String, String, List<String>>>?
    @WorkerThread
    suspend fun getFilesAtDir(dirId: String): List<Triple<String, String, List<String>>>

    @WorkerThread
    suspend fun moveFileToDir(fileId: String, dirId: String): List<String>

    @WorkerThread
    suspend fun createSpreadAtDir(title: String, dirId: String): Pair<Boolean, String>

}
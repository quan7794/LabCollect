package com.wac.labcollect.data.repository.sheet

import androidx.annotation.WorkerThread
import com.google.api.services.sheets.v4.model.Spreadsheet

interface GoogleApiDataSource {

    @WorkerThread
    suspend fun readSpreadSheet(spreadSheetId : String, spreadSheetRange : String)

    @WorkerThread
    suspend fun createSpreadsheet(title : String): String?

    @WorkerThread
    suspend fun getAllFiles(): List<Pair<String, String>>?

    @WorkerThread
    suspend fun moveFileToDir(fileId: String, dirId: String): List<String>

    @WorkerThread
    suspend fun createSpreadAtDir(title: String, dirId: String): Pair<Boolean, String>

}
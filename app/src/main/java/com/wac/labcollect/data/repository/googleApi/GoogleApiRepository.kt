package com.wac.labcollect.data.repository.googleApi

import android.accounts.Account
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.*
import com.google.gson.Gson
import com.wac.labcollect.data.manager.AuthenticationManager
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant.ID_KEY
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant.PARENT_FIELD_KEY
import com.wac.labcollect.data.repository.googleApi.GoogleApiConstant.ROOT_DIR_ID
import com.wac.labcollect.domain.models.Field
import com.wac.labcollect.domain.models.Test
import timber.log.Timber


class GoogleApiRepository(
    private val authManager: AuthenticationManager, private val transport: HttpTransport,
    private val jsonFactory: JsonFactory, private val lastSignedAccount: Account?
) : GoogleApiDataSource {

    fun getSpreadService(): Sheets {
        authManager.setUpGoogleAccountCredential(lastSignedAccount)
        return Sheets.Builder(transport, jsonFactory, authManager.googleAccountCredential)
            .setApplicationName("LabCollect")
            .build()
    }

    private fun getDriveService(): Drive {
        authManager.setUpGoogleAccountCredential(lastSignedAccount)
        return Drive.Builder(transport, jsonFactory, authManager.googleAccountCredential)
            .setApplicationName("LabCollect")
            .build()
    }

    override suspend fun createSpreadsheet(title: String): String? {
        val sheet = Sheet().setProperties(SheetProperties().setTitle("Data"))
        var spreadSheet = Spreadsheet().setProperties(SpreadsheetProperties().setTitle(title))
        spreadSheet.sheets = listOf(sheet)
        try {
            spreadSheet = getSpreadService().spreadsheets().create(spreadSheet).execute()
            Timber.e("Spreadsheet ID: ${spreadSheet.spreadsheetId}, uri: ${spreadSheet.spreadsheetUrl}")
        } catch (e: GoogleJsonResponseException) {
            Timber.e("Can not create spread: $e")
            e.printStackTrace()
        }
        return spreadSheet.spreadsheetId
    }
/*
Some API methods require a range in A1 notation.
This is a string like Sheet1!A1:B2, that refers to a group of cells in the spreadsheet, and is typically used in formulas.
For example, valid ranges are:
    TODO --------------- Sheet1!A1:B2 refers to the first two cells in the top two rows of Sheet1.
    TODO --------------- Sheet1!A:A refers to all the cells in the first column of Sheet1.
    TODO --------------- Sheet1!1:2 refers to the all the cells in the first two rows of Sheet1.
    TODO --------------- Sheet1!A5:A refers to all the cells of the first column of Sheet 1, from row 5 onward.
    TODO --------------- A1:B2 refers to the first two cells in the top two rows of the first visible sheet.
    TODO --------------- Sheet1 refers to all the cells in Sheet1.*/
    override suspend fun readSpreadSheet(spreadSheetId: String, tabName: String?, range: String): ValueRange? {
        val spreadRange = tabName?.let { if (range == "") "$tabName" else "$tabName!$range" } ?: range
        var result: ValueRange? = null
        try {
            // Gets the values of the cells in the specified range.
            result = getSpreadService().spreadsheets().values().get(spreadSheetId, spreadRange).execute()
            val numRows = if (result.getValues() != null) result.getValues().size else 0
            Timber.e("$numRows rows retrieved.", numRows)
        } catch (e: GoogleJsonResponseException) {
            val error = e.details
            if (error.code == 404) {
                Timber.e("Spreadsheet not found with id '%s'.\n", spreadSheetId)
            } else {
                throw e
            }
        }
        return result
    }

    override suspend fun getFilesAtRoot(): List<Triple<String, String, List<String>>> {
        val list = mutableListOf<Triple<String, String, List<String>>>()
        try {
            getDriveService().let { googleDriveService ->
                var pageToken: String?
                do {
                    val result = googleDriveService.files().list().apply {
                        spaces = "drive"
                        q = "'$ROOT_DIR_ID' in parents and trashed = false and not name contains '@readme@'"
                        fields = "nextPageToken, files(id, name, parents)"
                        pageToken = this.pageToken
                    }.execute()
                    Timber.e("Result: ${result}, files size: ${result.files.size}")
                    for (file in result.files) {
//                        Timber.e("${file.name}, ${file.id}, ${file.parents}")
                        list.add(Triple(file.name, file.id, file.parents))
                    }
                } while (pageToken != null)
            }
        } catch (e: GoogleJsonResponseException) {
            Timber.e("Get all file error: $e")
            e.printStackTrace()
        }
        return list
    }

    override suspend fun getFilesAtDir(dirId: String): List<Triple<String, String, List<String>>> {
        val list = mutableListOf<Triple<String, String, List<String>>>()
        try {
            getDriveService().let { googleDriveService ->
                var pageToken: String?
                do {
                    val result = googleDriveService.files().list().apply {
                        spaces = "drive"
                        q = "'$dirId' in parents and trashed = false"
                        fields = "nextPageToken, files(id, name, parents)"
                        pageToken = this.pageToken
                    }.execute()
                    for (file in result.files) {
//                        Timber.e("${file.name}, ${file.id}, ${file.parents}")
                        list.add(Triple(file.name, file.id, file.parents))
                    }
                } while (pageToken != null)
            }
        } catch (e: GoogleJsonResponseException) {
            Timber.e("Get all file error: $e")
            e.printStackTrace()
        }
        return list
    }

    override suspend fun moveFileToDir(fileId: String, dirId: String): List<String> {    //https://github.com/googleworkspace/java-samples/blob/master/drive/snippets/drive_v3/src/main/java/MoveFileToFolder.java
        try {
            val file = getDriveService().files().get(fileId).setFields(PARENT_FIELD_KEY).execute()
            val previousParents = StringBuilder()
            for (parent in file.parents) previousParents.append("$parent,")
            val update = getDriveService().files().update(fileId, null)
                .setRemoveParents(previousParents.toString())
                .setAddParents(dirId)
                .setSupportsAllDrives(true)
                .setFields("$ID_KEY, $PARENT_FIELD_KEY")
                .execute()
            return update.parents
        } catch (e: GoogleJsonResponseException) {
            Timber.e("Unable to move file: $e")
            e.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun createSpreadAtDir(title: String, dirId: String): Pair<Boolean, String> {
        createSpreadsheet(title)?.let {
            moveFileToDir(it, dirId)
            return  Pair(true, it)
        }
        return Pair(false, "")
    }

    fun updateSpread(spreadId: String, range: String, inputOption: String, rowData: List<Any>): UpdateValuesResponse? {
        var result: UpdateValuesResponse? = null
        val colList = mutableListOf<String>()
        rowData.forEach { colList.add((it as Field).key) }
        val list = mutableListOf<List<Any>>()

        list.add(colList)
        try {
            // Updates the values in the specified range.
            val body: ValueRange = ValueRange().setValues(list)
            result = getSpreadService().spreadsheets().values().update(spreadId, range, body)
                .setValueInputOption(inputOption)
                .execute()
            Timber.e("%d cells updated: ${result.updatedCells}")
        } catch (e: GoogleJsonResponseException) {
            val error = e.details
            if (error.code == 404) {
                System.out.printf("Spreadsheet not found with id '%s'.\n", spreadId)
            } else {
                throw e
            }
        }
        return result
    }

    fun updateSheetInformation(spreadId: String, data: Any): BatchUpdateSpreadsheetResponse? {
        var result: BatchUpdateSpreadsheetResponse? = null
        try {
            // Updates the values in the specified range.
            val request = Request().setAddSheet(AddSheetRequest().setProperties(SheetProperties().setTitle("Information")))
            val body = BatchUpdateSpreadsheetRequest().setRequests(listOf(request))
            result = getSpreadService().spreadsheets().batchUpdate(spreadId, body).execute()

            val informationData: ValueRange = ValueRange().setValues(listOf(listOf(Gson().toJson(data))))
            getSpreadService().spreadsheets().values()
                .update(spreadId, "Information!A1:Z20", informationData)
                .setValueInputOption("RAW")
                .execute()
        } catch (e: GoogleJsonResponseException) {
            val error = e.details
            if (error.code == 404) {
                Timber.e("Spreadsheet not found with id '%s'.\n", spreadId)
            } else {
                throw e
            }
        }
        return result
    }

    suspend fun getTestInfoFromSpread(spreadId: String): Test? {
        try {
            val result = readSpreadSheet(spreadId, "Information","A1")
            Timber.e("Test from server: ${result?.getValues()?.get(0)?.get(0)}")
            val test = Gson().fromJson(result?.getValues()?.get(0)?.get(0).toString(), Test::class.java)
            test.spreadId = spreadId
            return test
        } catch (e: Exception) { Timber.e("Error when get test from server: $e") }
        return null
    }
}
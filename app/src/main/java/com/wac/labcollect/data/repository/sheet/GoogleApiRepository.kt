package com.wac.labcollect.data.repository.sheet

import android.accounts.Account
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import com.wac.labcollect.data.manager.AuthenticationManager
import com.wac.labcollect.data.repository.sheet.GoogleApiConstant.ID_KEY
import com.wac.labcollect.data.repository.sheet.GoogleApiConstant.PARENT_FIELD_KEY
import com.wac.labcollect.data.repository.sheet.GoogleApiConstant.ROOT_DIR_ID
import com.wac.labcollect.domain.models.Field
import timber.log.Timber


class GoogleApiRepository(
    private val authManager: AuthenticationManager, private val transport: HttpTransport,
    private val jsonFactory: JsonFactory, private val lastSignedAccount: Account?
) : GoogleApiDataSource {

    private fun getSpreadService(): Sheets {
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
        var spreadSheet = Spreadsheet().setProperties(SpreadsheetProperties().setTitle(title))
        try {
            spreadSheet = getSpreadService().spreadsheets().create(spreadSheet).execute()
            Timber.e("Spreadsheet ID: ${spreadSheet.spreadsheetId}, uri: ${spreadSheet.spreadsheetUrl}")
        } catch (e: GoogleJsonResponseException) {
            Timber.e("Can not create spread: $e")
            e.printStackTrace()
        }
        return spreadSheet.spreadsheetId
    }

    override suspend fun readSpreadSheet(spreadSheetId: String, spreadSheetRange: String) {}

    override suspend fun getAllFiles(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        try {
            getDriveService().let { googleDriveService ->
                var pageToken: String?
                do {
                    val result = googleDriveService.files().list().apply {
                        spaces = "drive"
                        q = "'$ROOT_DIR_ID' in parents and trashed = false"
                        fields = "nextPageToken, files(id, name)"
                        pageToken = this.pageToken
                    }.execute()
                    for (file in result.files) {
                        list.add(Pair(file.name, file.id))
                    }
                } while (pageToken != null)
            }
        } catch (e: GoogleJsonResponseException) {
            Timber.e("Get all file error: $e")
            e.printStackTrace()
        }
        return list
    }
    //https://github.com/googleworkspace/java-samples/blob/master/drive/snippets/drive_v3/src/main/java/MoveFileToFolder.java
    override suspend fun moveFileToDir(fileId: String, dirId: String): List<String> {
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

    fun updateSpread(spreadId: String, range: String, inputOption: String, fields: List<Any>): UpdateValuesResponse? {
        var result: UpdateValuesResponse? = null
        val colList = mutableListOf<String>()
        fields.forEach { colList.add((it as Field).key) }
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

}
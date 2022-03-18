package com.wac.labcollect.data.repository.sheet

import android.accounts.Account
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.wac.labcollect.data.manager.AuthenticationManager
import com.wac.labcollect.data.repository.sheet.GoogleApiConstant.ID_KEY
import com.wac.labcollect.data.repository.sheet.GoogleApiConstant.PARENT_FIELD_KEY
import timber.log.Timber
import java.io.IOException
import java.lang.StringBuilder

class GoogleApiRepository(
    private val authManager: AuthenticationManager, private val transport: HttpTransport,
    private val jsonFactory: JsonFactory, private val lastSignedAccount: Account?
) : GoogleApiDataSource {

    private fun getSheetService(): Sheets {
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

    override suspend fun createSpreadsheet(spreadSheet: Spreadsheet): String? {
        val response = getSheetService().spreadsheets().create(spreadSheet).execute()
        Timber.e("Spreadsheet ID: ${response.spreadsheetId}, uri: ${response.spreadsheetUrl}")
        return response.spreadsheetId
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
                        fields = "nextPageToken, files(id, name)"
                        pageToken = this.pageToken
                    }.execute()
                    for (file in result.files) {
                        list.add(Pair(file.name, file.id))
                    }
                } while (pageToken != null)
            }
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            Timber.e("Unable to move file: $e")
            e.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun createSpreadAtDir(spreadSheetId: String, DirId: String): Boolean {
        TODO("Not yet implemented")
    }

}
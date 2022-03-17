package com.wac.labcollect.data.repository.sheet

import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.wac.labcollect.data.manager.AuthenticationManager

class SheetsAPIDataSource(private val authManager : AuthenticationManager,
                          private val transport : HttpTransport,
                          private val jsonFactory: JsonFactory) : SheetsDataSource {

    private val sheetsAPI : Sheets
        get() {
            return Sheets.Builder(transport, jsonFactory, authManager.googleAccountCredential)
                    .setApplicationName("LabCollect")
                    .build()
        }

    override fun readSpreadSheet(spreadsheetId: String, spreadsheetRange: String) {}

    override fun createSpreadsheet(spreadSheet : Spreadsheet) {}

    companion object {
        val KEY_ID = "spreadsheetId"
        val KEY_URL = "spreadsheetUrl"
    }
}
package com.wac.labcollect.data.repository.sheet

import com.google.api.services.sheets.v4.model.Spreadsheet

interface SheetsDataSource {

    fun readSpreadSheet(spreadsheetId : String, spreadsheetRange : String)

    fun createSpreadsheet(spreadSheet : Spreadsheet)
}
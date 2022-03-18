package com.wac.labcollect.data.manager

import android.accounts.Account
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.sheets.v4.SheetsScopes

class AuthenticationManager(val googleSignInClient : GoogleSignInClient? =null,
                            val googleAccountCredential : GoogleAccountCredential?) {

    fun setUpGoogleAccountCredential(account: Account?) {
        googleAccountCredential?.selectedAccount = account
    }

    companion object {
        val SCOPES = listOf(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE)
    }

}
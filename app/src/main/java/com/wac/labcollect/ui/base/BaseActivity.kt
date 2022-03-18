package com.wac.labcollect.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.wac.labcollect.MainApplication


abstract class BaseActivity : AppCompatActivity() {

    private val app get() = applicationContext as MainApplication

    fun currentTokenId() = this.let { GoogleSignIn.getLastSignedInAccount(it)?.idToken }

    fun lastSignedAccount(): GoogleSignInAccount? = this.let { GoogleSignIn.getLastSignedInAccount(it) }

    fun setUpGoogleAccountCredential() {
        (this.application as MainApplication).authManager.setUpGoogleAccountCredential(lastSignedAccount()?.account)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setUpGoogleAccountCredential()
    }

}
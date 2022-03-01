package com.wac.labcollect.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.wac.labcollect.App


abstract class BaseActivity : AppCompatActivity() {

    private val app get() = applicationContext as App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

}
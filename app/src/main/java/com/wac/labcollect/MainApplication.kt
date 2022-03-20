package com.wac.labcollect

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.wac.labcollect.data.database.TemplateDB
import com.wac.labcollect.data.manager.AuthenticationManager
import com.wac.labcollect.data.network.TemplateApiService
import com.wac.labcollect.data.repository.sheet.GoogleApiRepository
import com.wac.labcollect.data.repository.test.TestRepository
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class MainApplication : Application() {

    private val templateDb by lazy {
        TemplateDB.getDatabase(this)
    }

    private val apiService by lazy {
        TemplateApiService().apiService
    }

    val testRepository by lazy {
        TestRepository(templateDb.templateDao(), apiService)
    }

    val lastSignInAccount : GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(this)

    val authManager by lazy {
        val credentials = GoogleAccountCredential.usingOAuth2(this, AuthenticationManager.SCOPES)
        AuthenticationManager(null, credentials)
    }

    val googleApiRepository: GoogleApiRepository
        get() = GoogleApiRepository(authManager,  NetHttpTransport(), GsonFactory.getDefaultInstance(), lastSignInAccount?.account)

    override fun onCreate() {
        super.onCreate()
//        setupStrictMode()
        setupTimber()
    }

    private fun setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
//                    .penaltyDeath()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
//                    .penaltyDeath()
                    .build()
            )
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
    
}
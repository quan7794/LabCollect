package com.wac.labcollect

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.wac.labcollect.data.database.TemplateDB
import com.wac.labcollect.data.network.TemplateApiService
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

    val repository by lazy {
        TestRepository(templateDb.templateDao(), apiService)
    }

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
package com.wac.labcollect.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.wac.labcollect.MainApplication


abstract class BaseActivity : AppCompatActivity() {

    private val app get() = applicationContext as MainApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

}
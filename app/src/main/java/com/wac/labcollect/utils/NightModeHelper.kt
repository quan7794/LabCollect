package com.wac.labcollect.utils

import androidx.appcompat.app.AppCompatDelegate

object NightModeHelper {

    @JvmStatic
    fun handleSetNightMode(value: Int) {
        when (value) {
            DarkModeType.TURN_ON -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            DarkModeType.TURN_OFF -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            DarkModeType.SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            else -> {

            }
        }
    }
}

object DarkModeType {
    const val TURN_ON = 1
    const val TURN_OFF = 2
    const val SYSTEM = 3
}
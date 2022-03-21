package com.wac.labcollect.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.*
import com.wac.labcollect.*
import com.wac.labcollect.utils.Utils.fromJson


object PreferenceUtil {
    /**
     * @param value : 1 - on , 2 - off, 3 - system
     * */

    @JvmStatic
    fun setDarkModeSetting(context: Context, value: Int) {
        val darkModeConfig =
            context.getSharedPreferences(DARK_MODE_CONFIG, AppCompatActivity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = darkModeConfig.edit()
        editor.putInt(DARK_MODE_TYPE, value)
        editor.apply()
    }

    @JvmStatic
    fun getDarkModeSetting(context: Context): Int {
        val darkModeConfig =
            context.getSharedPreferences(DARK_MODE_CONFIG, AppCompatActivity.MODE_PRIVATE)
        return darkModeConfig.getInt(DARK_MODE_TYPE, 3)
    }
}
package com.wac.labcollect.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.*
import com.wac.labcollect.*
import com.wac.labcollect.utils.Utils.Companion.fromJson


object PreferenceUtil {

    @JvmStatic
    fun getSubmitData(context: Context): HashMap<String, String> {
        return Gson().fromJson(getSystemPref(context, FORM_DATA))
    }

    @JvmStatic
    fun saveSubmitData(context: Context, formData: String) {
        setSystemPref(context, FORM_DATA, formData)
    }

    fun saveUserConfig(
        context: Context,
        genID: String = "",
        name: String = "",
        phone: String = "",
        q4yn: String
    ) {

        setUserPref(context, GEN_ID, genID)
        setUserPref(context, FULL_NAME, name)
        setUserPref(context, PHONE, phone)
        setUserPref(context, Q4YN, q4yn)
    }

    fun getGenId(context: Context): String {
        return getUserPref(context, GEN_ID)
    }

    fun getFullName(context: Context): String {
        return getUserPref(context, FULL_NAME)
    }

    fun getPhone(context: Context): String {
        return getUserPref(context, PHONE)
    }

    fun getQ4yn(context: Context): String {
        return getUserPref(context, Q4YN)
    }

    fun getSubmitHost(context: Context): String {
        return getSystemPref(context, SUBMIT_HOST)
    }


    //Util region
    @JvmStatic
    fun getSystemPref(context: Context, prefKey: String): String {
        val systemConfig =
            context.getSharedPreferences(SYSTEM_CONFIG, AppCompatActivity.MODE_PRIVATE)
        return systemConfig.getString(prefKey, UNKNOWN) ?: UNKNOWN
    }

    @JvmStatic
    fun setSystemPref(context: Context, key: String, value: String) {
        val systemConfig =
            context.getSharedPreferences(SYSTEM_CONFIG, AppCompatActivity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = systemConfig.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getUserPref(context: Context, prefKey: String): String {
        val userConfig = context.getSharedPreferences(USER_CONFIG, AppCompatActivity.MODE_PRIVATE)
        return userConfig.getString(prefKey, UNKNOWN) ?: UNKNOWN
    }

    @JvmStatic
    fun setUserPref(context: Context, key: String, value: String) {
        val userConfig = context.getSharedPreferences(USER_CONFIG, AppCompatActivity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = userConfig.edit()
        editor.putString(key, value)
        editor.apply()
    }

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
package com.wac.labcollect.ui.fragment.firstScreen.profileTab

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wac.labcollect.BuildConfig
import com.wac.labcollect.utils.PreferenceUtil

class ProfileViewModel : ViewModel() {

    var currentTypeSetting = MutableLiveData<Int>()

    suspend fun getDarkModeSetting(context: Context) {
        PreferenceUtil.getDarkModeSetting(context).let { type ->
            currentTypeSetting.value = type
        }
    }

    suspend fun setDarkModeSetting(context: Context, value: Int) {
        PreferenceUtil.setDarkModeSetting(context, value)
    }
    fun getCurrentAppVersion() : String {
        return BuildConfig.VERSION_NAME
    }
}

object DarkModeType {
    const val TURN_ON = 1
    const val TURN_OFF = 2
    const val SYSTEM = 3
}
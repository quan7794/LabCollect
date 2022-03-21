package com.wac.labcollect.ui.fragment.firstScreen.profileTab

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wac.labcollect.BuildConfig
import com.wac.labcollect.utils.PreferenceUtil

class ProfileViewModel : ViewModel() {

    private var _currentTypeSetting = MutableLiveData<Int>()
    val currentTypeSetting :LiveData<Int>
        get() = _currentTypeSetting

    fun getDarkModeSetting(context: Context) {
        PreferenceUtil.getDarkModeSetting(context).let { type ->
            _currentTypeSetting.value = type
        }
    }

    fun setDarkModeSetting(context: Context, value: Int) {
        PreferenceUtil.setDarkModeSetting(context, value)
    }

    fun getCurrentAppVersion() : String {
        return BuildConfig.VERSION_NAME
    }
}
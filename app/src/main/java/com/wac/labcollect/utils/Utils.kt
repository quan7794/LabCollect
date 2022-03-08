package com.wac.labcollect.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
        observeForever(object : Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
        observe(owner, object : Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

    inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, object : TypeToken<T>() {}.type)

    inline fun <reified T : Enum<T>> String.toEnumOrDefault(defaultValue: T? = null): T? =
        enumValues<T>().firstOrNull { it.name.equals(this, ignoreCase = true) } ?: defaultValue

    fun isInternet(context: Context): Boolean {
        val mConMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return (mConMgr.activeNetworkInfo != null && mConMgr.activeNetworkInfo!!.isAvailable
                && mConMgr.activeNetworkInfo!!.isConnected)
    }

    fun getTime(hr: Int, min: Int): String? {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hr)
        cal.set(Calendar.MINUTE, min)
        val formatter = SimpleDateFormat("h:mm a", Locale.ROOT)
        return formatter.format(cal.time)
    }

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    data class Resource<out T>(val status: Status, val data: T? = null, val message: String? = null) {
        companion object {
            fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data)
            fun <T> error(data: T? = null, message: String) = Resource(Status.ERROR, data, message)
            fun <T> loading(data: T? = null) = Resource(Status.LOADING, data)
        }
    }

}
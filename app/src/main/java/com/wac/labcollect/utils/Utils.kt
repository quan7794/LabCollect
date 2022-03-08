package com.wac.labcollect.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wac.labcollect.DECLARE_FAIL
import com.wac.labcollect.DECLARE_SUCCESS
import java.security.KeyManagementException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


class Utils {
    companion object {
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

        fun String.isDeclareSuccess(): Boolean {
            return this.contains(DECLARE_SUCCESS) && !this.contains(DECLARE_FAIL)
        }

        inline fun <reified T : Enum<T>> String.toEnumOrDefault(defaultValue: T? = null): T? =
            enumValues<T>().firstOrNull { it.name.equals(this, ignoreCase = true) } ?: defaultValue

        fun isInternet(context: Context): Boolean {
            val mConMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return (mConMgr.activeNetworkInfo != null && mConMgr.activeNetworkInfo!!.isAvailable
                    && mConMgr.activeNetworkInfo!!.isConnected)
        }

        @SuppressLint("BatteryLife")
        fun Context.requestIgnoreBatteryOptimization() {
            val intent = Intent()
            val packageName = this.packageName
            val pm = this.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName))
//            intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS else
            {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                this.startActivity(intent)
            }
//        this.startActivity(intent)
        }

        fun getTime(hr: Int, min: Int): String? {
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, hr)
            cal.set(Calendar.MINUTE, min)
            val formatter = SimpleDateFormat("h:mm a", Locale.ROOT)
            return formatter.format(cal.time)
        }


        fun socketFactory(): SSLSocketFactory {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            try {
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                return sslContext.socketFactory
            } catch (e: Exception) {
                when (e) {
                    is RuntimeException, is KeyManagementException -> {
                        throw RuntimeException("Failed to create a SSL socket factory", e)
                    }
                    else -> throw e
                }
            }
        }
    }
}
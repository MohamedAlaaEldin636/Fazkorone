package com.grand.ezkorone.core

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import es.dmoral.toasty.Toasty
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {

    private var toast: Toast? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(LineNumberDebugTree())

        setupToasting()
    }

    private fun setupToasting() {
        Toasty.Config.getInstance().allowQueue(false).apply()
    }

    fun showSuccessToast(msg: String) = showToast(Toasty.success(this, msg, Toast.LENGTH_SHORT, true))

    fun showErrorToast(msg: String) = showToast(Toasty.error(this, msg, Toast.LENGTH_SHORT, true))

    fun showNormalToast(msg: String) = showToast(Toasty.normal(this, msg, Toast.LENGTH_SHORT, null, false))

    private fun showToast(toast: Toast) {
        this.toast?.cancel()
        this.toast = toast
        this.toast?.show()
    }

    class LineNumberDebugTree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
            return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
        }
    }

}

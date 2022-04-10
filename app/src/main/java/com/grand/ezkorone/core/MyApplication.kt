package com.grand.ezkorone.core

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDex
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import es.dmoral.toasty.Toasty
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    private var toast: Toast? = null

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

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

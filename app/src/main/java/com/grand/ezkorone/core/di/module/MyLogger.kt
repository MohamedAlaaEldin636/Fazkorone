package com.grand.ezkorone.core.di.module

import android.util.Log

object MyLogger {

    @JvmStatic
    fun e(any: Any?) {
        Log.e("MyLogger", "MyLogger -> $any")
    }

    @JvmStatic
    fun d(any: Any?) {
        Log.d("MyLogger", "MyLogger -> $any")
    }

    @JvmStatic
    fun v(any: Any?) {
        Log.v("MyLogger", "MyLogger -> $any")
    }

    @JvmStatic
    fun i(any: Any?) {
        Log.i("MyLogger", "MyLogger -> $any")
    }

    @JvmStatic
    fun w(any: Any?) {
        Log.w("MyLogger", "MyLogger -> $any")
    }

}

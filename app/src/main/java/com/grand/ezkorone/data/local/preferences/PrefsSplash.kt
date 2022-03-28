package com.grand.ezkorone.data.local.preferences

import android.content.Context
import com.google.gson.Gson
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsSplash @Inject constructor(
    @ApplicationContext context: Context,
    gson: Gson,
) : PrefsBase(context, gson, "PREFS_SPLASH") {

    companion object {
        private const val KEY_INITIAL_LAUNCH = "KEY_INITIAL_LAUNCH"
    }

    suspend fun setInitialLaunch(splashInitialLaunch: SplashInitialLaunch) =
        setValue(KEY_INITIAL_LAUNCH, splashInitialLaunch)

    fun getInitialLaunch() = getValue<SplashInitialLaunch?>(KEY_INITIAL_LAUNCH)

}

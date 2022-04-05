package com.grand.ezkorone.data.local.preferences

import android.content.Context
import com.google.gson.Gson
import com.grand.ezkorone.domain.alarms.TimeInDay
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import com.grand.ezkorone.domain.utils.common.InitialDataForApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsAlarms @Inject constructor(
    @ApplicationContext context: Context,
    gson: Gson,
) : PrefsBase(context, gson, "PREFS_ALARMS") {

    companion object {
        private const val KEY_DRAWER_ALARMS_AZKAR_SABAH = "KEY_DRAWER_ALARMS_AZKAR_SABAH"
        private const val KEY_DRAWER_ALARMS_AZKAR_MASAA = "KEY_DRAWER_ALARMS_AZKAR_MASAA"
        private const val KEY_DRAWER_ALARMS_TASPEH = "KEY_DRAWER_ALARMS_TASPEH"
    }

    suspend fun setDrawerAlarmAzkarSabah(timeInDay: TimeInDay) =
        setValue(KEY_DRAWER_ALARMS_AZKAR_SABAH, timeInDay)
    fun getDrawerAlarmAzkarSabah() = getValue<TimeInDay?>(KEY_DRAWER_ALARMS_AZKAR_SABAH)

    suspend fun setDrawerAlarmAzkarMasaa(timeInDay: TimeInDay) =
        setValue(KEY_DRAWER_ALARMS_AZKAR_MASAA, timeInDay)
    fun getDrawerAlarmAzkarMasaa() = getValue<TimeInDay?>(KEY_DRAWER_ALARMS_AZKAR_MASAA)

    suspend fun setDrawerAlarmTaspeh(timeInDay: TimeInDay) =
        setValue(KEY_DRAWER_ALARMS_TASPEH, timeInDay)
    fun getDrawerAlarmTaspeh() = getValue<TimeInDay?>(KEY_DRAWER_ALARMS_TASPEH)

}

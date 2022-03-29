package com.grand.ezkorone.data.local.preferences

import android.content.Context
import com.google.gson.Gson
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsApp @Inject constructor(
    @ApplicationContext context: Context,
    gson: Gson,
) : PrefsBase(context, gson, "PREFS_APP") {

    companion object {
        private const val KEY_LOCATION_DATA = "KEY_LOCATION_DATA"
        private const val KEY_NOTIFICATIONS_STATUS = "KEY_NOTIFICATIONS_STATUS"
        private const val KEY_FIREBASE_TOKEN = "KEY_FIREBASE_TOKEN"
    }

    suspend fun setLocationData(locationData: LocationData?) =
        setValue(KEY_LOCATION_DATA, locationData)

    fun getLocationData() = getValue<LocationData?>(KEY_LOCATION_DATA)

    suspend fun setNotificationStatus(enable: Boolean?) =
        setBooleanValue(KEY_NOTIFICATIONS_STATUS, enable)

    fun isNotificationsEnabled() = getBooleanValue(KEY_NOTIFICATIONS_STATUS)

    suspend fun setFirebaseToken(token: String?) =
        setStringValue(KEY_FIREBASE_TOKEN, token)

    fun getFirebaseToken() = getStringValue(KEY_FIREBASE_TOKEN)

}

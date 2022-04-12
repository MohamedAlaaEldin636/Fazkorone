package com.grand.ezkorone.data.local.preferences

import android.content.Context
import com.google.gson.Gson
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
        private const val KEY_LOCATIONS_LIST = "KEY_LOCATIONS_LIST"
    }

    suspend fun setLocationData(locationData: LocationData?) {
        addInLocationsList(locationData)

        setValue(KEY_LOCATION_DATA, locationData)
    }

    fun getLocationData() = getValue<LocationData?>(KEY_LOCATION_DATA)

    suspend fun setNotificationStatus(enable: Boolean?) =
        setBooleanValue(KEY_NOTIFICATIONS_STATUS, enable)

    fun isNotificationsEnabled() = getBooleanValue(KEY_NOTIFICATIONS_STATUS).map { it ?: false }

    suspend fun setFirebaseToken(token: String?) =
        setStringValue(KEY_FIREBASE_TOKEN, token)

    fun getFirebaseToken() = getStringValue(KEY_FIREBASE_TOKEN)

    private suspend fun addInLocationsList(locationData: LocationData?) {
        if (locationData == null) {
            return
        }

        val list = getLocationsList().first().toMutableList()
        if (!list.specialContains(locationData)) {
            list.add(0, locationData)
            setLocationsList(list.let {
                if (it.size > 100) it.dropLast(1) else it
            })
        }
    }

    private fun List<LocationData>.specialContains(locationData: LocationData?): Boolean {
        if (locationData == null) return false

        for (item in this) {
            if (item.address == locationData.address ||
                (item.latitude == locationData.latitude && item.longitude == locationData.longitude)) {
                return true
            }
        }

        return false
    }

    private suspend fun setLocationsList(locations: List<LocationData>) =
        setValue(KEY_LOCATIONS_LIST, locations)

    fun getLocationsList() = getValue<List<LocationData>>(KEY_LOCATIONS_LIST).map { it.orEmpty() }

}

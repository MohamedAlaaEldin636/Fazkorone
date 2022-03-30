package com.grand.ezkorone.data.notifications.dataSource.remote

import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.local.preferences.PrefsSplash
import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import com.grand.ezkorone.data.settings.dataSource.remote.ApiSettingsServices
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataSourceNotifications @Inject constructor(
    private val apiService: ApiNotificationsServices,
    private val prefsApp: PrefsApp
) : MABaseRemoteDataSource() {

    suspend fun enableOrDisableNotifications(enable: Boolean) = safeApiCall {
        val status = if (enable) 1 else 2

        apiService.enableOrDisableNotifications(prefsApp.getFirebaseToken().first()!!, status)
    }

    suspend fun registerDevice() = safeApiCall {
        apiService.enableOrDisableNotifications(prefsApp.getFirebaseToken().first()!!, 0)
    }

}

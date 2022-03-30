package com.grand.ezkorone.data.azan.repository

import com.grand.ezkorone.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.grand.ezkorone.data.azan.dataSource.remote.DataSourceAzan
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.notifications.dataSource.remote.DataSourceNotifications
import com.grand.ezkorone.domain.azan.ResponseAzan
import com.grand.ezkorone.domain.azan.TodayAndTomorrowSalawatTimes
import com.grand.ezkorone.domain.utils.MABaseResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RepositoryAzan @Inject constructor(
    private val dataSource: DataSourceAzan,
    private val prefsApp: PrefsApp,
) {

    fun getAzanTimes() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseAzan>> {
        val locationData = prefsApp.getLocationData().first()!!

        emit(dataSource.getAzanTimes(locationData.latitude, locationData.longitude))
    }

    fun getAzanTimesTodayAndTomorrow() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<TodayAndTomorrowSalawatTimes>> {
        val locationData = prefsApp.getLocationData().first()!!

        emit(dataSource.getAzanTimesTodayAndTomorrow(locationData.latitude, locationData.longitude))
    }

}

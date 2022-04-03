package com.grand.ezkorone.data.azan.repository

import com.grand.ezkorone.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.grand.ezkorone.data.azan.dataSource.remote.DataSourceAzan
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.notifications.dataSource.remote.DataSourceNotifications
import com.grand.ezkorone.domain.azan.ResponseAzan
import com.grand.ezkorone.domain.azan.TodayAndTomorrowSalawatTimes
import com.grand.ezkorone.domain.utils.MABaseResponse
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class RepositoryAzan @Inject constructor(
    private val dataSource: DataSourceAzan,
    private val prefsApp: PrefsApp,
) {

    fun getAzanTimes(localDate: LocalDate) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseAzan>> {
        val locationData = prefsApp.getLocationData().first()!!

        emit(dataSource.getAzanTimes(localDate, locationData.latitude, locationData.longitude))
    }

    suspend fun getAzanTimesSuspend(localDate: LocalDate) = prefsApp.getLocationData().first()!!.let { locationData ->
        dataSource.getAzanTimes(localDate, locationData.latitude, locationData.longitude)
    }

    fun getAzanTimesTodayAndTomorrow() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<TodayAndTomorrowSalawatTimes>> {
        val locationData = prefsApp.getLocationData().first()!!

        emit(dataSource.getAzanTimesTodayAndTomorrow(locationData.latitude, locationData.longitude))
    }

}

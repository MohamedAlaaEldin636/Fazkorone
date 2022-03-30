package com.grand.ezkorone.data.azan.dataSource.remote

import com.grand.ezkorone.core.extensions.minLengthOrPrefixZeros
import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import com.grand.ezkorone.domain.azan.TodayAndTomorrowSalawatTimes
import com.grand.ezkorone.domain.utils.MABaseResponse
import java.time.LocalDate
import javax.inject.Inject

class DataSourceAzan @Inject constructor(
    private val apiService: ApiAzanServices,
) : MABaseRemoteDataSource() {

    suspend fun getAzanTimes(latitude: String, longitude: String) = safeApiCall {
        val dayMonthYearFormatted = LocalDate.now().let {
            "${it.dayOfMonth.minLengthOrPrefixZeros(2)}-${it.monthValue.minLengthOrPrefixZeros(2)}-${it.year}"
        }

        apiService.getAzanTimes(dayMonthYearFormatted, latitude, longitude).toMABaseResponse()
    }

    suspend fun getAzanTimesTodayAndTomorrow(latitude: String, longitude: String) = safeApiCall {
        val dayMonthYearFormatted = LocalDate.now().let {
            "${it.dayOfMonth.minLengthOrPrefixZeros(2)}-${it.monthValue.minLengthOrPrefixZeros(2)}-${it.year}"
        }
        val tomorrowDayMonthYearFormatted = LocalDate.now().plusDays(1).let {
            "${it.dayOfMonth.minLengthOrPrefixZeros(2)}-${it.monthValue.minLengthOrPrefixZeros(2)}-${it.year}"
        }

        val today = apiService.getAzanTimes(dayMonthYearFormatted, latitude, longitude).toMABaseResponse()
        val tomorrow = apiService.getAzanTimes(tomorrowDayMonthYearFormatted, latitude, longitude).toMABaseResponse()

        today.mapData {
            TodayAndTomorrowSalawatTimes(today.data?.timings!!, tomorrow.data?.timings!!)
        }
    }

}

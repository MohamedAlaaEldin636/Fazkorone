package com.grand.ezkorone.data.azan.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.aboutUs.ItemAboutUs
import com.grand.ezkorone.domain.azan.ResponseAzan
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.MABaseResponseAladan
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiAzanServices {

    //https://api.aladhan.com/v1/timings/29-03-2022?latitude=30.126011848847234&longitude=31.37534849345684&method=5
    /**
     * @param dayMonthYearFormatted 29-03-2022
     */
    @GET("timings/{${ApiConst.Path.DD_MM_YYYY}}?method=5")
    suspend fun getAzanTimes(
        @Path(ApiConst.Path.DD_MM_YYYY) dayMonthYearFormatted: String,
        @Query(ApiConst.Query.LATITUDE) latitude: String,
        @Query(ApiConst.Query.LONGITUDE) longitude: String,
    ): MABaseResponseAladan<ResponseAzan>

}

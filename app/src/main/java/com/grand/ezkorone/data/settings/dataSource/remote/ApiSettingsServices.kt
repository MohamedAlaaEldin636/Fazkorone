package com.grand.ezkorone.data.settings.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.aboutUs.ItemAboutUs
import com.grand.ezkorone.domain.utils.MABaseResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiSettingsServices {

    @GET("settings?type=1")
    suspend fun getInitialAppRequirementsAndSetFirebaseToken(
        @Query(ApiConst.Query.FIREBASE_TOKEN) firebaseToken: String,
    ): MABaseResponse<List<ItemAboutUs>>

}
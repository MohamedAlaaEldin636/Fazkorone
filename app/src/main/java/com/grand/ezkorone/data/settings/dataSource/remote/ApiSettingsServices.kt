package com.grand.ezkorone.data.settings.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.aboutUs.ItemAboutUs
import com.grand.ezkorone.domain.contactUs.ItemContactUsPurpose
import com.grand.ezkorone.domain.utils.MABaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiSettingsServices {

    @GET("settings?type=1")
    suspend fun getInitialAppRequirementsAndSetFirebaseToken(
        @Query(ApiConst.Query.FIREBASE_TOKEN) firebaseToken: String,
    ): MABaseResponse<List<ItemAboutUs>>

    @GET("settings?type=2")
    suspend fun getContactUsPurposes(): MABaseResponse<List<ItemContactUsPurpose>>

    @Multipart
    @POST("contactUs/add")
    suspend fun contactUs(
        @Part image: MultipartBody.Part,
        @Part(ApiConst.Query.NAME) name: RequestBody,
        @Part(ApiConst.Query.EMAIL) email: RequestBody,
        @Part(ApiConst.Query.COMMUNICATION_ID) purposeId: Int,
        @Part(ApiConst.Query.MESSAGE) message: RequestBody,
    ): MABaseResponse<Any>
    @Multipart
    @POST("contactUs/add")
    suspend fun contactUs(
        @Part(ApiConst.Query.NAME) name: RequestBody,
        @Part(ApiConst.Query.EMAIL) email: RequestBody,
        @Part(ApiConst.Query.COMMUNICATION_ID) purposeId: Int,
        @Part(ApiConst.Query.MESSAGE) message: RequestBody,
    ): MABaseResponse<Any>

}
package com.grand.ezkorone.data.notifications.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.utils.MABaseResponse
import retrofit2.http.*

interface ApiNotificationsServices {

    @FormUrlEncoded
    @POST("set-notification-flag")
    suspend fun enableOrDisableNotifications(
        @Field(ApiConst.Query.FIREBASE_TOKEN) firebaseToken: String,
        @Field(ApiConst.Query.STATUS) status: Int,
    ): MABaseResponse<Any>

}

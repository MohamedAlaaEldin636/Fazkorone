package com.grand.ezkorone.data.favorite.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.utils.MABaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiFavoriteServices {

    @FormUrlEncoded
    @POST("favorite/add_or_remove")
    suspend fun toggleFavoriteForVerticalList(
        @Field(ApiConst.Query.CATEGORY_ID) id: Int
    ): MABaseResponse<Any>

    @FormUrlEncoded
    @POST("favorite/add_or_remove")
    suspend fun toggleFavoriteForHorizontalList(
        @Field(ApiConst.Query.AZKAR_ID) id: Int
    ): MABaseResponse<Any>

}
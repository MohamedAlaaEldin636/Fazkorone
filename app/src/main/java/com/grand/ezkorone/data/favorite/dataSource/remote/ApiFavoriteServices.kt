package com.grand.ezkorone.data.favorite.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.favorite.ItemFavorite
import com.grand.ezkorone.domain.utils.MABasePaging
import com.grand.ezkorone.domain.utils.MABaseResponse
import retrofit2.http.*

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

    @GET("favorite")
    suspend fun getFavoriteList(): MABaseResponse<MABasePaging<ItemFavorite>>

}
package com.grand.ezkorone.data.sheikh.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.home.ItemZekrInList
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.sheikh.ItemSheikh
import com.grand.ezkorone.domain.utils.MABasePaging
import com.grand.ezkorone.domain.utils.MABaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSheikhServices {

    @GET("shaikhs")
    suspend fun getSheikhListForSalawat(
        @Query(ApiConst.Query.PRAYER_ID) prayerId: Int,
        @Query(ApiConst.Query.PAGE) page: Int,
    ): MABaseResponse<MABasePaging<ItemSheikh>>

    @FormUrlEncoded
    @GET("shaikhs/add_or_remove")
    suspend fun toggleSheikhSelectionForSalawat(
        @Field(ApiConst.Query.PRAYER_ID) sheikhId: Int,
    ): MABaseResponse<Any>

}

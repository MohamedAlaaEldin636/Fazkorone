package com.grand.ezkorone.data.sheikh.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.home.ItemZekrInList
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.sheikh.ItemSheikh
import com.grand.ezkorone.domain.taspeh.ItemTaspeh
import com.grand.ezkorone.domain.utils.MABasePaging
import com.grand.ezkorone.domain.utils.MABaseResponse
import retrofit2.http.*

interface ApiSheikhServices {

    @GET("shaikhs")
    suspend fun getSheikhListForSalawat(
        @Query(ApiConst.Query.PRAYER_ID) prayerId: Int,
        @Query(ApiConst.Query.PAGE) page: Int,
    ): MABaseResponse<MABasePaging<ItemSheikh>>

    @FormUrlEncoded
    @POST("shaikhs/add_or_remove")
    suspend fun toggleSheikhSelectionForSalawat(
        @Field(ApiConst.Query.PRAYER_ID) sheikhId: Int,
    ): MABaseResponse<Any>

    @FormUrlEncoded
    @POST("shaikhs/add_or_remove")
    suspend fun toggleSheikhSelectionForTaspeh(
        @Field(ApiConst.Query.PRAISE_ID) taspehId: Int,
    ): MABaseResponse<Any>

    @GET("praise-shaikhs")
    suspend fun getSheikhListForTaspeh(
        @Query(ApiConst.Query.PRAISE_ID) taspehId: Int,
        @Query(ApiConst.Query.PAGE) page: Int,
    ): MABaseResponse<MABasePaging<ItemSheikh>>

}

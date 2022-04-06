package com.grand.ezkorone.data.taspeh.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.home.ItemZekrInList
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.taspeh.ItemTaspeh
import com.grand.ezkorone.domain.utils.MABasePaging
import com.grand.ezkorone.domain.utils.MABaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiTaspehServices {

    @GET("praises")
    suspend fun getTaspehList(
        @Query(ApiConst.Query.PAGE) page: Int,
    ): MABaseResponse<MABasePaging<ItemTaspeh>>

}
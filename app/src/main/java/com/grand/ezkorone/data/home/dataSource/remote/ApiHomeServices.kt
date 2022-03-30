package com.grand.ezkorone.data.home.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.azan.ResponseAzan
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.utils.MABasePaging
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.MABaseResponseAladan
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiHomeServices {

    @GET("home-categories")
    suspend fun getHomeCategories(
        @Query(ApiConst.Query.PAGE) page: Int,
    ): MABaseResponse<MABasePaging<ItemZekrTopCategory>>

}
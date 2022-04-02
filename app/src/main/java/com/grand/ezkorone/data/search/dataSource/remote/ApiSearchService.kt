package com.grand.ezkorone.data.search.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.search.ItemSearchQuery
import com.grand.ezkorone.domain.utils.MABasePaging
import com.grand.ezkorone.domain.utils.MABaseResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiSearchService {

    @GET("search")
    suspend fun search(
        @Query(ApiConst.Query.PAGE) page: Int,
        @QueryMap map: Map<String, String>,
    ): MABaseResponse<MABasePaging<ItemSearchQuery>>

}
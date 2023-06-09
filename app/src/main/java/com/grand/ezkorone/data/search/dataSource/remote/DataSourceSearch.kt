package com.grand.ezkorone.data.search.dataSource.remote

import com.grand.ezkorone.data.api.ApiConst
import com.grand.ezkorone.data.home.dataSource.remote.ApiHomeServices
import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import com.grand.ezkorone.domain.utils.MABasePaging
import com.grand.ezkorone.domain.utils.MABaseResponse
import javax.inject.Inject

class DataSourceSearch @Inject constructor(
    private val apiService: ApiSearchService,
) : MABaseRemoteDataSource() {

    suspend fun search(query: String?, page: Int) = safeApiCall {
        val map = mutableMapOf<String, String>()
        if (!query.isNullOrEmpty()) {
            map[ApiConst.Query.SEARCH] = query

            apiService.search(page, map)
        }else {
            MABaseResponse(MABasePaging(
                emptyList(),
                null,
                1,
                null,
                "1",
                1,
                0,
                0,
                0,
                10
            ), "", 200, null, null)
        }
    }

}

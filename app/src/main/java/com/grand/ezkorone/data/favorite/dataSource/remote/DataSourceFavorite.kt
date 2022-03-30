package com.grand.ezkorone.data.favorite.dataSource.remote

import com.grand.ezkorone.data.home.dataSource.remote.ApiHomeServices
import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import javax.inject.Inject

class DataSourceFavorite @Inject constructor(
    private val apiService: ApiFavoriteServices,
) : MABaseRemoteDataSource() {

    suspend fun toggleFavoriteForHorizontalList(id: Int) = safeApiCall {
        apiService.toggleFavoriteForHorizontalList(id)
    }

    suspend fun toggleFavoriteForVerticalList(id: Int) = safeApiCall {
        apiService.toggleFavoriteForVerticalList(id)
    }

}
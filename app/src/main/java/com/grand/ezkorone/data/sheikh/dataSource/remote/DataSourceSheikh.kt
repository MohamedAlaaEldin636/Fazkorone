package com.grand.ezkorone.data.sheikh.dataSource.remote

import com.grand.ezkorone.data.basePaging.BasePaging
import com.grand.ezkorone.data.home.dataSource.remote.ApiHomeServices
import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import com.grand.ezkorone.domain.salah.SalahFardType
import javax.inject.Inject

class DataSourceSheikh @Inject constructor(
    private val apiService: ApiSheikhServices,
) : MABaseRemoteDataSource() {

    suspend fun getSheikhListForSalahFardType(type: SalahFardType, page: Int) = safeApiCall {
        apiService.getSheikhListForSalawat(type.apiIntValue, page)
    }

    suspend fun getSheikhListForTaspeh(taspehId: Int, page: Int) = safeApiCall {
        apiService.getSheikhListForTaspeh(taspehId, page)
    }

    suspend fun toggleSheikhSelectionForSalawat(sheikhId: Int) = safeApiCall {
        apiService.toggleSheikhSelectionForSalawat(sheikhId)
    }

    suspend fun toggleSheikhSelectionForTaspeh(sheikhId: Int) = safeApiCall {
        apiService.toggleSheikhSelectionForTaspeh(sheikhId)
    }

}

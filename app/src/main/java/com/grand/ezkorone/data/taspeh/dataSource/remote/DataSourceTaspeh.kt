package com.grand.ezkorone.data.taspeh.dataSource.remote

import com.grand.ezkorone.data.home.dataSource.remote.ApiHomeServices
import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import com.grand.ezkorone.domain.taspeh.ResponseTaspeh
import javax.inject.Inject

class DataSourceTaspeh @Inject constructor(
    private val apiService: ApiTaspehServices,
) : MABaseRemoteDataSource() {

    suspend fun getTaspehList(page: Int) = safeApiCall {
        apiService.getTaspehList(page).mapData {
            ResponseTaspeh(
                it?.data.orEmpty(),
                page,
                page > 1,
                it?.links?.next.isNullOrEmpty().not()
            )
        }
    }

    suspend fun getTaspehListPaginated(page: Int) = safeApiCall {
        apiService.getTaspehList(page)
    }

}

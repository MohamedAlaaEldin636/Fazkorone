package com.grand.ezkorone.data.home.dataSource.remote

import androidx.paging.PagingData
import com.grand.ezkorone.core.extensions.minLengthOrPrefixZeros
import com.grand.ezkorone.data.azan.dataSource.remote.ApiAzanServices
import com.grand.ezkorone.data.basePaging.BasePaging
import com.grand.ezkorone.data.remote.MABaseRemoteDataSource
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class DataSourceHome @Inject constructor(
    private val apiService: ApiHomeServices,
) : MABaseRemoteDataSource() {

    suspend fun getHomeCategories(page: Int) = safeApiCall {
        apiService.getHomeCategories(page)
    }

}

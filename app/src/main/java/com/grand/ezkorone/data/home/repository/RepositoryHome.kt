package com.grand.ezkorone.data.home.repository

import androidx.paging.PagingData
import com.grand.ezkorone.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.grand.ezkorone.data.azan.dataSource.remote.DataSourceAzan
import com.grand.ezkorone.data.basePaging.BasePaging
import com.grand.ezkorone.data.home.dataSource.remote.DataSourceHome
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.domain.azan.ResponseAzan
import com.grand.ezkorone.domain.home.ItemZekrInList
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.utils.MABasePaging
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.domain.utils.mapImmediate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryHome @Inject constructor(
    private val dataSource: DataSourceHome,
    private val prefsApp: PrefsApp,
) {

    fun getHomeCategoriesVertical(): Flow<PagingData<ItemZekrTopCategory>> {
        return BasePaging.createFlowViaPager { page ->
            dataSource.getHomeCategories(page)
        }
    }

    fun getHomeCategoriesHorizontal() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<IdAndName>>> {
        emit(
            dataSource.getHomeCategories(1).mapImmediate { response ->
                response.mapData {
                    response.azkarCategories.orEmpty()
                }
            }
        )
    }

    fun getAzkarList(id: Int): Flow<PagingData<ItemZekrInList>> {
        return BasePaging.createFlowViaPager { page ->
            dataSource.getAzkarList(id, page)
        }
    }

}

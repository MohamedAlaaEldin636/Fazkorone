package com.grand.ezkorone.data.taspeh.repository

import androidx.paging.PagingData
import com.grand.ezkorone.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.grand.ezkorone.data.basePaging.BasePaging
import com.grand.ezkorone.data.home.dataSource.remote.DataSourceHome
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.taspeh.dataSource.remote.DataSourceTaspeh
import com.grand.ezkorone.domain.home.ItemZekrInList
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.taspeh.ItemTaspeh
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.domain.utils.mapImmediate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryTaspeh @Inject constructor(
    private val dataSource: DataSourceTaspeh,
    private val prefsApp: PrefsApp,
) {

    suspend fun getAzkarList(page: Int) =
        dataSource.getTaspehList(page)

    fun getTaspehListPaginated(): Flow<PagingData<ItemTaspeh>> {
        return BasePaging.createFlowViaPager { page ->
            dataSource.getTaspehListPaginated(page)
        }
    }

}

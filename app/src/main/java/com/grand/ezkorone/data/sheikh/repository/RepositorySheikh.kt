package com.grand.ezkorone.data.sheikh.repository

import androidx.paging.PagingData
import com.grand.ezkorone.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.grand.ezkorone.data.basePaging.BasePaging
import com.grand.ezkorone.data.home.dataSource.remote.DataSourceHome
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.sheikh.dataSource.remote.DataSourceSheikh
import com.grand.ezkorone.domain.home.ItemZekrInList
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.salah.SalahFardType
import com.grand.ezkorone.domain.sheikh.ItemSheikh
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.common.IdAndName
import com.grand.ezkorone.domain.utils.mapImmediate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositorySheikh @Inject constructor(
    private val dataSource: DataSourceSheikh,
) {

    fun getSheikhListForSalahFardType(type: SalahFardType): Flow<PagingData<ItemSheikh>> {
        return BasePaging.createFlowViaPager { page ->
            dataSource.getSheikhListForSalahFardType(type, page)
        }
    }

    suspend fun toggleSheikhSelectionForSalawat(sheikhId: Int) = dataSource
        .toggleSheikhSelectionForSalawat(sheikhId)

}

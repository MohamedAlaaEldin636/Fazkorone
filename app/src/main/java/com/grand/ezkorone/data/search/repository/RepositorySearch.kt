package com.grand.ezkorone.data.search.repository

import androidx.paging.PagingData
import com.grand.ezkorone.data.basePaging.BasePaging
import com.grand.ezkorone.data.home.dataSource.remote.DataSourceHome
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.search.dataSource.remote.DataSourceSearch
import com.grand.ezkorone.domain.home.ItemZekrTopCategory
import com.grand.ezkorone.domain.search.ItemSearchQuery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositorySearch @Inject constructor(
    private val dataSource: DataSourceSearch,
    private val prefsApp: PrefsApp,
) {

    fun search(query: String?): Flow<PagingData<ItemSearchQuery>> {
        return BasePaging.createFlowViaPager { page ->
            dataSource.search(query, page)
        }
    }

}

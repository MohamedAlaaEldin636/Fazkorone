package com.grand.ezkorone.data.favorite.repository

import com.grand.ezkorone.data.favorite.dataSource.remote.DataSourceFavorite
import com.grand.ezkorone.data.home.dataSource.remote.DataSourceHome
import com.grand.ezkorone.data.local.preferences.PrefsApp
import javax.inject.Inject

class RepositoryFavorite @Inject constructor(
    private val dataSource: DataSourceFavorite,
    private val prefsApp: PrefsApp,
) {

    suspend fun toggleFavoriteForHorizontalList(id: Int) = dataSource.toggleFavoriteForHorizontalList(id)

    suspend fun toggleFavoriteForVerticalList(id: Int) = dataSource.toggleFavoriteForVerticalList(id)

}

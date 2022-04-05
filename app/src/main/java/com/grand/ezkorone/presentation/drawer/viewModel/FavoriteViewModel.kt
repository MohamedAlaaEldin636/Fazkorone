package com.grand.ezkorone.presentation.drawer.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.grand.ezkorone.data.favorite.repository.RepositoryFavorite
import com.grand.ezkorone.presentation.drawer.adapters.RVItemFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    repoFavorite: RepositoryFavorite,
) : ViewModel() {

    val showEmptyView = MutableLiveData(false)

    val favoriteList = repoFavorite.getFavoriteList()

    val adapter = RVItemFavorite(repoFavorite)

    fun goToAzkar(view: View) {
        view.findNavController().navigateUp()
    }

}

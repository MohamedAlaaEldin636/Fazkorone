package com.grand.ezkorone.presentation.drawer.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor() : ViewModel() {

    val showEmptyView = MutableLiveData(true)

    fun goToAzkar(view: View) {
        // todo
    }

}

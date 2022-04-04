package com.grand.ezkorone.presentation.drawer.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import com.grand.ezkorone.data.local.preferences.PrefsSplash
import com.grand.ezkorone.presentation.drawer.adapters.RVItemWhoAreWe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WhoAreWeViewModel @Inject constructor(
    prefsSplash: PrefsSplash
) : ViewModel() {

    private val initialDataForApp = prefsSplash.getInitialDataForApp().asLiveData()

    val imageUrl = initialDataForApp.map { it?.socialMedia?.aboutUsImageUrl }

    val items = initialDataForApp.map { it?.aboutUsItems.orEmpty() }

    val adapter = RVItemWhoAreWe()

    fun goBack(view: View) {
        view.findNavController().navigateUp()
    }

}

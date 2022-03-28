package com.grand.ezkorone.presentation.location.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.grand.ezkorone.presentation.location.LocationSelectionFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditLocationViewModel @Inject constructor(

) : ViewModel() {

    val search = MutableLiveData("")

    val address = MutableLiveData("")

    val showLottie = MutableLiveData(false)

    fun autoDetectLocation(view: View) {
        // todo
    }

    fun toSearchPlace(view: View) {
        // todo
    }

}
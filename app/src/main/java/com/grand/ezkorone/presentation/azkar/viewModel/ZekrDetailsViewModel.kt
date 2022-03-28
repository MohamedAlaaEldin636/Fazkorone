package com.grand.ezkorone.presentation.azkar.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ZekrDetailsViewModel @Inject constructor() : ViewModel() {

    val search = MutableLiveData("")

    // todo change favorite for zekr mafe4 for page isa.
    fun toggleFavorite(view: View) {
        // todo
    }

    fun download(view: View) {
        // todo
    }

    fun play(view: View) {
        // todo
    }

    fun share(view: View) {
        // todo
    }

    fun incrementProgress(view: View) {
        // todo
    }

}

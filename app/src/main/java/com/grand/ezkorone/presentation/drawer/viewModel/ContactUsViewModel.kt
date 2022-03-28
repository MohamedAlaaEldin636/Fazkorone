package com.grand.ezkorone.presentation.drawer.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor() : ViewModel() {

    val name = MutableLiveData("")

    val email = MutableLiveData("")

    val purpose = MutableLiveData("")

    val message = MutableLiveData("")

    fun showPurposeOfContactPopUp(view: View) {
        // todo
    }

    fun pickImage(view: View) {
        // todo
    }

    fun send(view: View) {
        // todo
    }

}

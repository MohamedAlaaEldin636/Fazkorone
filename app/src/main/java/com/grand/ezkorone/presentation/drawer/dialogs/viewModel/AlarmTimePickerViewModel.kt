package com.grand.ezkorone.presentation.drawer.dialogs.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmTimePickerViewModel @Inject constructor() : ViewModel() {

    val time = MutableLiveData("")

    fun launchTimePicker(view: View) {
        // todo
    }

    fun save(view: View) {
        // todo
    }

}

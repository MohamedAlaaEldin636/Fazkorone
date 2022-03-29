package com.grand.ezkorone.presentation.drawer.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    // todo dunno if local or from api isa.
    val enableNotifications = MutableLiveData(true)

    fun toggleNotification(view: View) {
        // todo
    }

}

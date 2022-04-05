package com.grand.ezkorone.presentation.drawer.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.notifications.repository.RepositoryNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    repoNotifications: RepositoryNotifications,
    prefsApp: PrefsApp,
) : ViewModel() {

    // todo dunno if local or from api isa.
    val enableNotifications = prefsApp.isNotificationsEnabled().asLiveData()

    val notifications = repoNotifications.getNotifications()

    //val adapter;

    fun toggleNotification(view: View) {
        // todo
    }

}

package com.grand.ezkorone.presentation.drawer.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.grand.ezkorone.core.extensions.executeOnGlobalLoading
import com.grand.ezkorone.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.notifications.repository.RepositoryNotifications
import com.grand.ezkorone.domain.utils.MAResult
import com.grand.ezkorone.presentation.drawer.NotificationsFragment
import com.grand.ezkorone.presentation.drawer.adapters.RVItemNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repoNotifications: RepositoryNotifications,
    private val prefsApp: PrefsApp,
) : ViewModel() {

    val enableNotifications = prefsApp.isNotificationsEnabled().asLiveData()

    val notifications = repoNotifications.getNotifications()

    val adapter = RVItemNotification()

    fun toggleNotification(view: View) {
        val fragment = view.findFragment<NotificationsFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                val newState = !(enableNotifications.value ?: false)

                repoNotifications.enableOrDisableNotificationsSuspend(newState).also {
                    if (it is MAResult.Success) {
                        prefsApp.setNotificationStatus(newState)
                    }
                }
            },
            afterHidingLoading = {
                // Nothing is needed isa.
            },
            canCancelDialog = true
        )
    }

}

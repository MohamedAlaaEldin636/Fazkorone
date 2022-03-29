package com.grand.ezkorone.presentation.onBoard.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.grand.ezkorone.core.customTypes.RetryAbleFlow
import com.grand.ezkorone.core.extensions.popAllBackStacks
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.data.notifications.repository.RepositoryNotifications
import com.grand.ezkorone.presentation.location.LocationSelectionFragmentArgs
import com.grand.ezkorone.presentation.onBoard.OnBoardFragment
import com.grand.ezkorone.presentation.onBoard.OnBoardFragmentDirections
import com.grand.ezkorone.presentation.splash.SplashFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    repository: RepositoryNotifications,
    private val prefsApp: PrefsApp
) : ViewModel() {

    private val _handleDisableNotificationsRetryAbleFlow = MutableLiveData(false)
    val liveDataOfRetryAbleFlowDisableNotifications = _handleDisableNotificationsRetryAbleFlow.map {
        if (it == true) {
            // NOTE only call api if false as Amgad said default is true isa.
            RetryAbleFlow {
                repository.enableOrDisableNotifications(false)
            }
        }else {
            null
        }
    }

    fun enableReceivingNotifications(view: View) {
        saveLocallyAndProceedToNextScreen(view.findFragment(), true)
    }

    fun disableReceivingNotifications() {
        _handleDisableNotificationsRetryAbleFlow.value = true
    }

    fun saveLocallyAndProceedToNextScreen(fragment: OnBoardFragment, enableNotifications: Boolean) {
        fragment.lifecycleScope.launch {
            prefsApp.setNotificationStatus(enableNotifications)

            fragment.findNavController().popAllBackStacks()

            fragment.findNavController().navigate(OnBoardFragmentDirections.actionDestOnBoardToDestBottomNav())
        }
    }

}

package com.grand.ezkorone.presentation.main.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.getAppWebLinkOnGooglePay
import com.grand.ezkorone.core.extensions.launchAppOnGooglePlay
import com.grand.ezkorone.core.extensions.launchShareText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerHeaderMainViewModel @Inject constructor() : ViewModel() {

    fun whoAreWe(view: View) {
        // todo
    }

    fun contactUs(view: View) {
        // todo
    }

    fun favorite(view: View) {
        // todo
    }

    fun alarms(view: View) {
        // todo
    }

    fun notifications(view: View) {
        // todo
    }

    /*fun donate(view: View) {
        // to-do
    }*/

    fun shareApp(view: View) {
        view.context.apply {
            launchShareText(
                "${getString(R.string.app_name)}\n${getAppWebLinkOnGooglePay()}"
            )
        }
    }

    fun rateApp(view: View) {
        view.context.launchAppOnGooglePlay()
    }

    fun toInsta(view: View) {
        // todo
    }

    fun toTwitter(view: View) {
        // todo
    }

}

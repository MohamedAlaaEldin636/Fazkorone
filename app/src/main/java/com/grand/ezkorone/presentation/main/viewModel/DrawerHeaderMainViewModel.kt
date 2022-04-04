package com.grand.ezkorone.presentation.main.viewModel

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.databinding.DrawerHeaderMainBinding
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections
import com.grand.ezkorone.presentation.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerHeaderMainViewModel @Inject constructor() : ViewModel() {

    fun whoAreWe(view: View) = view.closeDrawerThenActWithNavController {
        navigate(
            BottomNavFragmentDirections.actionDestBottomNavToDestWhoAreWe()
        )
    }

    fun contactUs(view: View) = view.closeDrawerThenActWithNavController {
        navigate(
            BottomNavFragmentDirections.actionDestBottomNavToDestContactUs()
        )
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

    private fun View.closeDrawerThenActWithNavController(action: NavController.() -> Unit) {
        val activity = getMainActivityOrNull() ?: return

        activity.closeDrawerLayout()

        activity.findNavControllerOfProject().action()
    }

    private fun View.closeDrawerThenActWithActivity(action: MainActivity.() -> Unit) {
        val activity = getMainActivityOrNull() ?: return

        activity.closeDrawerLayout()

        activity.action()
    }

    private fun View.getMainActivityOrNull(): MainActivity? {
        return DataBindingUtil.findBinding<DrawerHeaderMainBinding>(this)
            ?.lifecycleOwner as? MainActivity
    }

}

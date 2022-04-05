package com.grand.ezkorone.presentation.main.viewModel

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.navigation.NavController
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.data.local.preferences.PrefsSplash
import com.grand.ezkorone.databinding.DrawerHeaderMainBinding
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections
import com.grand.ezkorone.presentation.main.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawerHeaderMainViewModel @Inject constructor(
    prefsSplash: PrefsSplash,
) : ViewModel() {

    private val initialDataForApp = prefsSplash.getInitialDataForApp().asLiveData()

    val instaLink = initialDataForApp.map { it?.socialMedia?.instaUrl }

    val twitterLink = initialDataForApp.map { it?.socialMedia?.instaUrl }

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

    fun favorite(view: View) = view.closeDrawerThenActWithNavController {
        navigate(
            BottomNavFragmentDirections.actionDestBottomNavToDestFavorite()
        )
    }

    fun alarms(view: View) = view.closeDrawerThenActWithNavController {
        navigate(
            BottomNavFragmentDirections.actionDestBottomNavToDestAlarms()
        )
    }

    fun notifications(view: View) = view.closeDrawerThenActWithNavController {
        navigate(
            BottomNavFragmentDirections.actionDestBottomNavToDestNotifications()
        )
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
        view.context.launchBrowser(instaLink.value ?: return)
    }

    fun toTwitter(view: View) {
        view.context.launchBrowser(twitterLink.value ?: return)
    }

    private fun View.closeDrawerThenActWithNavController(action: NavController.() -> Unit) {
        val activity = getMainActivityOrNull() ?: return

        activity.closeDrawerLayout()

        activity.findNavControllerOfProject().action()
    }

    /*private fun View.closeDrawerThenActWithActivity(action: MainActivity.() -> Unit) {
        val activity = getMainActivityOrNull() ?: return

        activity.closeDrawerLayout()

        activity.action()
    }*/

    private fun View.getMainActivityOrNull(): MainActivity? {
        return DataBindingUtil.findBinding<DrawerHeaderMainBinding>(this)
            ?.lifecycleOwner as? MainActivity
    }

}

package com.grand.ezkorone.presentation.internalNavigation.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.grand.ezkorone.NavBottomNavDirections
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.RetryAbleFlow
import com.grand.ezkorone.core.customTypes.switchMapMultiple
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.getMonthName
import com.grand.ezkorone.core.extensions.myApp
import com.grand.ezkorone.core.extensions.openDrawerLayout
import com.grand.ezkorone.data.azan.repository.RepositoryAzan
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.domain.azan.SalawatTimes
import com.grand.ezkorone.domain.azan.TodayAndTomorrowSalawatTimes
import com.grand.ezkorone.presentation.internalNavigation.BottomNavFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    prefsApp: PrefsApp,
    azanRepo: RepositoryAzan
) : AndroidViewModel(application) {

    // todo check that when change in prefs via edit location screen then here changed else keep listeneing to changes dunno how isa.
    private val locationData = prefsApp.getLocationData().map { it!! }.asLiveData()

    val currentDateTime = MutableLiveData(LocalDateTime.now())

    val address = locationData.map {
        it.address
    }

    val regularDate = currentDateTime.map {
        "${it.dayOfMonth} ${myApp.getMonthName(it.monthValue.dec())} ${it.year}"
    }

    val hijrahDate = currentDateTime.map {
        HijrahDate.from(it).format(
            DateTimeFormatter.ofPattern("d LLLL yyyy ${myApp.getString(R.string.h)}", Locale("ar"))
        )
    }

    val retryAbleFlowAzanTimes = RetryAbleFlow(azanRepo::getAzanTimesTodayAndTomorrow)

    val todayAndTomorrowSalawatTimes = MutableLiveData<TodayAndTomorrowSalawatTimes>()

    private val calculations = switchMapMultiple(currentDateTime, todayAndTomorrowSalawatTimes) {
        val currentDateTime = currentDateTime.value
        val salawatTimes = todayAndTomorrowSalawatTimes.value

        val calculations = if (currentDateTime != null && salawatTimes != null) {
            salawatTimes.today.getNextHourAndMinutesAndRemainingTimeAsHourAndMinutesWithType(
                currentDateTime.hour,
                currentDateTime.minute,
                salawatTimes.tomorrow
            )
        }else {
            null
        }

        MutableLiveData(calculations)
    }
    val prayExactTime = calculations.map {
        it?.getNextTime(myApp) ?: myApp.getString(R.string.loading)
    }
    val prayRemainingTime = calculations.map {
        it?.getRemainingTime().orEmpty()
    }

    fun showNavDrawer(view: View) {
        view.openDrawerLayout()
    }

    fun toSearchPlace(view: View) {
        view.findNavControllerOfProject().navigate(BottomNavFragmentDirections.actionDestBottomNavToDestSearchQueries())
    }

    fun editLocation(view: View) {
        view.findNavControllerOfProject().navigate(BottomNavFragmentDirections.actionDestBottomNavToDestEditLocation())
    }

    fun toPrayBottomNav(view: View) {
        view.findNavController().navigate(NavBottomNavDirections.actionGlobalDestSalah())
    }

}
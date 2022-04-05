package com.grand.ezkorone.presentation.drawer.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.myApp
import com.grand.ezkorone.data.local.preferences.PrefsAlarms
import com.grand.ezkorone.domain.alarms.TimeInDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AlarmsViewModel @Inject constructor(
    application: Application,
    prefsAlarms: PrefsAlarms,
) : AndroidViewModel(application) {

    private val stringAm by lazy {
        myApp.getString(R.string.am)
    }

    private val stringPm by lazy {
        myApp.getString(R.string.pm)
    }

    val azkarSabahTime = prefsAlarms.getDrawerAlarmAzkarSabah().map {
        it?.format()
    }.asLiveData()

    val azkarMasaaTime = prefsAlarms.getDrawerAlarmAzkarMasaa().map {
        it?.format()
    }.asLiveData()

    val taspehTime = prefsAlarms.getDrawerAlarmTaspeh().map {
        it?.format()
    }.asLiveData()

    fun toggleAzkarSabah(view: View) {
        if (azkarSabahTime.value.isNullOrEmpty()) {
            // Turn ON

        }else {
            // Turn OFF

        }
        // todo either off and in that case cancel alarm manager or on to launch dialog fragment isa.
        //  and also change el text in data store as well isa.
    }

    fun toggleAzkarMasaa(view: View) {
        // todo
    }

    fun toggleTaspeh(view: View) {
        // todo
    }

    private fun TimeInDay.format(): String {
        return "$hour12 : $minute ${if (isAm) stringAm else stringPm}"
    }

}

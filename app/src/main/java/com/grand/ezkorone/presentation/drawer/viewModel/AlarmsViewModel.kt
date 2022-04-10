package com.grand.ezkorone.presentation.drawer.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.broadcastReceiver.AlarmsBroadcastReceiver
import com.grand.ezkorone.core.extensions.myApp
import com.grand.ezkorone.data.local.preferences.PrefsAlarms
import com.grand.ezkorone.domain.alarms.TimeInDay
import com.grand.ezkorone.presentation.drawer.AlarmsFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmsViewModel @Inject constructor(
    application: Application,
    val prefsAlarms: PrefsAlarms,
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
        toggleAlarm(azkarSabahTime.value.isNullOrEmpty(), view, myApp.getString(R.string.azkar_sabah_alarm)) {
            prefsAlarms.setDrawerAlarmAzkarSabah(null)
        }
    }

    fun toggleAzkarMasaa(view: View) {
        toggleAlarm(azkarMasaaTime.value.isNullOrEmpty(), view, myApp.getString(R.string.azkar_masaa_alarm)) {
            prefsAlarms.setDrawerAlarmAzkarMasaa(null)
        }
    }

    fun toggleTaspeh(view: View) {
        toggleAlarm(taspehTime.value.isNullOrEmpty(), view, myApp.getString(R.string.taspeh_alarm)) {
            prefsAlarms.setDrawerAlarmTaspeh(null)
        }
    }

    private fun toggleAlarm(performTurnOn: Boolean, view: View, tag: String, prefsAction: suspend () -> Unit) {
        if (performTurnOn) {
            // Turn ON
            view.findNavController().navigate(
                AlarmsFragmentDirections.actionDestAlarmsToDestAlarmTimePickerDialog(tag)
            )
        }else {
            // Turn OFF so cancel alarm manager work manager and data store as well isa.
            AlarmsBroadcastReceiver.cancelAlarmManager(view.context, tag)

            viewModelScope.launch {
                prefsAction()
            }
        }
    }

    private fun TimeInDay.format(): String {
        return "$minute : $hour12 ${if (isAm) stringAm else stringPm}"
    }

}

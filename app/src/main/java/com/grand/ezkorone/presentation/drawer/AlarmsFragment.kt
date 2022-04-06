package com.grand.ezkorone.presentation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.broadcastReceiver.AlarmsBroadcastReceiver
import com.grand.ezkorone.core.extensions.fromJson
import com.grand.ezkorone.databinding.FragmentAlarmsBinding
import com.grand.ezkorone.domain.alarms.TimeInDay
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.drawer.dialogs.AlarmTimePickerDialogFragment
import com.grand.ezkorone.presentation.drawer.viewModel.AlarmsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.*

@AndroidEntryPoint
class AlarmsFragment : MABaseFragment<FragmentAlarmsBinding>() {

    private val viewModel by viewModels<AlarmsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_alarms

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData(
            AlarmTimePickerDialogFragment.SAVED_STATE_RESULT_JSON,
            ""
        )?.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                findNavController().currentBackStackEntry?.savedStateHandle?.set(
                    AlarmTimePickerDialogFragment.SAVED_STATE_RESULT_JSON,
                    ""
                )

                val result = it.fromJson<AlarmTimePickerDialogFragment.Result>()
                when (result.argTitleName) {
                    getString(R.string.azkar_sabah_alarm) -> viewModel.viewModelScope.launch {
                        viewModel.prefsAlarms.setDrawerAlarmAzkarSabah(result.timeInDay)
                    }
                    getString(R.string.azkar_masaa_alarm) -> viewModel.viewModelScope.launch {
                        viewModel.prefsAlarms.setDrawerAlarmAzkarMasaa(result.timeInDay)
                    }
                    getString(R.string.taspeh_alarm) -> viewModel.viewModelScope.launch {
                        viewModel.prefsAlarms.setDrawerAlarmTaspeh(result.timeInDay)
                    }
                    else -> return@observe
                }

                scheduleAlarm(result.argTitleName, result.timeInDay)
            }
        }
    }

    private fun scheduleAlarm(name: String, timeInDay: TimeInDay) {
        // todo check still has permission if api 31 and above isa -> https://developer.android.com/training/scheduling/alarms#exact-permission-check

        val firstTriggerDateTime = LocalDateTime.of(
            LocalDate.now(),
            LocalTime.of(timeInDay.hour24, timeInDay.minute)
        ).let {
            if (it.isBefore(LocalDateTime.now())) {
                it.plusDays(1)
            }else {
                it
            }
        }

        AlarmsBroadcastReceiver.scheduleAlarmManager(requireContext(), firstTriggerDateTime, name)
    }

}

package com.grand.ezkorone.presentation.drawer.dialogs.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.navigation.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.findNavControllerOfProject
import com.grand.ezkorone.core.extensions.myApp
import com.grand.ezkorone.core.extensions.showErrorToast
import com.grand.ezkorone.core.extensions.toJson
import com.grand.ezkorone.domain.alarms.TimeInDay
import com.grand.ezkorone.presentation.drawer.dialogs.AlarmTimePickerDialogFragment
import com.grand.ezkorone.presentation.drawer.dialogs.AlarmTimePickerDialogFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AlarmTimePickerViewModel @Inject constructor(
    application: Application,
    private val args: AlarmTimePickerDialogFragmentArgs
) : AndroidViewModel(application) {

    companion object {
        private const val DIALOG_TIME_PICKER_KEY = "DIALOG_TIME_PICKER_KEY"
    }

    private val stringAm by lazy {
        myApp.getString(R.string.am)
    }

    private val stringPm by lazy {
        myApp.getString(R.string.pm)
    }

    val title = "${myApp.getString(R.string.determine_your_remind_time)} ${args.titleName}"

    private val timeInDay = MutableLiveData<TimeInDay>()

    val time = timeInDay.map {
        it?.format()
    }

    fun launchTimePicker(view: View) {
        val now = LocalDateTime.now()

        val timePicker = MaterialTimePicker.Builder()
            .setTheme(R.style.ThemeOverlay_App_TimePicker)
            .setTitleText(view.context.getString(R.string.determine_the_time))
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(now.hour)
            .setMinute(now.minute)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            timeInDay.postValue(TimeInDay(timePicker.hour, timePicker.minute))
        }

        timePicker.show(view.findFragment<Fragment>().childFragmentManager, DIALOG_TIME_PICKER_KEY)
    }

    fun save(view: View) {
        val timeInDay = timeInDay.value
            ?: return view.context.showErrorToast(view.context.getString(R.string.field_required))

        val navController = view.findNavControllerOfProject()

        navController.navigateUp()

        navController.currentBackStackEntry?.savedStateHandle?.set(
            AlarmTimePickerDialogFragment.SAVED_STATE_RESULT_JSON,
            AlarmTimePickerDialogFragment.Result(args.titleName, timeInDay).toJson()
        )
    }

    private fun TimeInDay.format(): String {
        return "$minute : $hour12 ${if (isAm) stringAm else stringPm}"
    }

}

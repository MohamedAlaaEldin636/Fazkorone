package com.grand.ezkorone.presentation.drawer.dialogs

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.getMyDrawable
import com.grand.ezkorone.databinding.DialogFragmentAlarmTimePickerBinding
import com.grand.ezkorone.databinding.FragmentContactUsBinding
import com.grand.ezkorone.domain.alarms.TimeInDay
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.base.dialogs.MADialogFragment
import com.grand.ezkorone.presentation.drawer.dialogs.viewModel.AlarmTimePickerViewModel
import com.grand.ezkorone.presentation.drawer.viewModel.ContactUsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmTimePickerDialogFragment : MADialogFragment<DialogFragmentAlarmTimePickerBinding>() {

    companion object {
        const val SAVED_STATE_RESULT_JSON = "SAVED_STATE_RESULT_JSON"
    }

    private val viewModel by viewModels<AlarmTimePickerViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_alarm_time_picker

    override val windowGravity: Int = Gravity.BOTTOM

    @CallSuper
    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(getMyDrawable(R.drawable.dr_top_round_white))

        window.attributes?.windowAnimations = R.style.ScaleDialogAnim
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    data class Result(
        val argTitleName: String,
        val timeInDay: TimeInDay,
    )

}

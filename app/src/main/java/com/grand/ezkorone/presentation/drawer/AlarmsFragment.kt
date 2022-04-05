package com.grand.ezkorone.presentation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.AbstractListDetailFragment
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.fromJson
import com.grand.ezkorone.databinding.FragmentAlarmsBinding
import com.grand.ezkorone.databinding.FragmentFavoriteBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.drawer.dialogs.AlarmTimePickerDialogFragment
import com.grand.ezkorone.presentation.drawer.viewModel.AlarmsViewModel
import com.grand.ezkorone.presentation.drawer.viewModel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                    getString(R.string.azkar_sabah_alarm) -> {

                    }
                    getString(R.string.azkar_masaa_alarm) -> {

                    }
                    getString(R.string.taspeh_alarm) -> {

                    }
                }

                // todo it must have been off to be turned on so call alarm manager and call local data store as well isa.
            }
        }
    }

}

package com.grand.ezkorone.presentation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentAlarmsBinding
import com.grand.ezkorone.databinding.FragmentFavoriteBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
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

    }

}

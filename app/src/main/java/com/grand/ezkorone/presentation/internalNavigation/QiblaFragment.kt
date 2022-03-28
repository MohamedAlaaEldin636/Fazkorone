package com.grand.ezkorone.presentation.internalNavigation

import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentQiblaBinding
import com.grand.ezkorone.databinding.FragmentSalahBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.QiblaViewModel
import com.grand.ezkorone.presentation.internalNavigation.viewModel.SalahViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QiblaFragment : MABaseFragment<FragmentQiblaBinding>() {

    private val viewModel by viewModels<QiblaViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_qibla

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}


package com.grand.ezkorone.presentation.internalNavigation

import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentQiblaBinding
import com.grand.ezkorone.databinding.FragmentTaspehBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.QiblaViewModel
import com.grand.ezkorone.presentation.internalNavigation.viewModel.TaspehViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaspehFragment : MABaseFragment<FragmentTaspehBinding>() {

    private val viewModel by viewModels<TaspehViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_taspeh

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}

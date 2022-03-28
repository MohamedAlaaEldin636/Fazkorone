package com.grand.ezkorone.presentation.internalNavigation

import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentHomeBinding
import com.grand.ezkorone.databinding.FragmentSalahBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.HomeViewModel
import com.grand.ezkorone.presentation.internalNavigation.viewModel.SalahViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalahFragment : MABaseFragment<FragmentSalahBinding>() {

    private val viewModel by viewModels<SalahViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_salah

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}

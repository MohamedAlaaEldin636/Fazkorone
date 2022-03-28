package com.grand.ezkorone.presentation.sheikh

import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentSheikhListBinding
import com.grand.ezkorone.databinding.FragmentTaspehBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.TaspehViewModel
import com.grand.ezkorone.presentation.sheikh.viewModel.SheikhListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SheikhListFragment : MABaseFragment<FragmentSheikhListBinding>() {

    private val viewModel by viewModels<SheikhListViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_sheikh_list

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

}

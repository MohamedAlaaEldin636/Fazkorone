package com.grand.ezkorone.presentation.azkar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentAzkarListBinding
import com.grand.ezkorone.databinding.FragmentPickZekrBinding
import com.grand.ezkorone.presentation.azkar.viewModel.AzkarListViewModel
import com.grand.ezkorone.presentation.azkar.viewModel.PickZekrViewModel
import com.grand.ezkorone.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickZekrFragment : MABaseFragment<FragmentPickZekrBinding>() {

    private val viewModel by viewModels<PickZekrViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_pick_zekr

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}

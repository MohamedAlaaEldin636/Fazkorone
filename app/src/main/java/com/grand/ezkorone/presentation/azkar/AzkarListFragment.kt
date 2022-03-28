package com.grand.ezkorone.presentation.azkar

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentAzkarListBinding
import com.grand.ezkorone.databinding.FragmentZekrDetailsBinding
import com.grand.ezkorone.presentation.azkar.viewModel.AzkarListViewModel
import com.grand.ezkorone.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AzkarListFragment : MABaseFragment<FragmentAzkarListBinding>() {

    private val viewModel by viewModels<AzkarListViewModel>()

    private val args by navArgs<AzkarListFragmentArgs>()

    override fun getLayoutId(): Int = R.layout.fragment_azkar_list

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel.titleToolbar.postValue(args.toolbarTitle)
    }

}

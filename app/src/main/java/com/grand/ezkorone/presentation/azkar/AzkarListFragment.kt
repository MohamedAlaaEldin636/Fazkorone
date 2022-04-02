package com.grand.ezkorone.presentation.azkar

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.withCustomAdapters
import com.grand.ezkorone.databinding.FragmentAzkarListBinding
import com.grand.ezkorone.databinding.FragmentZekrDetailsBinding
import com.grand.ezkorone.presentation.azkar.viewModel.AzkarListViewModel
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val headerAdapterCurrent = LSAdapterLoadingErrorEmpty(viewModel.adapter, false)
        val footerAdapterFinished = LSAdapterLoadingErrorEmpty(viewModel.adapter, true)
        binding?.recyclerView?.adapter = viewModel.adapter.withCustomAdapters(
            headerAdapterCurrent,
            footerAdapterFinished
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.azkar.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }
    }

}

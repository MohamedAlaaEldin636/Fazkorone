package com.grand.ezkorone.presentation.sheikh

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.withCustomAdapters
import com.grand.ezkorone.databinding.FragmentSheikhListBinding
import com.grand.ezkorone.databinding.FragmentTaspehBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import com.grand.ezkorone.presentation.internalNavigation.viewModel.TaspehViewModel
import com.grand.ezkorone.presentation.sheikh.viewModel.SheikhListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SheikhListFragment : MABaseFragment<FragmentSheikhListBinding>() {

    companion object {
        const val SAVED_STATE_SELECTED_JSON_ITEM_SHEIKH = "SheikhListFragment.SAVED_STATE_SELECTED_JSON_ITEM_SHEIKH"
    }

    private val viewModel by viewModels<SheikhListViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_sheikh_list

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val headerAdapterCurrent = LSAdapterLoadingErrorEmpty(viewModel.adapter, false)
        val footerAdapterFinished = LSAdapterLoadingErrorEmpty(viewModel.adapter, true)
        binding?.recyclerView?.adapter = viewModel.adapter.withCustomAdapters(
            headerAdapterCurrent,
            footerAdapterFinished
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sheikhList.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }
    }

}

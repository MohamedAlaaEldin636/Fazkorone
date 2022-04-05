package com.grand.ezkorone.presentation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.withCustomAdapters
import com.grand.ezkorone.databinding.FragmentContactUsBinding
import com.grand.ezkorone.databinding.FragmentFavoriteBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import com.grand.ezkorone.presentation.drawer.viewModel.ContactUsViewModel
import com.grand.ezkorone.presentation.drawer.viewModel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : MABaseFragment<FragmentFavoriteBinding>() {

    private val viewModel by viewModels<FavoriteViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_favorite

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val headerAdapterCurrent = LSAdapterLoadingErrorEmpty(viewModel.adapter, false, canShowEmptyView = false)
        val footerAdapterFinished = LSAdapterLoadingErrorEmpty(viewModel.adapter, true, canShowEmptyView = false)
        binding?.recyclerView?.adapter = viewModel.adapter.withCustomAdapters(
            headerAdapterCurrent,
            footerAdapterFinished
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.favoriteList.collectLatest {
                        viewModel.adapter.submitData(it)
                    }
                }

                launch {
                    viewModel.adapter.loadStateFlow.collectLatest { loadState ->
                        if (loadState/*.source*/.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                            viewModel.showEmptyView.value = viewModel.adapter.snapshot().isEmpty()
                        }
                    }
                }
            }
        }
    }

}

package com.grand.ezkorone.presentation.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.withCustomAdapters
import com.grand.ezkorone.databinding.FragmentSearchQueriesBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import com.grand.ezkorone.presentation.search.viewModel.SearchQueriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchQueriesFragment : MABaseFragment<FragmentSearchQueriesBinding>() {

    private val viewModel by viewModels<SearchQueriesViewModel>()

    private var job: Job? = null

    override fun getLayoutId(): Int = R.layout.fragment_search_queries

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
                viewModel.data.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }

        viewModel.query.observe(viewLifecycleOwner) {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(100)

                viewModel.search.value = it.orEmpty()
            }
        }
    }

}

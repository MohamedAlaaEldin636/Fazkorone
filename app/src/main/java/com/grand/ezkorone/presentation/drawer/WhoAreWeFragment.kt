package com.grand.ezkorone.presentation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.withCustomAdapters
import com.grand.ezkorone.databinding.FragmentWhoAreWeBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import com.grand.ezkorone.presentation.drawer.viewModel.WhoAreWeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhoAreWeFragment : MABaseFragment<FragmentWhoAreWeBinding>() {

    private val viewModel by viewModels<WhoAreWeViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_who_are_we

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.adapter = viewModel.adapter

        viewModel.items.observe(viewLifecycleOwner) {
            viewModel.adapter.submitList(it.orEmpty())
        }
    }

}

package com.grand.ezkorone.presentation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.withCustomAdapters
import com.grand.ezkorone.databinding.FragmentNotificationsBinding
import com.grand.ezkorone.databinding.FragmentWhoAreWeBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import com.grand.ezkorone.presentation.drawer.viewModel.NotificationsViewModel
import com.grand.ezkorone.presentation.drawer.viewModel.WhoAreWeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : MABaseFragment<FragmentNotificationsBinding>() {

    private val viewModel by viewModels<NotificationsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_notifications

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
                viewModel.notifications.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }
    }

}

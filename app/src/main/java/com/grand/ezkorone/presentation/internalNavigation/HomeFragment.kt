package com.grand.ezkorone.presentation.internalNavigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.grand.ezkorone.core.extensions.withCustomAdapters
import com.grand.ezkorone.databinding.FragmentHomeBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.base.adapters.LSAdapterLoadingErrorEmpty
import com.grand.ezkorone.presentation.internalNavigation.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class HomeFragment : MABaseFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    private var timer: Timer? = null

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun onResume() {
        super.onResume()

        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                viewModel.currentDateTime.postValue(LocalDateTime.now())
            }
        }, 0, 1_000)
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.horizontalRecyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding?.horizontalRecyclerView?.adapter = viewModel.adapterItemTextInCard

        binding?.verticalRecyclerView?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val headerAdapterCurrent = LSAdapterLoadingErrorEmpty(viewModel.adapterItemHomeZekr, false)
        val footerAdapterFinished = LSAdapterLoadingErrorEmpty(viewModel.adapterItemHomeZekr, true)
        binding?.verticalRecyclerView?.adapter = viewModel.adapterItemHomeZekr.withCustomAdapters(
            headerAdapterCurrent,
            footerAdapterFinished
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeCategoriesVertical.collectLatest {
                    viewModel.adapterItemHomeZekr.submitData(it)
                }
            }
        }

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlowAzanTimes) {
            viewModel.todayAndTomorrowSalawatTimes.value = it.data

            binding?.root?.post {
                handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlowHomeCategoriesHorizontal) { response ->
                    viewModel.adapterItemTextInCard.submitList(response.data)
                }
            }
        }
    }

    override fun onPause() {
        timer?.cancel()
        timer = null

        super.onPause()
    }

}

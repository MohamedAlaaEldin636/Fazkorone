package com.grand.ezkorone.presentation.internalNavigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.grand.ezkorone.databinding.FragmentHomeBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        }, 0, 60 * 1_000)
    }

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // todo on save things below save in data store ashan salah tab isa.
        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlowAzanTimes) {
            viewModel.todayAndTomorrowSalawatTimes.value = it.data
        }
    }

    override fun onPause() {
        timer?.cancel()
        timer = null

        super.onPause()
    }

}

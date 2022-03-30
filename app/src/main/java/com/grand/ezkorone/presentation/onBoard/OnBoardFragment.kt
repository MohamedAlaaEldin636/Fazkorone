package com.grand.ezkorone.presentation.onBoard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.grand.ezkorone.databinding.FragmentOnBoardBinding
import com.grand.ezkorone.databinding.FragmentSplashBinding
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.onBoard.viewModel.OnBoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class OnBoardFragment : MABaseFragment<FragmentOnBoardBinding>() {

    private val viewModel by viewModels<OnBoardViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_on_board

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.e("iodwjowejd 3")

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
            viewModel.liveDataOfRetryAbleFlowDisableNotifications.observe(viewLifecycleOwner) {
                if (it != null) {
                    handleRetryAbleFlowWithMustHaveResultWithNullability(it) {
                        viewModel.saveLocallyAndProceedToNextScreen(this, false)
                    }
                }
            }
        }
    }

}

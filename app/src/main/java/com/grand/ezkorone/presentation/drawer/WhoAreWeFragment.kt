package com.grand.ezkorone.presentation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentSplashBinding
import com.grand.ezkorone.databinding.FragmentWhoAreWeBinding
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.drawer.viewModel.WhoAreWeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WhoAreWeFragment : MABaseFragment<FragmentWhoAreWeBinding>() {

    private val viewModel by viewModels<WhoAreWeViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_who_are_we

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}

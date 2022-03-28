package com.grand.ezkorone.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentSplashBinding
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import com.grand.ezkorone.presentation.base.MABaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : MABaseFragment<FragmentSplashBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // https://www.youtube.com/watch?v=fSB6_KE95bU&t=746s
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(1_000)

                // todo set firebase token isa, or see if pusher has setup as well isa.

                when (prefsSplash.getInitialLaunch().first()) {
                    SplashInitialLaunch.HOME -> {
                        // todo use deep links isa. since the destinations would be in app modules places isa.
                        //findNavController().navigate(SplashFragmentDirections.actionDestSplashToNavUser())
                    }
                    SplashInitialLaunch.ON_BOARD, null -> {
                        //findNavController().navigate(SplashFragmentDirections.actionDestSplashToNavUser())
                    }
                }
            }
        }
    }

}

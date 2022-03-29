package com.grand.ezkorone.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.core.extensions.fromJson
import com.grand.ezkorone.core.extensions.navigateDeepLinkWithoutOptions
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.databinding.FragmentSplashBinding
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.location.LocationSelectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : MABaseFragment<FragmentSplashBinding>() {

    @Inject
    protected lateinit var prefsApp: PrefsApp

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData(
            LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
            ""
        )?.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                findNavController().currentBackStackEntry?.savedStateHandle?.set(
                    LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON,
                    ""
                )

                val locationData = it.fromJson<LocationData>()

                activityViewModel.globalLoading.value = true

                lifecycleScope.launch {
                    prefsApp.setLocationData(locationData)

                    delay(150)

                    activityViewModel.globalLoading.value = false

                    delay(150)

                    findNavController().navigate(SplashFragmentDirections.actionDestSplashToDestOnBoard())
                }
            }
        }

        // https://www.youtube.com/watch?v=fSB6_KE95bU&t=746s
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(1_000)

                if (prefsApp.getLocationData().first() != null) {
                    return@repeatOnLifecycle
                }

                when (prefsSplash.getInitialLaunch().first()) {
                    SplashInitialLaunch.HOME -> {
                        while (findNavController().popBackStack()) {
                            continue
                        }

                        // todo set this after calling notifications enable/disable isa.
                        findNavController().navigate(SplashFragmentDirections.actionDestSplashToDestBottomNav())
                    }
                    SplashInitialLaunch.ON_BOARD, null -> {
                        findNavController().navigateDeepLinkWithoutOptions(
                            "fragment-dest",
                            "com.maproductions.mohamedalaa.shared.location.selection.on.board.location.selection",
                            true.toString()
                        )
                    }
                }
            }
        }
    }

}

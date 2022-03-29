package com.grand.ezkorone.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.core.extensions.fromJson
import com.grand.ezkorone.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.grand.ezkorone.core.extensions.navigateDeepLinkWithoutOptions
import com.grand.ezkorone.core.extensions.popAllBackStacks
import com.grand.ezkorone.data.local.preferences.PrefsApp
import com.grand.ezkorone.databinding.FragmentSplashBinding
import com.grand.ezkorone.domain.splash.SplashInitialLaunch
import com.grand.ezkorone.domain.utils.common.InitialDataForApp
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.location.LocationSelectionFragment
import com.grand.ezkorone.presentation.splash.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : MABaseFragment<FragmentSplashBinding>() {

    private val viewModel by viewModels<SplashViewModel>()

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

                    findNavController().popAllBackStacks()

                    findNavController().navigate(SplashFragmentDirections.actionDestSplashToDestOnBoard())
                }
            }
        }

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlow) {
            // https://www.youtube.com/watch?v=fSB6_KE95bU&t=746s
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val initialDataForApp = InitialDataForApp(
                        it.socialMedia!!,
                        it.data!!
                    )
                    prefsSplash.setInitialDataForApp(initialDataForApp)

                    delay(1_000)

                    if (findNavController().currentBackStackEntry?.savedStateHandle
                            ?.contains(LocationSelectionFragment.KEY_FRAGMENT_RESULT_LOCATION_DATA_AS_JSON) == true) {
                        return@repeatOnLifecycle
                    }

                    when (prefsSplash.getInitialLaunch().first()) {
                        SplashInitialLaunch.HOME -> {
                            findNavController().popAllBackStacks()

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

}

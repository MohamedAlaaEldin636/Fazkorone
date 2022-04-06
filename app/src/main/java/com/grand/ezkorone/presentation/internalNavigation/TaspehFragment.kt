package com.grand.ezkorone.presentation.internalNavigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.executeOnGlobalLoadingAndAutoHandleRetry
import com.grand.ezkorone.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.grand.ezkorone.databinding.FragmentQiblaBinding
import com.grand.ezkorone.databinding.FragmentTaspehBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.QiblaViewModel
import com.grand.ezkorone.presentation.internalNavigation.viewModel.TaspehViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaspehFragment : MABaseFragment<FragmentTaspehBinding>() {

    private val viewModel by viewModels<TaspehViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_taspeh

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        executeOnGlobalLoadingAndAutoHandleRetry(
            afterShowingLoading = {
                viewModel.repoTaspeh.getAzkarList(1)
            },
            afterHidingLoading = {
                viewModel.responseTaspeh.value = it
            }
        )
    }

}

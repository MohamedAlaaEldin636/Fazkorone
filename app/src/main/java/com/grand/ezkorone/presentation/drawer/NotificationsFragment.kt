package com.grand.ezkorone.presentation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentNotificationsBinding
import com.grand.ezkorone.databinding.FragmentWhoAreWeBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.drawer.viewModel.NotificationsViewModel
import com.grand.ezkorone.presentation.drawer.viewModel.WhoAreWeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : MABaseFragment<FragmentNotificationsBinding>() {

    private val viewModel by viewModels<NotificationsViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_notifications

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}

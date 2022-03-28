package com.grand.ezkorone.presentation.location

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.FragmentEditLocationBinding
import com.grand.ezkorone.databinding.FragmentHomeBinding
import com.grand.ezkorone.presentation.base.MABaseFragment
import com.grand.ezkorone.presentation.internalNavigation.viewModel.HomeViewModel
import com.grand.ezkorone.presentation.location.viewModel.EditLocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditLocationFragment : MABaseFragment<FragmentEditLocationBinding>() {

    private val viewModel by viewModels<EditLocationViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_edit_location

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // todo
        //binding?.lottieAnimationView?.playAnimation()
        binding?.lottieAnimationView?.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                // todo stop animation
                binding?.lottieAnimationView?.cancelAnimation()

                viewModel.showLottie.value = false
            }
        })
    }

}

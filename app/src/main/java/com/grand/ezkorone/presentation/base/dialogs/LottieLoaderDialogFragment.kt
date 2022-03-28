package com.grand.ezkorone.presentation.base.dialogs

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.databinding.DialogFragmentLottieLoaderBinding
import com.grand.ezkorone.presentation.main.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LottieLoaderDialogFragment : MADialogFragment<DialogFragmentLottieLoaderBinding>() {

    override val heightIsMatchParent = true
    override val canceledOnTouchOutside = false

    private val activityViewModel by activityViewModels<MainViewModel>()

    override fun getLayoutId(): Int = R.layout.dialog_fragment_lottie_loader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_FRAME, R.style.TransparentDialogTheme)
    }

    override fun onCreateDialogWindowChanges(window: Window) {
        window.setBackgroundDrawable(ColorDrawable(requireContext().getColor(android.R.color.transparent)))

        //window.attributes?.windowAnimations = R.style.ScaleDialogAnim
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel.globalLoading.observe(viewLifecycleOwner) {
            if (it == false) {
                findNavController().navigateUp()
            }
        }
    }

}

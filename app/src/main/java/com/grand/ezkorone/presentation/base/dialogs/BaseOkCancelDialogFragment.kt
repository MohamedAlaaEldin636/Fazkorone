package com.grand.ezkorone.presentation.base.dialogs

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.dpToPx
import com.grand.ezkorone.databinding.DialogFragmentBaseOkCancelBinding
import kotlin.math.roundToInt

abstract class BaseOkCancelDialogFragment : MADialogFragment<DialogFragmentBaseOkCancelBinding>() {

    final override fun getLayoutId(): Int = R.layout.dialog_fragment_base_ok_cancel

    @CallSuper
    override fun onCreateDialogWindowChanges(window: Window) {
        val drawable = InsetDrawable(
            AppCompatResources.getDrawable(requireContext(), R.drawable.border_white_16),
            requireContext().dpToPx(16f).roundToInt()
        )
        window.setBackgroundDrawable(drawable)
    }

    final override fun initializeBindingVariables() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.imageView?.isVisible = drawableRes != 0
        if (drawableRes != 0) {
            binding?.imageView?.setImageResource(drawableRes)
        }

        binding?.messageTextView?.text = message

        binding?.okMaterialButton?.text = okButtonText
        binding?.okMaterialButton?.setOnClickListener(::onOkClick)

        binding?.cancelMaterialButton?.text = cancelButtonText
        binding?.cancelMaterialButton?.setOnClickListener(::onCancelClick)
    }

    open val drawableRes: Int = 0

    abstract val okButtonText: String

    abstract val cancelButtonText: String

    abstract val message: String

    abstract fun onOkClick(view: View)

    open fun onCancelClick(view: View) {
        findNavController().navigateUp()
    }

}

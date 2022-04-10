package com.grand.ezkorone.presentation.base.dialogs

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.dpToPx
import com.grand.ezkorone.databinding.DialogFragmentGrandBinding
import kotlin.math.roundToInt

class GrandDialogFragment : MADialogFragment<DialogFragmentGrandBinding>() {

    companion object {
        private const val TAG = "com.grand.ezkorone.presentation.base.dialogs.GrandDialogFragment"

        fun show(manager: FragmentManager): GrandDialogFragment {
            val dialogFragment = GrandDialogFragment()
            dialogFragment.show(manager, TAG)
            return dialogFragment
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_fragment_grand

    @CallSuper
    override fun onCreateDialogWindowChanges(window: Window) {
        val drawable = InsetDrawable(
            AppCompatResources.getDrawable(requireContext(), R.drawable.border_white_16),
            requireContext().dpToPx(16f).roundToInt()
        )
        window.setBackgroundDrawable(drawable)
    }

}

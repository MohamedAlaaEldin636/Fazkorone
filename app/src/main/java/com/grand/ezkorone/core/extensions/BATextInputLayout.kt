package com.grand.ezkorone.core.extensions

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("textInputLayout_setEndIconOnClickListener")
fun TextInputLayout.setEndIconOnClickListenerBA(listener: View.OnClickListener) {
	setEndIconOnClickListener(listener)
}

package com.grand.ezkorone.core.extensions

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("editText_setOnEditorActionListenerBA")
fun EditText.setOnEditorActionListenerBA(listener: TextView.OnEditorActionListener) {
	setOnEditorActionListener(listener)
}

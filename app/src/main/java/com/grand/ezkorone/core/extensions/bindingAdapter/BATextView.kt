package com.grand.ezkorone.core.extensions.bindingAdapter

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("textView_setDrawableEndCompatBA")
fun TextView.setDrawableEndCompatBA(drawable: Drawable?) {
	setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
}

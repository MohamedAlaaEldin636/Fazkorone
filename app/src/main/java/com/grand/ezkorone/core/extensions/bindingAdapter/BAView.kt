package com.grand.ezkorone.core.extensions.bindingAdapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter

@BindingAdapter("view_backgroundDrawableRes")
fun View.setBackgroundDrawableResBA(@DrawableRes drawableRes: Int?) {
	background = drawableRes?.let { AppCompatResources.getDrawable(context, it) }
}

@BindingAdapter("view_setVisibleOrInvisible")
fun View.setVisibleOrInvisible(show: Boolean?) {
	visibility = if (show == true) View.VISIBLE else View.INVISIBLE
}

package com.grand.ezkorone.core.extensions.bindingAdapter

import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("circularProgressIndicator_setProgressBA")
fun CircularProgressIndicator.setProgressBA(percentage: Int?) {
    progress = percentage ?: 0
}

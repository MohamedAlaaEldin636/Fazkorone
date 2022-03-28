package com.grand.ezkorone.core.extensions

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.getMyDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(requireContext(), id)!!

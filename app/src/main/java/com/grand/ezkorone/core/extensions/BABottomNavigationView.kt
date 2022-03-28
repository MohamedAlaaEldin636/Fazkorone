package com.grand.ezkorone.core.extensions

import androidx.annotation.MenuRes
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

@BindingAdapter("bottomNavigationView_inflateMenuViaRes")
fun BottomNavigationView.inflateMenuViaRes(@MenuRes res: Int?) {
	menu.clear()
	res?.also { inflateMenu(res) }
}

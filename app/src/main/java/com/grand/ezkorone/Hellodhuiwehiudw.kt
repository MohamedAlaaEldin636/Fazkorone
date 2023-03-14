package com.grand.ezkorone

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun Fragment.abc() {
	val backstackEntry = try {
		findNavController().getBackStackEntry(R.id.nav_main)
	}catch (e: IllegalAccessException) {
		null
	}

	backstackEntry?.savedStateHandle?.set("", "")

}

package com.grand.ezkorone.core.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import com.grand.ezkorone.presentation.base.MABaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> View.executeOnGlobalLoading(afterShowingLoading: suspend () -> T, afterHidingLoading: (T) -> Unit) {
    val fragment = findFragment<MABaseFragment<*>>()

    fragment.executeOnGlobalLoading(afterShowingLoading, afterHidingLoading)
}

// todo see if log out then perform it like handle retry able isa.
fun <T> MABaseFragment<*>.executeOnGlobalLoading(afterShowingLoading: suspend () -> T, afterHidingLoading: (T) -> Unit) {
    lifecycleScope.launch {
        activityViewModel.globalLoading.value = true

        delay(150)

        val result = afterShowingLoading()

        activityViewModel.globalLoading.value = false

        delay(150)

        afterHidingLoading(result)
    }
}

fun MABaseFragment<*>.executeOnGlobalLoadingAfterShowingLoading(afterShowingLoading: suspend () -> Unit) {
    lifecycleScope.launch {
        activityViewModel.globalLoading.value = true

        delay(150)

        afterShowingLoading()

        activityViewModel.globalLoading.value = false
    }
}

package com.grand.ezkorone.core.extensions

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import com.grand.ezkorone.presentation.base.MABaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> View.executeOnGlobalLoading(afterShowingLoading: suspend () -> T, afterHidingLoading: (T) -> Unit) {
    val fragment = findFragment<MABaseFragment<*>>()

    fragment.lifecycleScope.launch {
        fragment.activityViewModel.globalLoading.value = true

        delay(150)

        val result = afterShowingLoading()

        fragment.activityViewModel.globalLoading.value = false

        delay(150)

        afterHidingLoading(result)
    }
}

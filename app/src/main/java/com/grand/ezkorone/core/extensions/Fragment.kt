package com.grand.ezkorone.core.extensions

import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.grand.ezkorone.core.customTypes.GlobalError
import com.grand.ezkorone.core.customTypes.RetryAbleFlow
import com.grand.ezkorone.core.customTypes.RetryAbleFlow2
import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.MAResult
import com.grand.ezkorone.presentation.base.MABaseFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

fun Fragment.getMyDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(requireContext(), id)!!

fun <VDB : ViewDataBinding, F : MABaseFragment<VDB>, T> F.handleRetryAbleFlowWithMustHaveResultWithNullability(
    retryAbleFlow: RetryAbleFlow<T>,
    onSuccess: (MABaseResponse<T>) -> Unit,
) {
    // Used to not keep repeating even after collection is done isa.
    var job: Job? = null
    job = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            retryAbleFlow.value.collectLatest {
                when (it) {
                    is MAResult.Loading -> {
                        activityViewModel.globalLoading.value = true
                    }
                    is MAResult.Success -> {
                        activityViewModel.globalLoading.value = false

                        onSuccess(it.value)
                    }
                    is MAResult.Failure -> {
                        Timber.e("failure is $it")

                        activityViewModel.globalLoading.value = false

                        activityViewModel.globalError.value = GlobalError.Show(it.message)

                        activityViewModel.globalError.observe(viewLifecycleOwner, object :
                            Observer<GlobalError> {
                            override fun onChanged(globalError: GlobalError?) {
                                if (globalError is GlobalError.Retry) {
                                    activityViewModel.globalError.removeObserver(this)

                                    activityViewModel.globalError.value = GlobalError.Cancel

                                    retryAbleFlow.retry()

                                    Handler(Looper.getMainLooper()).post {
                                        handleRetryAbleFlowWithMustHaveResultWithNullability(retryAbleFlow, onSuccess)
                                    }
                                }
                            }
                        })
                    }
                }
            }
            job?.cancel()
        }
    }
}

/**
 * - if [MABaseResponse.data] is not null then [onNotNullSuccess]
 */
/*fun <VDB : ViewDataBinding, F : MABaseFragment<VDB>, T> F.handleRetryAbleFlowWithMustHaveResultWithNullability(
    retryAbleFlow: RetryAbleFlow<T>,
    onNullableSuccess: (MABaseResponse<T>) -> Unit = {
        Timber.d("null success in handleRetryAbleFlowWithMustHaveResultWithNullability $it")
    },
    onNotNullSuccess: (T) -> Unit,
) {
    // Used to not keep repeating even after collection is done isa.
    var job: Job? = null
    job = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            retryAbleFlow.value.collectLatest {
                when (it) {
                    is MAResult.Loading -> {
                        activityViewModel.globalLoading.value = true
                    }
                    is MAResult.Success -> {
                        activityViewModel.globalLoading.value = false

                        it.value.data?.also { data ->
                            onNotNullSuccess(data)
                        } ?: onNullableSuccess(it.value)
                    }
                    is MAResult.Failure -> {
                        activityViewModel.globalLoading.value = false

                        activityViewModel.globalError.value = GlobalError.Show(it.message)

                        activityViewModel.globalError.observe(viewLifecycleOwner, object :
                            Observer<GlobalError> {
                            override fun onChanged(globalError: GlobalError?) {
                                if (globalError is GlobalError.Retry) {
                                    activityViewModel.globalError.removeObserver(this)

                                    activityViewModel.globalError.value = GlobalError.Cancel

                                    retryAbleFlow.retry()

                                    Handler(Looper.getMainLooper()).post {
                                        handleRetryAbleFlowWithMustHaveResultWithNullability(retryAbleFlow, onNullableSuccess, onNotNullSuccess)
                                    }
                                }
                            }
                        })
                    }
                }
            }
            job?.cancel()
        }
    }
}*/

fun <VDB : ViewDataBinding, F : MABaseFragment<VDB>, T> F.handleRetryAbleFlowWithMustHaveResultWithNullability(
    retryAbleFlow: RetryAbleFlow2<T>,
    onSuccess: (T) -> Unit,
) {
    // Used to not keep repeating even after collection is done isa.
    var job: Job? = null
    job = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            retryAbleFlow.value.collectLatest {
                when (it) {
                    is MAResult.Loading -> {
                        activityViewModel.globalLoading.value = true
                    }
                    is MAResult.Success -> {
                        activityViewModel.globalLoading.value = false

                        onSuccess(it.value)
                    }
                    is MAResult.Failure -> {
                        activityViewModel.globalLoading.value = false

                        activityViewModel.globalError.value = GlobalError.Show(it.message)

                        activityViewModel.globalError.observe(viewLifecycleOwner, object :
                            Observer<GlobalError> {
                            override fun onChanged(globalError: GlobalError?) {
                                if (globalError is GlobalError.Retry) {
                                    activityViewModel.globalError.removeObserver(this)

                                    activityViewModel.globalError.value = GlobalError.Cancel

                                    retryAbleFlow.retry()

                                    Handler(Looper.getMainLooper()).post {
                                        handleRetryAbleFlowWithMustHaveResultWithNullability(retryAbleFlow, onSuccess)
                                    }
                                }
                            }
                        })
                    }
                }
            }
            job?.cancel()
        }
    }
}

fun <T> MABaseFragment<*>.executeOnGlobalLoadingAndAutoHandleRetry(
    afterShowingLoading: suspend () -> MAResult.Immediate<MABaseResponse<T>>,
    afterHidingLoading: (T) -> Unit
) {
    lifecycleScope.launch {
        activityViewModel.globalLoading.value = true

        delay(150)

        when (val result = afterShowingLoading()) {
            is MAResult.Failure -> {
                Timber.e("failure is $result")

                activityViewModel.globalLoading.value = false

                delay(150)

                activityViewModel.globalError.value = GlobalError.Show(result.message)

                activityViewModel.globalError.observe(viewLifecycleOwner, object :
                    Observer<GlobalError> {
                    override fun onChanged(globalError: GlobalError?) {
                        if (globalError is GlobalError.Retry) {
                            activityViewModel.globalError.removeObserver(this)

                            activityViewModel.globalError.value = GlobalError.Cancel

                            Handler(Looper.getMainLooper()).post {
                                executeOnGlobalLoadingAndAutoHandleRetry(afterShowingLoading, afterHidingLoading)
                            }
                        }
                    }
                })
            }
            is MAResult.Success -> {
                activityViewModel.globalLoading.value = false

                delay(150)

                afterHidingLoading(result.value.data!!)
            }
        }
    }
}

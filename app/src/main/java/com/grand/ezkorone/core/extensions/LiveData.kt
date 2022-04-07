package com.grand.ezkorone.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.Observer
import com.grand.ezkorone.domain.utils.MAResult
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> liveDataInitialLoadingWithMinExecutionTime(
	timeInMillis: Long = 600,
	context: CoroutineContext = EmptyCoroutineContext,
	timeoutInMs: Long = 5_000L,
	block: suspend LiveDataScope<MAResult<T>>.() -> Unit
): LiveData<MAResult<T>> = androidx.lifecycle.liveData(context, timeoutInMs) {
	emit(MAResult.Loading())
	
	val current = System.currentTimeMillis()
	
	block()
	
	val remaining = timeInMillis - (System.currentTimeMillis() - current)
	if (remaining > 0) {
		delay(remaining)
	}
}

fun <T> LiveData<T?>.observeOnceUntilNotNull(owner: LifecycleOwner, action: (T) -> Unit) {
	val value = value

	if (value != null) {
		action(value)
	}else {
		val observer = object : Observer<T?> {
			override fun onChanged(newValue: T?) {
				if (newValue != null) {
					action(newValue)

					removeObserver(this)
				}
			}
		}

		observe(owner, observer)
	}
}

package com.grand.ezkorone.core.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
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

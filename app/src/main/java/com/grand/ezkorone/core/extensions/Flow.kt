package com.grand.ezkorone.core.extensions

import com.grand.ezkorone.domain.utils.MAResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

fun <T> flowInitialLoadingWithMinExecutionTime(
	timeInMillis: Long = 600,
	block: suspend FlowCollector<MAResult<T>>.() -> Unit
): Flow<MAResult<T>> {
	return flow {
		emit(MAResult.Loading())
		
		val current = System.currentTimeMillis()
		
		block()
		
		val remaining = timeInMillis - (System.currentTimeMillis() - current)
		if (remaining > 0) {
			delay(remaining)
		}
	}
}

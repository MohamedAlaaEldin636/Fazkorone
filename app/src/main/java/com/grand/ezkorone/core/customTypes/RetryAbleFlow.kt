package com.grand.ezkorone.core.customTypes

import com.grand.ezkorone.domain.utils.MABaseResponse
import com.grand.ezkorone.domain.utils.MAResult
import kotlinx.coroutines.flow.*

class RetryAbleFlow<T>(private val getFlow: () -> Flow<MAResult<MABaseResponse<T>>>) {
	
	var value: Flow<MAResult<MABaseResponse<T>>> = getFlow()
		private set
	
	fun retry() {
		value = getFlow()
	}
	
}

class RetryAbleFlow2<T>(private val getFlow: () -> Flow<MAResult<T>>) {
	
	var value: Flow<MAResult<T>> = getFlow()
		private set
	
	fun retry() {
		value = getFlow()
	}
	
}

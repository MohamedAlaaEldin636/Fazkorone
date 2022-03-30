package com.grand.ezkorone.core.customTypes

import com.grand.ezkorone.domain.utils.MAResult

class MAPagingException(val failure: MAResult.Failure<*>) : Exception(failure.message)

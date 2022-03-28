package com.grand.ezkorone.core.customTypes

sealed class GlobalError {

    data class Show(val errorMsg: String?) : GlobalError()

    object Cancel : GlobalError()

    object Retry : GlobalError()

}

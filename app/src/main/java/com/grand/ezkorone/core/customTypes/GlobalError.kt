package com.grand.ezkorone.core.customTypes

sealed class GlobalError {

    data class Show(val errorMsg: String?, val canCancelDialog: Boolean = false) : GlobalError()

    object Cancel : GlobalError()

    object Retry : GlobalError()

}

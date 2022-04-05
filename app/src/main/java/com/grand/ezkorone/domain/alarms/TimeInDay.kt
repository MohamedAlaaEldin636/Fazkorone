package com.grand.ezkorone.domain.alarms

data class TimeInDay(
    var hour24: Int,
    var minute: Int,
) {

    val hour12 get() = when {
        hour24 == 0 -> 12
        hour24 <= 12 -> hour24
        else -> hour24 - 12
    }

    val isAm get() = hour24 < 12
    val isPm get() = hour24 >= 12

}

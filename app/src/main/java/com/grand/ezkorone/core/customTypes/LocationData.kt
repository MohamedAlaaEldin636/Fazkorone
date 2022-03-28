package com.grand.ezkorone.core.customTypes

data class LocationData(
    val latitude: String,
    val longitude: String,
    val address: String,
)

fun LocationData?.orEmpty() = this?.copy() ?: LocationData("", "", "")

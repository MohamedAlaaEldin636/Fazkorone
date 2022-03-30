package com.grand.ezkorone.core.extensions

fun Int.minLengthOrPrefixZeros(length: Int): String {
    val num = toString()

    return if (num.length >= length) num else "${"0".repeat(length - num.length)}$num"
}

package com.grand.ezkorone.core.extensions

import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun LocalDateTime.toEpochMilliUTCOffset(): Long = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/*.apply {
    Timber.e("time abcde ${atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()}")
    val k = ZoneId.systemDefault().rules.getOffset(this@toEpochMilliUTCOffset)
    Timber.e("time abcdedddd $k")
    Timber.e("time abcdedddd 1 -> ${atZone(k).toInstant().toEpochMilli()}")
    Timber.e("time abcdedddd 2 -> ${toInstant(k).toEpochMilli()}")
    Timber.e("other time ${toInstant(ZoneOffset.UTC).toEpochMilli()}")
}*/
//fun LocalDateTime.toEpochMilliUTCOffset(): Long = toInstant(ZoneOffset.UTC).toEpochMilli()

fun Long.toLocalDateTimeUTCOffset(): LocalDateTime = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
//fun Long.toLocalDateTimeUTCOffset(): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)

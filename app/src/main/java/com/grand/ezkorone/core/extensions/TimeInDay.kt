package com.grand.ezkorone.core.extensions

import com.grand.ezkorone.domain.alarms.TimeInDay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

/**
 * - trigger time must be > now isa.
 */
fun TimeInDay.getTriggerTimeInMillis(): Long {
    return LocalDateTime.of(
        LocalDate.now(),
        LocalTime.of(hour24, minute)
    ).let {
        val dateTime = if (it.isBefore(LocalDateTime.now())) {
            it.plusDays(1)
        }else {
            it
        }

        dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    }
}

package com.grand.ezkorone.domain.salah

import java.time.LocalDate

/**
 * @param month from 1 to 12
 */
data class SimpleDate(
    val dayOfMonth: Int,
    val month: Int,
    val year: Int
)

fun LocalDate.toSimpleDate() = SimpleDate(dayOfMonth, monthValue, year)

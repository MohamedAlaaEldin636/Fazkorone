package com.grand.ezkorone.domain.azan

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.minLengthOrPrefixZeros
import com.grand.ezkorone.domain.alarms.TimeInDay
import com.grand.ezkorone.domain.salah.SalahFardType

/**
 * "timings": {
"Fajr": "04:20",
"Sunrise": "05:48",
"Dhuhr": "11:59",
"Asr": "15:29",
"Sunset": "18:11",
"Maghrib": "18:11",
"Isha": "19:30",
"Imsak": "04:10",
"Midnight": "00:00"
},
 */
data class SalawatTimes(
    @SerializedName("Fajr") val fajr: String,
    @SerializedName("Dhuhr") val dohr: String,
    @SerializedName("Asr") val asr: String,
    @SerializedName("Maghrib") val maghrep: String,
    @SerializedName("Isha") val esha: String,
) {

    val fajrHour get() = getHour(fajr)
    val fajrMinutes get() = getMinutes(fajr)
    val fajrTimeInDay get() = TimeInDay(fajrHour, fajrMinutes)

    val dohrHour get() = getHour(dohr)
    val dohrMinutes get() = getMinutes(dohr)
    val dohrTimeInDay get() = TimeInDay(dohrHour, dohrMinutes)

    val asrHour get() = getHour(asr)
    val asrMinutes get() = getMinutes(asr)
    val asrTimeInDay get() = TimeInDay(asrHour, asrMinutes)

    val maghrepHour get() = getHour(maghrep)
    val maghrepMinutes get() = getMinutes(maghrep)
    val maghrepTimeInDay get() = TimeInDay(maghrepHour, maghrepMinutes)

    val eshaHour get() = getHour(esha)
    val eshaMinutes get() = getMinutes(esha)
    val eshaTimeInDay get() = TimeInDay(eshaHour, eshaMinutes)

    fun getTimeInDayOf(salahFardType: SalahFardType): TimeInDay {
        return when (salahFardType) {
            SalahFardType.FAGR -> fajrTimeInDay
            SalahFardType.DOHR -> dohrTimeInDay
            SalahFardType.ASR -> asrTimeInDay
            SalahFardType.MAGHREP -> maghrepTimeInDay
            SalahFardType.ESHA -> eshaTimeInDay
        }
    }

    fun getFajrSalahTimeFormat(context: Context): String =
        "$fajrHour:$fajrMinutes ${context.getIsAmOrPm(fajrHour)}"
    fun getFajrSalahTimeFormat12(context: Context): String = TimeInDay(fajrHour, fajrMinutes).let {
        "${it.hour12}:${it.minute.minLengthOrPrefixZeros(2)} ${context.getIsAmOrPm(fajrHour)}"
    }

    fun getDohrSalahTimeFormat(context: Context): String =
        "$dohrHour:$dohrMinutes ${context.getIsAmOrPm(dohrHour)}"
    fun getDohrSalahTimeFormat12(context: Context): String = TimeInDay(dohrHour, dohrMinutes).let {
        "${it.hour12}:${it.minute.minLengthOrPrefixZeros(2)} ${context.getIsAmOrPm(dohrHour)}"
    }

    fun getAsrSalahTimeFormat(context: Context): String =
        "$asrHour:$asrMinutes ${context.getIsAmOrPm(asrHour)}"
    fun getAsrSalahTimeFormat12(context: Context): String = TimeInDay(asrHour, asrMinutes).let {
        "${it.hour12}:${it.minute.minLengthOrPrefixZeros(2)} ${context.getIsAmOrPm(asrHour)}"
    }

    fun getMaghrepSalahTimeFormat(context: Context): String =
        "$maghrepHour:$maghrepMinutes ${context.getIsAmOrPm(maghrepHour)}"
    fun getMaghrepSalahTimeFormat12(context: Context): String = TimeInDay(maghrepHour, maghrepMinutes).let {
        "${it.hour12}:${it.minute.minLengthOrPrefixZeros(2)} ${context.getIsAmOrPm(maghrepHour)}"
    }

    fun getEshaSalahTimeFormat(context: Context): String =
        "$eshaHour:$eshaMinutes ${context.getIsAmOrPm(eshaHour)}"
    fun getEshaSalahTimeFormat12(context: Context): String = TimeInDay(eshaHour, eshaMinutes).let {
        "${it.hour12}:${it.minute.minLengthOrPrefixZeros(2)} ${context.getIsAmOrPm(eshaHour)}"
    }

    private fun Context.getIsAmOrPm(hour: Int) = when {
        hour < 12 -> getString(R.string.am)
        else -> getString(R.string.pm)
    }

    /*
            /*
        "timings": {
            "Fajr": "04:20",
            "Sunrise": "05:48",
            "Dhuhr": "11:59",
            "Asr": "15:29",
            "Sunset": "18:11",
            "Maghrib": "18:11",
            "Isha": "19:30",
            "Imsak": "04:10",
            "Midnight": "00:00"
        },
         */

     */
    fun getNextHourAndMinutesAndRemainingTimeAsHourAndMinutesWithType(
        hour: Int,
        minutes: Int,
        seconds: Int,
        tomorrowSalawatTimes: SalawatTimes
    ): Calculations {
        return when {
            fajrHour > hour || fajrHour == hour && fajrMinutes > minutes -> {
                Calculations(fajrHour, fajrMinutes, fajrHour - hour, fajrMinutes - minutes, Type.FAJR, seconds)
            }
            dohrHour > hour || dohrHour == hour && dohrMinutes > minutes -> {
                Calculations(dohrHour, dohrMinutes, dohrHour - hour, dohrMinutes - minutes, Type.DOHR, seconds)
            }
            asrHour > hour || asrHour == hour && asrMinutes > minutes -> {
                Calculations(asrHour, asrMinutes, asrHour - hour, asrMinutes - minutes, Type.ASR, seconds)
            }
            maghrepHour > hour || maghrepHour == hour && maghrepMinutes > minutes -> {
                Calculations(maghrepHour, maghrepMinutes, maghrepHour - hour, maghrepMinutes - minutes, Type.MAGHREP, seconds)
            }
            eshaHour > hour || eshaHour == hour && eshaMinutes > minutes -> {
                Calculations(eshaHour, eshaMinutes, eshaHour - hour, eshaMinutes - minutes, Type.ESHA, seconds)
            }
            else -> {
                Calculations(
                    tomorrowSalawatTimes.fajrHour, tomorrowSalawatTimes.fajrMinutes,
                    (24 - hour) + tomorrowSalawatTimes.fajrHour,
                    (0 - minutes) + tomorrowSalawatTimes.eshaMinutes,
                    Type.FAJR,
                    seconds
                )
            }
        }
    }

    private fun getHour(string: String): Int = string.split(":")[0].toInt()
    private fun getMinutes(string: String): Int = string.split(":")[1].toInt()

    enum class Type {
        FAJR, DOHR, ASR, MAGHREP, ESHA
    }

    //infix fun <A, B, C> Pair<A, B>.triple(third: C): Triple<A, B, C> = Triple(first, second, third)

    data class Calculations(
        private val nextHour: Int,
        private val nextMinutes: Int,
        private val remainingHour: Int,
        private val remainingMinutes: Int,
        val type: Type,
        /** seconds of current time so subtract from minutes or hours isa. */
        private val seconds: Int,
    ) {

        fun getNextTime(context: Context): String {
            return "${getTypeName(context)} : $nextHour:${nextMinutes.minLengthOrPrefixZeros(2)} ${getIsAmOrPm(context)}"
        }

        fun getRemainingTime(): String {
            val (remainingHour, remainingMinutes) = if (remainingMinutes < 0) {
                remainingHour.dec() to (60 + remainingMinutes)
            }else {
                remainingHour to remainingMinutes
            }

            val (hr, min, sec) = when {
                seconds == 0 || seconds == 60 -> Triple(remainingHour, remainingMinutes, seconds)
                remainingMinutes == 0 -> Triple(remainingHour.dec(), 59, 60 - seconds)
                else -> Triple(remainingHour, remainingMinutes.dec(), 60 - seconds)
            }

            return "(${hr.minLengthOrPrefixZeros(2)}:${min.minLengthOrPrefixZeros(2)}:${sec.minLengthOrPrefixZeros(2)})"
        }

        private fun getTypeName(context: Context) = when (type) {
            Type.FAJR -> context.getString(R.string.fagr)
            Type.DOHR -> context.getString(R.string.dohr)
            Type.ASR -> context.getString(R.string.asr)
            Type.MAGHREP -> context.getString(R.string.maghrep)
            Type.ESHA -> context.getString(R.string.esha)
        }

        private fun getIsAmOrPm(context: Context) = when {
            nextHour < 12 -> context.getString(R.string.am)
            else -> context.getString(R.string.pm)
        }

    }

}

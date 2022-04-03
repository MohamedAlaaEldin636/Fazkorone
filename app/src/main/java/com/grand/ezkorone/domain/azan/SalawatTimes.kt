package com.grand.ezkorone.domain.azan

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.minLengthOrPrefixZeros

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

    val dohrHour get() = getHour(dohr)
    val dohrMinutes get() = getMinutes(dohr)

    val asrHour get() = getHour(asr)
    val asrMinutes get() = getMinutes(asr)

    val maghrepHour get() = getHour(maghrep)
    val maghrepMinutes get() = getMinutes(maghrep)

    val eshaHour get() = getHour(esha)
    val eshaMinutes get() = getMinutes(esha)

    fun getFajrSalahTimeFormat(context: Context): String =
        "$fajrHour:$fajrMinutes ${context.getIsAmOrPm(fajrHour)}"

    fun getDohrSalahTimeFormat(context: Context): String =
        "$dohrHour:$dohrMinutes ${context.getIsAmOrPm(dohrHour)}"

    fun getAsrSalahTimeFormat(context: Context): String =
        "$asrHour:$asrMinutes ${context.getIsAmOrPm(asrHour)}"

    fun getMaghrepSalahTimeFormat(context: Context): String =
        "$maghrepHour:$maghrepMinutes ${context.getIsAmOrPm(maghrepHour)}"

    fun getEshaSalahTimeFormat(context: Context): String =
        "$eshaHour:$eshaMinutes ${context.getIsAmOrPm(eshaHour)}"

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
    fun getNextHourAndMinutesAndRemainingTimeAsHourAndMinutesWithType(hour: Int, minutes: Int, tomorrowSalawatTimes: SalawatTimes): Calculations {
        return when {
            fajrHour > hour || fajrHour == hour && fajrMinutes > minutes -> {
                Calculations(fajrHour, fajrMinutes, fajrHour - hour, fajrMinutes - minutes, Type.FAJR)
            }
            dohrHour > hour || dohrHour == hour && dohrMinutes > minutes -> {
                Calculations(dohrHour, dohrMinutes, dohrHour - hour, dohrMinutes - minutes, Type.DOHR)
            }
            asrHour > hour || asrHour == hour && asrMinutes > minutes -> {
                Calculations(asrHour, asrMinutes, asrHour - hour, asrMinutes - minutes, Type.ASR)
            }
            maghrepHour > hour || maghrepHour == hour && maghrepMinutes > minutes -> {
                Calculations(maghrepHour, maghrepMinutes, maghrepHour - hour, maghrepMinutes - minutes, Type.MAGHREP)
            }
            eshaHour > hour || eshaHour == hour && eshaMinutes > minutes -> {
                Calculations(eshaHour, eshaMinutes, eshaHour - hour, eshaMinutes - minutes, Type.ESHA)
            }
            else -> {
                Calculations(
                    tomorrowSalawatTimes.fajrHour, tomorrowSalawatTimes.fajrMinutes,
                    (24 - hour) + tomorrowSalawatTimes.fajrHour,
                    (0 - minutes) + tomorrowSalawatTimes.eshaMinutes,
                    Type.FAJR
                )
            }
        }
    }

    private fun getHour(string: String): Int = string.split(":")[0].toInt()
    private fun getMinutes(string: String): Int = string.split(":")[1].toInt()

    enum class Type {
        FAJR, DOHR, ASR, MAGHREP, ESHA
    }

    data class Calculations(
        private val nextHour: Int,
        private val nextMinutes: Int,
        private val remainingHour: Int,
        private val remainingMinutes: Int,
        val type: Type,
    ) {

        fun getNextTime(context: Context): String {
            return "${getTypeName(context)} : $nextHour:$nextMinutes ${getIsAmOrPm(context)}"
        }

        fun getRemainingTime(): String {
            val (remainingHour, remainingMinutes) = if (remainingMinutes < 0) {
                remainingHour.dec() to (60 + remainingMinutes)
            }else {
                remainingHour to remainingMinutes
            }

            return "(${remainingHour.minLengthOrPrefixZeros(2)}:${remainingMinutes.minLengthOrPrefixZeros(2)})"
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

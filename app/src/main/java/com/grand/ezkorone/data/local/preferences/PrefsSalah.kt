package com.grand.ezkorone.data.local.preferences

import android.content.Context
import com.google.gson.Gson
import com.grand.ezkorone.core.customTypes.LocationData
import com.grand.ezkorone.domain.salah.SalahFardType
import com.grand.ezkorone.domain.salah.SalahFardType.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsSalah @Inject constructor(
    @ApplicationContext context: Context,
    gson: Gson,
) : PrefsBase(context, gson, "PREFS_SALAH") {

    companion object {
        private const val KEY_CAN_RING_FAJR = "KEY_CAN_RING_FAJR"
        private const val KEY_CAN_RING_DOHR = "KEY_CAN_RING_DOHR"
        private const val KEY_CAN_RING_ASR = "KEY_CAN_RING_ASR"
        private const val KEY_CAN_RING_MAGHREP = "KEY_CAN_RING_MAGHREP"
        private const val KEY_CAN_RING_ESHA = "KEY_CAN_RING_ESHA"

        private const val KEY_FAJR_NOTIFICATION_SOUND_URI = "KEY_FAJR_NOTIFICATION_SOUND_URI"
        private const val KEY_DOHR_NOTIFICATION_SOUND_URI = "KEY_DOHR_NOTIFICATION_SOUND_URI"
        private const val KEY_ASR_NOTIFICATION_SOUND_URI = "KEY_ASR_NOTIFICATION_SOUND_URI"
        private const val KEY_MAGHREP_NOTIFICATION_SOUND_URI = "KEY_MAGHREP_NOTIFICATION_SOUND_URI"
        private const val KEY_ESHA_NOTIFICATION_SOUND_URI = "KEY_ESHA_NOTIFICATION_SOUND_URI"
    }

    suspend fun setFajrNotificationSoundUri(uri: String?) =
        setStringValue(KEY_FAJR_NOTIFICATION_SOUND_URI, uri)
    fun getFajrNotificationSoundUri() = getStringValue(KEY_FAJR_NOTIFICATION_SOUND_URI)

    suspend fun setDohrNotificationSoundUri(uri: String?) =
        setStringValue(KEY_DOHR_NOTIFICATION_SOUND_URI, uri)
    fun getDohrNotificationSoundUri() = getStringValue(KEY_DOHR_NOTIFICATION_SOUND_URI)

    suspend fun setAsrNotificationSoundUri(uri: String?) =
        setStringValue(KEY_ASR_NOTIFICATION_SOUND_URI, uri)
    fun getAsrNotificationSoundUri() = getStringValue(KEY_ASR_NOTIFICATION_SOUND_URI)

    suspend fun setMaghrepNotificationSoundUri(uri: String?) =
        setStringValue(KEY_MAGHREP_NOTIFICATION_SOUND_URI, uri)
    fun getMaghrepNotificationSoundUri() = getStringValue(KEY_FAJR_NOTIFICATION_SOUND_URI)

    suspend fun setEshaNotificationSoundUri(uri: String?) =
        setStringValue(KEY_ESHA_NOTIFICATION_SOUND_URI, uri)
    fun getEshaNotificationSoundUri() = getStringValue(KEY_ESHA_NOTIFICATION_SOUND_URI)

    suspend fun setSalahFardTypeNotificationSoundUri(type: SalahFardType, uri: String?) = when (type) {
        FAGR -> setFajrNotificationSoundUri(uri)
        DOHR -> setDohrNotificationSoundUri(uri)
        ASR -> setAsrNotificationSoundUri(uri)
        MAGHREP -> setMaghrepNotificationSoundUri(uri)
        ESHA -> setEshaNotificationSoundUri(uri)
    }
    suspend fun getSalahFardTypeNotificationSoundUri(type: SalahFardType): String? = when (type) {
        FAGR -> getFajrNotificationSoundUri().first()
        DOHR -> getDohrNotificationSoundUri().first()
        ASR -> getAsrNotificationSoundUri().first()
        MAGHREP -> getMaghrepNotificationSoundUri().first()
        ESHA -> getEshaNotificationSoundUri().first()
    }

    suspend fun setSalawatNotificationDownloadManagerId(type: SalahFardType, idOfDownloadManager: Long?) =
        setLongValue(type.name, idOfDownloadManager)
    fun getSalawatNotificationDownloadManagerId(type: SalahFardType) = getLongValue(type.name)

    suspend fun setSalahFardTypeNotificationDownloadManagerId(
        type: SalahFardType, idOfSheikh: Int, idOfDownloadManager: Long
    ) = setLongValue(getKeyOfSalahFardTypeNotificationDownloadManagerId(type, idOfSheikh), idOfDownloadManager)
    fun getSalahFardTypeNotificationDownloadManagerId(
        type: SalahFardType, idOfSheikh: Int
    ) = getLongValue(getKeyOfSalahFardTypeNotificationDownloadManagerId(type, idOfSheikh))
    private fun getKeyOfSalahFardTypeNotificationDownloadManagerId(type: SalahFardType, idOfSheikh: Int) = "${type.name}_$idOfSheikh"

    suspend fun setCanRingFajr(canRing: Boolean?) =
        setBooleanValue(KEY_CAN_RING_FAJR, canRing)
    fun getCanRingFajr() = getBooleanValue(KEY_CAN_RING_FAJR).map { it ?: false }

    suspend fun setCanRingDohr(canRing: Boolean?) =
        setBooleanValue(KEY_CAN_RING_DOHR, canRing)
    fun getCanRingDohr() = getBooleanValue(KEY_CAN_RING_DOHR).map { it ?: false }

    suspend fun setCanRingAsr(canRing: Boolean?) =
        setBooleanValue(KEY_CAN_RING_ASR, canRing)
    fun getCanRingAsr() = getBooleanValue(KEY_CAN_RING_ASR).map { it ?: false }

    suspend fun setCanRingMaghrep(canRing: Boolean?) =
        setBooleanValue(KEY_CAN_RING_MAGHREP, canRing)
    fun getCanRingMaghrep() = getBooleanValue(KEY_CAN_RING_MAGHREP).map { it ?: false }

    suspend fun setCanRingEsha(canRing: Boolean?) =
        setBooleanValue(KEY_CAN_RING_ESHA, canRing)
    fun getCanRingEsha() = getBooleanValue(KEY_CAN_RING_ESHA).map { it ?: false }

}

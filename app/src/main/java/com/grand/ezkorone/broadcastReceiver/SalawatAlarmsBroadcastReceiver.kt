package com.grand.ezkorone.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import androidx.work.*
import com.grand.ezkorone.core.customTypes.NotificationUtils
import com.grand.ezkorone.core.extensions.toEpochMilliUTCOffset
import com.grand.ezkorone.core.extensions.toLocalDateTimeUTCOffset
import com.grand.ezkorone.data.local.preferences.PrefsSalah
import com.grand.ezkorone.domain.salah.SalahFardType
import com.grand.ezkorone.presentation.main.MainActivity
import com.grand.ezkorone.workManager.SalawatAlarmsWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.grand.ezkorone.R
import com.grand.ezkorone.services.MediaForegroundService

@AndroidEntryPoint
class SalawatAlarmsBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val KEY_TRIGGER_TIME_IN_MILLIS = "KEY_TRIGGER_TIME_IN_MILLIS"

        const val KEY_ID_OF_DOWNLOAD_MANAGER = "KEY_ID_OF_DOWNLOAD_MANAGER"

        /**
         * - schedule alarm with tag then initial delay work manager of firing time + 1 minute
         * and work manager will use same tag again which will override what happened in alarm
         * manager, but ensure initial delay occurs correctly for work manager isa.
         */
        fun scheduleAlarmManagerAndWorkManager(context: Context, localDateTime: LocalDateTime, salahFardType: SalahFardType, idOfDownloadManager: Long?) {
            // to-do del below code later isa.
            /*Timber.e("actual date ${localDateTime2.dayOfMonth} / ${localDateTime2.monthValue} / ${localDateTime2.year}")
            Timber.e("actual time ${localDateTime2.hour} : ${localDateTime2.minute}")
            val localDateTime = LocalDateTime.now().plusMinutes(1)*/

            Timber.e("date ${localDateTime.dayOfMonth} / ${localDateTime.monthValue} / ${localDateTime.year}")
            Timber.e("time ${localDateTime.hour} : ${localDateTime.minute}")
            Timber.e("idOfDownloadManager $idOfDownloadManager")

            val action = salahFardType.name

            val triggerTimeInMillis = localDateTime.toEpochMilliUTCOffset()

            scheduleAlarmManagerOnly(context, localDateTime, action, idOfDownloadManager)

            val initialDelayInMillis = triggerTimeInMillis - LocalDateTime.now()
                .toEpochMilliUTCOffset() + TimeUnit.MINUTES.toMillis(1)

            performScheduleWorkManagerOnly(
                context,
                initialDelayInMillis,
                action,
                localDateTime.plusDays(1),
                idOfDownloadManager
            )
        }

        fun scheduleWorkManagerOnly(context: Context, triggerDateTime: LocalDateTime, salahFardType: SalahFardType, idOfDownloadManager: Long?) {
            Timber.e("in scheduleWorkManagerOnly -> date ${triggerDateTime.dayOfMonth} / ${triggerDateTime.monthValue} / ${triggerDateTime.year}")
            Timber.e("in scheduleWorkManagerOnly -> time ${triggerDateTime.hour} : ${triggerDateTime.minute}")
            Timber.e("in scheduleWorkManagerOnly -> idOfDownloadManager $idOfDownloadManager")

            val action = salahFardType.name

            val initialDelayAtTime = triggerDateTime.minusDays(1)
            val initialDelayInMillis = initialDelayAtTime.toEpochMilliUTCOffset() -
                LocalDateTime.now().toEpochMilliUTCOffset() + TimeUnit.MINUTES.toMillis(1)

            performScheduleWorkManagerOnly(
                context,
                initialDelayInMillis,
                action,
                triggerDateTime,
                idOfDownloadManager
            )
        }

        private fun performScheduleWorkManagerOnly(
            context: Context,
            initialDelayInMillis: Long,
            tag: String,
            inputDataTriggerDateTime: LocalDateTime,
            idOfDownloadManager: Long?
        ) {
            val workRequest = OneTimeWorkRequestBuilder<SalawatAlarmsWorker>()
                .setInitialDelay(initialDelayInMillis, TimeUnit.MILLISECONDS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setInputData(
                    workDataOf(
                        Pair(
                            SalawatAlarmsWorker.KEY_INPUT_DATA_TRIGGER_TIME_MILLIS,
                            inputDataTriggerDateTime.toEpochMilliUTCOffset()
                        ),
                        Pair(
                            SalawatAlarmsWorker.KEY_INPUT_DATA_TRIGGER_TAG,
                            tag
                        ),
                        Pair(
                            SalawatAlarmsWorker.KEY_INPUT_DATA_ID_OF_DOWNLOAD_MANAGER,
                            idOfDownloadManager
                        )
                    )
                )
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .addTag(tag)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                SalawatAlarmsWorker.UNIQUE_NAME,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                workRequest
            )
        }

        private fun scheduleAlarmManagerOnly(context: Context, localDateTime: LocalDateTime, action: String, idOfDownloadManager: Long?) {
            val alarmManager = context.getSystemService<AlarmManager>() ?: return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Timber.e("canScheduleExactAlarms ${alarmManager.canScheduleExactAlarms()}")
            }

            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val showOrEditIntent = PendingIntent.getActivity(
                context,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerTimeInMillis = localDateTime.toEpochMilliUTCOffset()

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    triggerTimeInMillis,
                    showOrEditIntent
                ),
                getOperationPendingIntent(context, action, triggerTimeInMillis, idOfDownloadManager)
            )
        }

        fun cancelAlarmManagerAndWorkManager(context: Context, salahFardType: SalahFardType) {
            val tag = salahFardType.name

            val alarmManager = context.getSystemService<AlarmManager>() ?: return

            WorkManager.getInstance(context).cancelAllWorkByTag(tag)

            alarmManager.cancel(getOperationPendingIntent(context, tag))
        }

        private fun getOperationPendingIntent(context: Context, action: String, triggerTimeInMillis: Long? = null, idOfDownloadManager: Long? = null): PendingIntent {
            val intent = Intent(context, SalawatAlarmsBroadcastReceiver::class.java)
            intent.setClass(context, SalawatAlarmsBroadcastReceiver::class.java)
            intent.action = action
            intent.data = Uri.Builder().scheme("my-app-s2").authority("com.grand.ezkorone.salawat.broadcastReceiver").build()
            intent.putExtra(KEY_TRIGGER_TIME_IN_MILLIS, triggerTimeInMillis)
            intent.putExtra(KEY_ID_OF_DOWNLOAD_MANAGER, idOfDownloadManager)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            return PendingIntent.getBroadcast(
                context.applicationContext,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    @Inject
    lateinit var prefsSalah: PrefsSalah

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.e("TAG TIRJOTRO -> SalawatAlarmsBroadcastReceiver")

        if (context == null || intent == null) return

        val triggerTimeInMillis = intent.getLongExtra(KEY_TRIGGER_TIME_IN_MILLIS, 0)
        val name = intent.action.orEmpty()

        Timber.e("triggerTimeInMillis $triggerTimeInMillis")

        val triggerDateTime = triggerTimeInMillis.toLocalDateTimeUTCOffset()

        val salahFardType = SalahFardType.valueOf(name)

        val temp = runBlocking {
            prefsSalah.getSalawatNotificationDownloadManagerId(salahFardType).first()
        }

        val idOfDownloadManager = temp ?: (if (intent.hasExtra(KEY_ID_OF_DOWNLOAD_MANAGER)) {
            intent.getLongExtra(KEY_ID_OF_DOWNLOAD_MANAGER, 0)
        }else {
            null
        })
        scheduleAlarmManagerOnly(context, triggerDateTime.plusDays(1), name, idOfDownloadManager)

        val notificationBody = when (salahFardType) {
            SalahFardType.FAGR -> context.getString(R.string.fagr)
            SalahFardType.DOHR -> context.getString(R.string.dohr)
            SalahFardType.ASR -> context.getString(R.string.asr)
            SalahFardType.MAGHREP -> context.getString(R.string.maghrep)
            SalahFardType.ESHA -> context.getString(R.string.esha)
        }

        Timber.e("showNotificationToLaunchMainActivityForSalawat 0000 idOfDownloadManager -> $idOfDownloadManager")

        MediaForegroundService.launch(
            context.applicationContext,
            idOfDownloadManager,
            notificationBody
        )
        /*NotificationUtils.showNotificationToLaunchMainActivityForSalawat(
            context.applicationContext,
            notificationBody,
            idOfDownloadManager
        )*/
    }
}

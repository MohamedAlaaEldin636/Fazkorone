package com.grand.ezkorone.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
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

@AndroidEntryPoint
class SalawatAlarmsBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val KEY_TRIGGER_TIME_IN_MILLIS = "KEY_TRIGGER_TIME_IN_MILLIS"

        /**
         * - schedule alarm with tag then initial delay work manager of firing time + 1 minute
         * and work manager will use same tag again which will override what happened in alarm
         * manager, but ensure initial delay occurs correctly for work manager isa.
         */
        fun scheduleAlarmManagerAndWorkManager(context: Context, localDateTime2: LocalDateTime, salahFardType: SalahFardType) {
            // todo del below code later isa.
            val localDateTime = LocalDateTime.now().plusMinutes(1)

            Timber.e("date ${localDateTime.monthValue} / ${localDateTime.year}")
            Timber.e("time ${localDateTime.hour} : ${localDateTime.minute}")

            val action = salahFardType.name

            val triggerTimeInMillis = localDateTime.toEpochMilliUTCOffset()

            scheduleAlarmManagerOnly(context, localDateTime, action)

            val initialDelayInMillis = triggerTimeInMillis - LocalDateTime.now()
                .toEpochMilliUTCOffset() + TimeUnit.MINUTES.toMillis(1)

            performScheduleWorkManagerOnly(
                context,
                initialDelayInMillis,
                action,
                localDateTime.plusDays(1)
            )
        }

        fun scheduleWorkManagerOnly(context: Context, triggerDateTime: LocalDateTime, salahFardType: SalahFardType) {
            val action = salahFardType.name

            val initialDelayAtTime = triggerDateTime.minusDays(1)
            val initialDelayInMillis = initialDelayAtTime.toEpochMilliUTCOffset() -
                LocalDateTime.now().toEpochMilliUTCOffset() + TimeUnit.MINUTES.toMillis(1)

            performScheduleWorkManagerOnly(
                context,
                initialDelayInMillis,
                action,
                triggerDateTime
            )
        }

        private fun performScheduleWorkManagerOnly(
            context: Context,
            initialDelayInMillis: Long,
            tag: String,
            inputDataTriggerDateTime: LocalDateTime
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

        private fun scheduleAlarmManagerOnly(context: Context, localDateTime: LocalDateTime, action: String) {
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
                getOperationPendingIntent(context, action, triggerTimeInMillis)
            )
        }

        fun cancelAlarmManagerAndWorkManager(context: Context, salahFardType: SalahFardType) {
            val tag = salahFardType.name

            val alarmManager = context.getSystemService<AlarmManager>() ?: return

            WorkManager.getInstance(context).cancelAllWorkByTag(tag)

            alarmManager.cancel(getOperationPendingIntent(context, tag))
        }

        private fun getOperationPendingIntent(context: Context, action: String, triggerTimeInMillis: Long? = null): PendingIntent {
            val intent = Intent(context, AlarmsBroadcastReceiver::class.java)
            intent.setClass(context, AlarmsBroadcastReceiver::class.java)
            intent.action = action
            intent.data = Uri.Builder().scheme("my-app").authority("com.grand.ezkorone.salawat.broadcastReceiver").build()
            intent.putExtra(KEY_TRIGGER_TIME_IN_MILLIS, triggerTimeInMillis)
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

        scheduleAlarmManagerOnly(context, triggerDateTime.plusDays(1), name)

        val uri = runBlocking {
            when (SalahFardType.valueOf(name)) {
                SalahFardType.FAGR -> prefsSalah.getFajrNotificationSoundUri()
                SalahFardType.DOHR -> prefsSalah.getDohrNotificationSoundUri()
                SalahFardType.ASR -> prefsSalah.getAsrNotificationSoundUri()
                SalahFardType.MAGHREP -> prefsSalah.getMaghrepNotificationSoundUri()
                SalahFardType.ESHA -> prefsSalah.getEshaNotificationSoundUri()
            }.first()
        }

        NotificationUtils.showNotificationToLaunchMainActivityForSalawat(
            context.applicationContext,
            name,
            if (uri.isNullOrEmpty()) null else Uri.parse(uri)
        )
    }
}

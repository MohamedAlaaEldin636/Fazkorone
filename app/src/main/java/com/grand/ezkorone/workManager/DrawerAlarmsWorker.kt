package com.grand.ezkorone.workManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.getBroadcast
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.*
import com.grand.ezkorone.R
import com.grand.ezkorone.broadcastReceiver.AlarmsBroadcastReceiver
import com.grand.ezkorone.core.extensions.toEpochMilliUTCOffset
import com.grand.ezkorone.core.extensions.toLocalDateTimeUTCOffset
import com.grand.ezkorone.presentation.main.MainActivity
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class DrawerAlarmsWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(
    appContext,
    workerParams
) {

    // todo na2es enque worker + make below private plus test them ba2a isa. + make ones as well in salah screen as well isa.
    /*companion object {
        const val KEY_INPUT_DATA_TRIGGER_TIME_MILLIS = "KEY_INPUT_DATA_TRIGGER_TIME_MILLIS"
        const val KEY_INPUT_DATA_TRIGGER_TAG = "KEY_INPUT_DATA_TRIGGER_TAG"

        fun scheduleWorkManager(context: Context, initialDelayInHours: Long, name: String, triggerTimeInMillis: Long) {
            val workRequest = PeriodicWorkRequestBuilder<DrawerAlarmsWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelayInHours, TimeUnit.HOURS)
                .setInputData(
                    workDataOf(
                    Pair(
                        KEY_INPUT_DATA_TRIGGER_TIME_MILLIS,
                        triggerTimeInMillis
                    ),
                    Pair(
                        KEY_INPUT_DATA_TRIGGER_TAG,
                        name
                    ),
                )
                )
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .addTag(name)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }

        fun scheduleAlarmManager(context: Context, localDateTime: LocalDateTime, action: String) {
            //Timber.e("time is actually ${localDateTime.hour} ${localDateTime.minute}")

            val alarmManager = context.getSystemService<AlarmManager>() ?: return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Timber.e("can ${alarmManager.canScheduleExactAlarms()}")
            }else {
                Timber.e("can hiiiiiiiii")
            }

            // todo handle both reboots and api 31 permission checks isa.
            *//*val am: AlarmManager
            am.setAlarmClock()
            am.canScheduleExactAlarms()
            //Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            // also a broadcast receiver FOR -> https://developer.android.com/training/scheduling/alarms#exact-permission-check

            am.cancel(intent)*//*

            // keep alarms to survive restarts isa.
            // https://developer.android.com/training/scheduling/alarms#boot

            // todo better just launch activity dest_splash but with arguments to launch alarms directly
            //  and to ensure splash called api to get drawer value isa.
            val showOrEditIntent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_main)
                .addDestination(R.id.dest_alarms)
                .createPendingIntent()

            val triggerTimeInMillis = localDateTime.toEpochMilliUTCOffset()

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    triggerTimeInMillis,
                    showOrEditIntent
                ),
                getOperationPendingIntent(context, action, triggerTimeInMillis)
            )
        }

        fun cancelWorkManagerAndAlarmManager(context: Context, tag: String) {
            val alarmManager = context.getSystemService<AlarmManager>() ?: return

            WorkManager.getInstance(context).cancelAllWorkByTag(tag)

            //action, data, type, class, and categories todo
            alarmManager.cancel(getOperationPendingIntent(context, tag))
        }

        private fun getOperationPendingIntent(context: Context, action: String, triggerTimeInMillis: Long? = null): PendingIntent {
            val intent = Intent(context, AlarmsBroadcastReceiver::class.java)
            intent.setClass(context, AlarmsBroadcastReceiver::class.java)
            intent.action = action
            intent.data = Uri.Builder().scheme("my-app").authority("com.grand.ezkorone.broadcastReceiver").build()
            intent.putExtra(AlarmsBroadcastReceiver.KEY_TRIGGER_TIME_IN_MILLIS, triggerTimeInMillis)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            return getBroadcast(
                context.applicationContext,
                0 *//* Request code *//*,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            *//*return NavDeepLinkBuilder(requireContext())
                .setGraph(R.navigation.nav_main)
                .setDestination(R.id.dest_splash)
                .createPendingIntent()*//*
        }
    }*/

    override suspend fun doWork(): Result {
        /*val triggerMillis = inputData.getLong(KEY_INPUT_DATA_TRIGGER_TIME_MILLIS, -1)
        val triggerTag = inputData.getString(KEY_INPUT_DATA_TRIGGER_TAG).orEmpty()

        val triggerDateTime = triggerMillis.toLocalDateTimeUTCOffset()

        // todo why work manager doesn't get the delay the initial delay isa ?
        scheduleAlarmManager(applicationContext, triggerDateTime, triggerTag)*/

        // todo e3mel enque b el trigger.plus 1 day isa.
        //  aw better from the broadcast w efkes le da asln isa. bs efred not fired isa.

        return Result.success()
    }

}

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
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.WorkManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.customTypes.NotificationUtils
import com.grand.ezkorone.core.extensions.toEpochMilliUTCOffset
import com.grand.ezkorone.core.extensions.toLocalDateTimeUTCOffset
import com.grand.ezkorone.workManager.DrawerAlarmsWorker
import timber.log.Timber
import java.time.LocalDateTime

class AlarmsBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val KEY_TRIGGER_TIME_IN_MILLIS = "KEY_TRIGGER_TIME_IN_MILLIS"

        fun scheduleAlarmManager(context: Context, localDateTime: LocalDateTime, action: String) {
            val alarmManager = context.getSystemService<AlarmManager>() ?: return

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Timber.e("canScheduleExactAlarms ${alarmManager.canScheduleExactAlarms()}")
            }

            // todo handle both reboots and api 31 permission checks isa.
            /*val am: AlarmManager
            am.setAlarmClock()
            am.canScheduleExactAlarms()
            //Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            // also a broadcast receiver FOR -> https://developer.android.com/training/scheduling/alarms#exact-permission-check

            am.cancel(intent)*/

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

            alarmManager.cancel(getOperationPendingIntent(context, tag))
        }

        private fun getOperationPendingIntent(context: Context, action: String, triggerTimeInMillis: Long? = null): PendingIntent {
            val intent = Intent(context, AlarmsBroadcastReceiver::class.java)
            intent.setClass(context, AlarmsBroadcastReceiver::class.java)
            intent.action = action
            intent.data = Uri.Builder().scheme("my-app").authority("com.grand.ezkorone.broadcastReceiver").build()
            intent.putExtra(KEY_TRIGGER_TIME_IN_MILLIS, triggerTimeInMillis)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            return PendingIntent.getBroadcast(
                context.applicationContext,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            /*return NavDeepLinkBuilder(requireContext())
                .setGraph(R.navigation.nav_main)
                .setDestination(R.id.dest_splash)
                .createPendingIntent()*/
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val triggerTimeInMillis = intent.getLongExtra(KEY_TRIGGER_TIME_IN_MILLIS, 0)
        val name = intent.action.orEmpty()

        Timber.e("triggerTimeInMillis $triggerTimeInMillis")

        val triggerDateTime = triggerTimeInMillis.toLocalDateTimeUTCOffset()

        scheduleAlarmManager(context, triggerDateTime.plusDays(1), name)

        NotificationUtils.showNotificationToLaunchMainActivityForAlarms(context.applicationContext, name)
    }
}

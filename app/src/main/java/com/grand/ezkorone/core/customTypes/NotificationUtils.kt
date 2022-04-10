package com.grand.ezkorone.core.customTypes

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.grand.ezkorone.R
import com.grand.ezkorone.domain.salah.SalahFardType
import com.grand.ezkorone.presentation.main.MainActivity
import timber.log.Timber

object NotificationUtils {

    private const val ALARMS_CHANNEL_ID = "ALARMS_CHANNEL_ID"
    private const val NOTIFICATIONS_CHANNEL_ID = "NOTIFICATIONS_CHANNEL_ID"
    private const val ALARMS_NOTIFICATION_ID = 47
    private const val NOTIFICATIONS_NOTIFICATION_ID = 48

    fun showNotificationToLaunchMainActivityForAlarms(
        appContext: Context,
        name: String,
    ) {
        showNotificationToLaunchMainActivity(
            appContext,
            appContext.getString(R.string.app_name),
            name,
            ALARMS_CHANNEL_ID,
            appContext.getString(R.string.alarms),
            ALARMS_NOTIFICATION_ID
        )
    }

    fun showNotificationToLaunchMainActivityForSalawat(
        appContext: Context,
        name: String,
        uri: Uri?
    ) {
        Timber.e("showNotificationToLaunchMainActivityForSalawat uri -> $uri")

        showNotificationToLaunchMainActivity(
            appContext,
            appContext.getString(R.string.app_name),
            name,
            ALARMS_CHANNEL_ID,
            appContext.getString(R.string.alarms),
            ALARMS_NOTIFICATION_ID,
            uri
        )
    }

    fun showNotificationToLaunchMainActivityForFirebaseNotification(
        appContext: Context,
        title: String,
        body: String,
    ) {
        showNotificationToLaunchMainActivity(
            appContext,
            title,
            body,
            NOTIFICATIONS_CHANNEL_ID,
            appContext.getString(R.string.alarms),
            NOTIFICATIONS_NOTIFICATION_ID
        )
    }

    private fun showNotificationToLaunchMainActivity(
        applicationContext: Context,
        title: String,
        body: String,
        channelId: String,
        channelName: String,
        notificationId: Int,
        uri: Uri? = null
    ) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.mipmap.ic_app_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_app_launcher))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setAutoCancel(true)
            .setShowWhen(true)
            .setSound(uri ?: defaultSoundUri)
            .setDefaults(/*Notification.DEFAULT_SOUND or */Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)

        val notificationManager = applicationContext.getSystemService<NotificationManager>() ?: return

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            //channel.description = getString(R.string.notifications_2)
            notificationBuilder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}

package com.grand.ezkorone.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.gms.location.*
import com.grand.ezkorone.core.customTypes.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.grand.ezkorone.R
import com.grand.ezkorone.presentation.main.MainActivity

@AndroidEntryPoint
class MediaForegroundService : Service() {

    companion object {
        const val KEY_ID_OF_DOWNLOAD_MANAGER = "KEY_ID_OF_DOWNLOAD_MANAGER"
        const val KEY_NOTIFICATION_BODY = "KEY_NOTIFICATION_BODY"
        const val KEY_STOP_SERVICE = "KEY_STOP_SERVICE"

        fun launch(
            context: Context,
            idOfDownloadManager: Long?,
            notificationBody: String,
        ) {
            val intent = Intent(context, MediaForegroundService::class.java).also {
                idOfDownloadManager?.also { _ ->
                    it.putExtra(KEY_ID_OF_DOWNLOAD_MANAGER, idOfDownloadManager)
                }
                it.putExtra(KEY_NOTIFICATION_BODY, notificationBody)
                it.putExtra(KEY_STOP_SERVICE, false)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            }else {
                context.startService(intent)
            }
        }
    }

    private lateinit var player: ExoPlayer

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        Timber.e("onCreate")

        // For hilt injection isa.
        super.onCreate()

        player = ExoPlayer.Builder(this)
            .build()

        notificationManager = getSystemService()!!

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)

            val channel = NotificationChannel(
                NotificationUtils.SALAWAT_CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_HIGH
            )
            val audioAttrs = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build()
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            channel.setSound(defaultSoundUri, audioAttrs)
            channel.importance = NotificationManager.IMPORTANCE_HIGH

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.e("onStartCommand")

        val stopService = intent?.getBooleanExtra(KEY_STOP_SERVICE, false) ?: false

        if (stopService) {
            Timber.e("stop service")

            stopForeground(false)
            stopSelf()

            notificationManager.cancel(NotificationUtils.SALAWAT_NOTIFICATION_ID)

            return START_NOT_STICKY
        }

        val idOfDownloadManager = if (intent?.hasExtra(KEY_ID_OF_DOWNLOAD_MANAGER) == true) {
            intent.getLongExtra(KEY_ID_OF_DOWNLOAD_MANAGER, -1)
        }else {
            null
        }
        val bodyOfNotification = intent?.getStringExtra(KEY_NOTIFICATION_BODY).orEmpty()

        val dismissIntent = Intent(this, MediaForegroundService::class.java).also {
            it.putExtra(KEY_STOP_SERVICE, true)
        }
        val donePendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val activityIntent = Intent(applicationContext, MainActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0 /* Request code */,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NotificationUtils.SALAWAT_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.mipmap.ic_app_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_app_launcher))
            .setContentTitle(getString(R.string.app_name))
            .setContentText(bodyOfNotification)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bodyOfNotification))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setOngoing(true)
            .setShowWhen(true)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_baseline_done_24, getString(R.string.done), donePendingIntent)
            .build()

        startForeground(idOfDownloadManager?.toInt() ?: bodyOfNotification.hashCode(), notification)

        player.also { exoPlayer ->
            /*exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == ExoPlayer.STATE_READY) {
                        // update notification if exists isa.
                    }
                }
            })*/

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val uri = idOfDownloadManager?.let {
                getSystemService<DownloadManager>()?.getUriForDownloadedFile(idOfDownloadManager)
            } ?: defaultSoundUri

            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)

            exoPlayer.playWhenReady = true
            exoPlayer.prepare()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        player.release()
    }

}

package com.grand.ezkorone.core

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.grand.ezkorone.core.customTypes.NotificationUtils
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {}

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.e("message key/value pairs -> ${remoteMessage.data}")

        val title = remoteMessage.data.getValue("title").orEmpty()
        val body = remoteMessage.data.getValue("body").orEmpty()

        NotificationUtils.showNotificationToLaunchMainActivityForFirebaseNotification(
            this,
            title,
            body
        )
    }

}

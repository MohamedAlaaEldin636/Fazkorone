package com.grand.ezkorone.core

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonParser
import com.grand.ezkorone.core.customTypes.NotificationUtils
import org.json.JSONObject
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {}

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.e("message key/value pairs -> ${remoteMessage.data}")

        val (title, body) = remoteMessage.data.getValue("message").orEmpty()
            .getNotificationModel()

        NotificationUtils.showNotificationToLaunchMainActivityForFirebaseNotification(
            this,
            title,
            body
        )
    }

    private data class NotificationModel(
        val title: String,
        val body: String,
    )

    private fun String.getNotificationModel(): NotificationModel {
        val jsonObject = JSONObject(this)

        val title = jsonObject.getString("title")
        val body = jsonObject.getString("body")

        return NotificationModel(title, body)
    }
    /*
    public static BaseNotificationModel getNotification(String mJsonString) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(mJsonString);
        Gson gson = new Gson();
        return gson.fromJson(mJson, BaseNotificationModel.class);
    }
     */

}

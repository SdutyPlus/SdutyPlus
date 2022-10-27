package com.d108.sduty.model

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.d108.sduty.R
import com.d108.sduty.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG ="FCMService"
class FirebaseCloudMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: ${token}")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification.let {
            val messageTitle = it!!.title
            val messageContent = it!!.body

//            NoticeMessageUtil.setMessageToSharedPreference(messageContent.toString())

            val mainIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0)
            val builder = NotificationCompat.Builder(this, "sduty_id")
                .setSmallIcon(R.mipmap.ic_launcher_sduty_gradient_round)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)

            NotificationManagerCompat.from(this).apply {
                notify(101,builder.build())
            }

        }
    }
}
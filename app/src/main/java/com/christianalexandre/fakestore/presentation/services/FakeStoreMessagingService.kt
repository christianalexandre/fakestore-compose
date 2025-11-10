package com.christianalexandre.fakestore.presentation.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.christianalexandre.fakestore.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FakeStoreMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Push received: ${remoteMessage.from}")

        if (FirebaseAuth.getInstance().currentUser == null) {
            Log.d("FCM", "User are not logged in")
            return
        }

        remoteMessage.data.let { data ->
            val productId = data["productId"]
            val title = data["title"]
            val message = data["body"]

            sendNotification(
                title = title ?: "Title",
                message = message ?: "body",
                productId = productId ?: "productId",
            )
        }
    }

    private fun sendNotification(
        title: String,
        message: String,
        productId: String,
    ) {
        val deepLinkUri = "app://fakestore/product_detail/$productId".toUri()
        val deepLinkIntent =
            Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                deepLinkIntent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
            )

        val channelId = "promotions_channel"
        val notificationBuilder =
            NotificationCompat
                .Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel =
            NotificationChannel(
                channelId,
                "Promotions",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "New FCM token: $token")
    }
}

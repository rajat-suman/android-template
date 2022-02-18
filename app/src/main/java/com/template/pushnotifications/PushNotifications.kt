package com.template.pushnotifications

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.template.MainActivity
import com.template.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotifications : FirebaseMessagingService() {

    private var mChannel: NotificationChannel? = null
    private var notificationManager: NotificationManager? = null
    private var pendingIntent: PendingIntent? = null
    private val notificationIcon: Int = R.mipmap.ic_launcher

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("onMessageReceived", "====${p0.data}")
        Log.d("onMessageReceived", "====${p0.notification?.body}||${p0.notification?.title}")
            displayNotifications(remoteMessage = p0.data, notification = p0.notification)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("onNewToken", "====${p0}")
    }

    @SuppressLint("WrongConstant")
    fun displayNotifications(
        remoteMessage: MutableMap<String, String>?,
        notification: RemoteMessage.Notification?,
    ) {
        if (notificationManager == null)
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bundle = getBundle(remoteMessage)

        pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.splash)
            .setArguments(bundle)
            .createPendingIntent()

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, getString(R.string.app_name))
                .setContentTitle(notification?.title)
                .setContentText(notification?.body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSmallIcon(notificationIcon) // required
                .setContentIntent(pendingIntent)
                .setGroup("Likes")
                .setGroupSummary(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mChannel == null)
                mChannel = NotificationChannel(
                    getString(R.string.app_name),
                    notification?.title,
                    NotificationManager.IMPORTANCE_HIGH
                )
            mChannel?.let {
                it.enableLights(true)
                it.lightColor = Color.RED
                it.setShowBadge(true)
                it.importance = NotificationManager.IMPORTANCE_HIGH
                it.enableVibration(true)
                it.description = notification?.body
                notificationManager?.createNotificationChannel(it)
            }
        }
        notificationManager?.notify(1234, builder.build())
    }

    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        Log.d("Active process : ", activeProcess)
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return isInBackground
    }

    private fun getBundle(remoteMessage: MutableMap<String, String>?): Bundle {
        val bundle = Bundle()
        remoteMessage?.forEach {
            bundle.putString(it.key, it.value)
        }
        return bundle
    }
}

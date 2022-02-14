package com.template.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.template.R
import java.lang.ref.WeakReference

const val PROGRESS_NOTIFICATION = 1234
fun Context.progressNotification(message:String, returnData: (NotificationManager, NotificationCompat.Builder) -> Unit) {
     val ICON = R.mipmap.ic_launcher
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
    val notificationBuilder = WeakReference(
        NotificationCompat.Builder(this.applicationContext, packageName)
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val mChannel = NotificationChannel(
            packageName,
            message,
            NotificationManager.IMPORTANCE_HIGH
        )
        mChannel.description = message
        mChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(mChannel)
    }
    notificationBuilder.get()?.apply {
        setSmallIcon(ICON) // required
        setAutoCancel(false)
        setDefaults(NotificationCompat.DEFAULT_ALL)
        setContentTitle(message)
        setProgress(0, 0, true)
        priority = NotificationCompat.PRIORITY_MAX
        setCategory(NotificationCompat.CATEGORY_MESSAGE)
        setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        setOngoing(true)
        color = ContextCompat.getColor(this@progressNotification, R.color.transparent)
        setDefaults(NotificationCompat.DEFAULT_ALL)
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
    }
    notificationManager?.notify(PROGRESS_NOTIFICATION, notificationBuilder.get()?.build())
    if (notificationManager != null) {
        notificationBuilder.get()?.let { returnData(notificationManager, it) }
    }
}
package com.example.junemon.uploadfilteringimage_firebase.utils.firebasenotif

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.junemon.uploadfilteringimage_firebase.ui.activity.main.MainActivity


class ManuallyNotification {
    private val NOTIFICATION = "Notification"
    private var REMINDER_NOTIFICATION_ID: Int = 23
    private var OREO_NOTIF_CHANEL_ID: String = "item"
    private val PENDING_INTENT_ID = 3490
    private var contentTitle: String? = null

    fun firebaseNotif(context: Context?, messageBody: String?) {
        val notif = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(OREO_NOTIF_CHANEL_ID, NOTIFICATION, NotificationManager.IMPORTANCE_HIGH)
            notif.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(context, OREO_NOTIF_CHANEL_ID)
                .setColor(
                        ContextCompat.getColor(
                                context,
                                com.example.junemon.uploadfilteringimage_firebase.R.color.colorPrimary
                        )
                )
                .setContentTitle("Hai ! ${contentTitle} okaay ?")
//                .setSmallIcon(R.drawable.ic_cofee_bean)
                .setFullScreenIntent(contentIntent(context), true)
                .setContentText(messageBody)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))  //ini pendingIntent
                .setAutoCancel(true)  //notif akan hilang setelah di klik

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.priority = NotificationCompat.PRIORITY_HIGH
        }
        notif.notify(REMINDER_NOTIFICATION_ID, builder.build())

    }

//    private fun largeIcon(context: Context): Bitmap {
//        val imgUri = mFirebaseAuth.currentUser?.photoUrl
//        val bitmap = MediaStore.Images
//        return BitmapFactory.decodeResource(res, )
//    }


    private fun contentIntent(context: Context): PendingIntent {
        val i = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, PENDING_INTENT_ID, i, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
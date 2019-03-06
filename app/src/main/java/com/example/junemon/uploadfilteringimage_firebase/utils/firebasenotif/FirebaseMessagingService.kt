package com.example.junemon.uploadfilteringimage_firebase.utils.firebasenotif

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var manualNotif: ManuallyNotification

    init {
        manualNotif = ManuallyNotification()
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        manualNotif.firebaseNotif(this, p0?.notification?.body)
        Log.d(this.javaClass.simpleName, "FCM Message Id: " + p0?.messageId)
        Log.d(this.javaClass.simpleName, "FCM Notification Message: " + p0?.notification)
        Log.d(this.javaClass.simpleName, "FCM Data Message: " + p0?.data)
    }

    override fun onNewToken(p0: String?) {
        Log.d(this.javaClass.simpleName, "new Token: " + p0)
        super.onNewToken(p0)
    }


}
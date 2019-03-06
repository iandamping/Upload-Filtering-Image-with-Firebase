package com.example.junemon.uploadfilteringimage_firebase.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.ui.activity.main.MainActivity

inline fun <reified T : Activity> FragmentActivity.startActivity() {
    startActivity(Intent(this, T::class.java))
    this.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)
    this.finish()
}

fun FragmentActivity.backToMainActivity() {
    startActivity(Intent(this, MainActivity::class.java))
    this.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)
    this.finish()
}

inline fun <reified T : Activity> FragmentActivity.startActivityWithParcel(key: String?, value: Parcelable?) {
    startActivity(Intent(this, T::class.java).putExtra(key, value))
    this.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)
}


inline fun <reified T : Activity> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T : Activity> Context.startActivityWithStringValue(key: String?, value: String?) {
    startActivity(Intent(this, T::class.java).putExtra(key, value))
}

fun FragmentManager.switchFragment(savedInstanceState: Bundle?, target: Fragment) {
    if (savedInstanceState == null) {
        this.beginTransaction().replace(R.id.main_container, target).commit()
    }
}
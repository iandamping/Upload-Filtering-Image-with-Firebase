package com.example.junemon.uploadfilteringimage_firebase.utils

import android.content.Context
import android.widget.Toast
import org.jetbrains.anko.alert
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.yesButton

fun Context.alertHelper(tittle: String?) {
    tittle?.let {
        this.alert(it) {
            yesButton { it.dismiss() }
        }.show()
    }
}

fun Context.myToast(tittle: String) {
    Toast.makeText(this, tittle, Toast.LENGTH_SHORT).show()
}

fun Context.customProgressDialog(tittle: String?, body: String?, progress: Int) {
    this.progressDialog(tittle, body) {
        this.progress = progress
    }.show()
}

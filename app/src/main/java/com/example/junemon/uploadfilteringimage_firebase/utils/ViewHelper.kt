package com.example.junemon.uploadfilteringimage_firebase.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ViewGroup.inflates(layoutResources: Int): View {
    return LayoutInflater.from(context).inflate(layoutResources, this, false)
}
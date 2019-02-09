package com.example.junemon.uploadfilteringimage_firebase.base

import android.content.Context

interface BasePresenter {
    fun getContext(): Context?

    fun onCreate(context: Context)

    fun onStop()
}
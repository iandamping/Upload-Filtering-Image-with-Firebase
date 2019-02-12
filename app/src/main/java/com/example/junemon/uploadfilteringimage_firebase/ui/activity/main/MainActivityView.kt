package com.example.junemon.uploadfilteringimage_firebase.ui.activity.main

import com.example.junemon.uploadfilteringimage_firebase.base.BaseView
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel

interface MainActivityView : BaseView {
    fun onGetDataBack(user: UserModel?)
    fun onFailedGetDataBack(message: String?)
}
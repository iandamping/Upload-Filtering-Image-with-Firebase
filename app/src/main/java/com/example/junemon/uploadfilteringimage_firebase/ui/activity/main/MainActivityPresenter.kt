package com.example.junemon.uploadfilteringimage_firebase.ui.activity.main

import android.content.Context
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.gson
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.prefHelper
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.base.BasePresenter
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MainActivityPresenter(var mView: MainActivityView) : BasePresenter {
    private lateinit var ctx: Context
    private lateinit var type: Type
    private lateinit var userModelData: UserModel

    override fun getContext(): Context? {
        return ctx
    }

    override fun onCreate(context: Context) {
        this.ctx = context
        type = object : TypeToken<UserModel>() {}.type
        mView.initView()
    }

    override fun onStop() {
    }


    fun getUserData() {
        val userData = prefHelper.getUserLoginState()
        if (userData.isNullOrEmpty()) {
            prefHelper.setUserLoginState(ctx.resources?.getString(R.string.user_logout))
        }
        if (userData != null) {
            if (!userData.equals(ctx.resources?.getString(R.string.user_logout), ignoreCase = true)) {
                userModelData = gson.fromJson(userData, UserModel::class.java)
                mView.onGetDataBack(userModelData)
            } else {
//            mView.onFailedGetDataBack(ctx.resources?.getString(R.string.login_failed))
            }
        }
    }


}
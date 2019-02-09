package com.example.junemon.uploadfilteringimage_firebase.ui.activity

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.KEY
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.gson
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.prefToken
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.base.BasePresenter
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MainActivityPresenter(var mView: MainActivityView) : BasePresenter {
    //    private lateinit var gson: Gson
    private lateinit var ctx: Context
    private lateinit var type: Type
    private lateinit var preferences: SharedPreferences
    private lateinit var userModelData: UserModel
    override fun getContext(): Context? {
        return ctx
    }

    override fun onCreate(context: Context) {
        this.ctx = context
        type = object : TypeToken<UserModel>() {}.type
    }

    override fun onStop() {
    }


    fun getUserData() {
        preferences = ctx.getSharedPreferences(prefToken, MODE_PRIVATE)
        val userData = preferences.getString(KEY, ctx.resources?.getString(R.string.user_logout))
        if (!userData.equals(ctx.resources?.getString(R.string.user_logout), ignoreCase = true)) {
            userModelData = gson.fromJson(userData, type)
            mView.onGetDataBack(userModelData)
        } else {
            mView.onFailedGetDataBack(ctx.resources?.getString(R.string.login_failed))
        }
    }


}
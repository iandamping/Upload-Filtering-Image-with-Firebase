package com.example.junemon.uploadfilteringimage_firebase.ui.activity

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
        val prefs = ctx.getSharedPreferences(prefToken, MODE_PRIVATE)
        val userData = prefs?.getString(KEY, "zero")
        if (!userData.equals("zero", ignoreCase = true)) {
            userModelData = gson.fromJson(userData, type)
            mView.onGetDataBack(userModelData)
        } else {
            mView.onFailedGetDataBack(ctx.resources?.getString(R.string.login_failed))
        }
    }


}
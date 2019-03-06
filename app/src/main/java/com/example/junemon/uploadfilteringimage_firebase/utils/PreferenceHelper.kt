package com.example.junemon.uploadfilteringimage_firebase.utils

import android.app.Application
import android.content.Context
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.PrefHelperInit
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.PrefHelperKeyValue

class PreferenceHelper(app: Application) {
    private val prefHelp by lazy {
        app.getSharedPreferences(PrefHelperInit, Context.MODE_PRIVATE)
    }

    private val prefeHelperEditor= prefHelp.edit()

    fun setUserLoginState(value: String?) {
        prefeHelperEditor.putString(PrefHelperKeyValue, value).apply()
    }

    fun getUserLoginState(): String? {
        return prefHelp.getString(PrefHelperKeyValue, "") ?: ""
    }


}
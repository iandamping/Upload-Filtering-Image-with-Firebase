package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile

import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentView
import com.google.firebase.auth.FirebaseUser

interface ProfileFragmentView:BaseFragmentView {
//    fun onGetDataBack(user:FirebaseUser?)
    fun onGetDataBack(user:String?)
}
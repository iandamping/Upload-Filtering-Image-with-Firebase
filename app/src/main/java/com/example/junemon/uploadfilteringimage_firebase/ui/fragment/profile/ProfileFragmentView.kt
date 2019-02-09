package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile

import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentView

interface ProfileFragmentView : BaseFragmentView {
    //    fun onGetDataBack(user:FirebaseUser?)
    fun onGetDataBack(user: String?)
}
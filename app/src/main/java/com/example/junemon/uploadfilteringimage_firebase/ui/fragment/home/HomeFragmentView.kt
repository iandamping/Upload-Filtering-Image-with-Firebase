package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home

import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentView
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel

interface HomeFragmentView : BaseFragmentView {
    fun onGetDataback(data: UploadImageModel?)
//    fun ongetDatauser(user:FirebaseUser?)
}
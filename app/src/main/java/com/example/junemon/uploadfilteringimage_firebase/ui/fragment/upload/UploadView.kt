package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.upload

import com.example.junemon.uploadfilteringimage_firebase.base.BaseView

interface UploadView : BaseView {
    fun allPermisionGranted(status: Boolean)
}
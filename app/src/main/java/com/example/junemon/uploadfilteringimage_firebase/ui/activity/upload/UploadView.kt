package com.example.junemon.uploadfilteringimage_firebase.ui.activity.upload

import android.net.Uri
import com.example.junemon.uploadfilteringimage_firebase.base.BaseView

interface UploadView : BaseView {
    fun allPermisionGranted(status: Boolean)
    fun getCameraUri(uris: Uri)
}
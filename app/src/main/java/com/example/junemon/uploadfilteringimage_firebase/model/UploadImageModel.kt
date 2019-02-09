package com.example.junemon.uploadfilteringimage_firebase.model

data class UploadImageModel(var text: String?, var name: String?, var photoUrl: String?) {
    constructor() : this(null, null, null)
}
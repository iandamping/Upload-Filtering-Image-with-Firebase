package com.example.junemon.uploadfilteringimage_firebase.model

data class UploadImageModel(
    var text: String?, var name: String?, var photoUrl: String?,
    var userPhotoProfileUrl: String?, var userPhotoLastPathSegment: String?
) {
    constructor() : this(null, null, null, null, null)
}
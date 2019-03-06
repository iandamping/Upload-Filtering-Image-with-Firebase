package com.example.junemon.uploadfilteringimage_firebase.utils

object Constant {
    const val PrefHelperKeyValue = "data"
    const val PrefHelperInit = "tokenizer"
    val uploadNodePhotos = "photo"
    val uploadNodeUser = "users"
    val uploadNodePhotoStorage = "photo_storage"
    val IMAGE_NAME: String = "empty_image.png"
    val defaultMessageLimit = 1000
    val RequestSelectGalleryImage = 102
    val RequestOpenCamera = 234
    val RequestSignIn: Int = 2341
    val saveCaptureImagePath = "picture" + System.currentTimeMillis() + ".jpeg"
    val saveFilterImagePath = "filterImage" + System.currentTimeMillis() + ".jpeg"
    val maxWidth = 612
    val maxHeight = 816
    val userPassedkey = "users"
    val userLogout = "logouts"
    val userLogin = "login"
}
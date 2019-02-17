package com.example.junemon.uploadfilteringimage_firebase.model

data class UserModel(var name: String?, var email: String?, var userPhotoUrl: String?) {
    constructor() : this(null, null, null)
}
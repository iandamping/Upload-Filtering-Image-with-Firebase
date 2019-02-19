package com.example.junemon.uploadfilteringimage_firebase.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(var name: String?, var email: String?, var userPhotoUrl: String?,var userToken:String?,var phoneNumber:String?) :
    Parcelable {
    constructor() : this(null, null, null,null,null)
}
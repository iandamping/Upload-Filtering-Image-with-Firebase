package com.example.junemon.uploadfilteringimage_firebase.utils.firebasenotif

import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.google.firebase.database.DatabaseReference

fun DatabaseReference.saveProfileData(child: String, data: Any) {
    this.child(child).setValue(data is UserModel)
}
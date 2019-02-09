package com.example.junemon.uploadfilteringimage_firebase.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel : ViewModel() {
    private var firebaseUser: MutableLiveData<FirebaseUser>? = MutableLiveData()

    fun setFirebaseUser(user: FirebaseUser?) {
        firebaseUser?.value = user
    }

    fun getFirebaseUser(): MutableLiveData<FirebaseUser>? = firebaseUser
}
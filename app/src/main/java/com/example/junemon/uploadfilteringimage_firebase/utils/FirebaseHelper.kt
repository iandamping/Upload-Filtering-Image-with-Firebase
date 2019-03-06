package com.example.junemon.uploadfilteringimage_firebase.utils

import android.app.ProgressDialog
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.junemon.uploadfilteringimage_firebase.data.HomeViewmodel
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import com.example.junemon.uploadfilteringimage_firebase.ui.activity.main.MainActivity
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.userLogin
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.userLogout
import com.google.firebase.database.*

fun Context.deleteSpesificFirebaseData(data: Query?, dialogs: ProgressDialog?) {
    dialogs?.show()
    data?.addChildEventListener(object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildRemoved(p0: DataSnapshot) {
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            p0.ref.setValue(null).addOnCompleteListener {
                if (it.isSuccessful) {
                    dialogs?.dismiss()
                    this@deleteSpesificFirebaseData.startActivity<MainActivity>()
                }
            }
        }
    })
}

fun Fragment.getAllData(dataReference: DatabaseReference, status: String?) {
    val vm = this.viewModelHelperForFragment<HomeViewmodel>()
    val listener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            if (p0.getValue(UploadImageModel::class.java) != null) {
                if (status.equals(userLogin)) {
                    vm.setImageData(p0.getValue(UploadImageModel::class.java)!!)
                }
            }
        }

        override fun onChildRemoved(p0: DataSnapshot) {
        }
    }

    if (!status.equals(userLogout)) {
        dataReference.addChildEventListener(listener)
    } else {
        dataReference.removeEventListener(listener)
    }
}
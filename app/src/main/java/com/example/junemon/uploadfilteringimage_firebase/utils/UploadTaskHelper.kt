package com.example.junemon.uploadfilteringimage_firebase.utils

import android.content.Context
import android.net.Uri
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

fun UploadTask.uploadHelper(ctx: Context?, tittle: String?, storageReference: StorageReference, selectedImage: Uri?, username: String?, urisPath: String, userPhoto: String?, comment: String?) {
    this.addOnSuccessListener {
        val progress = 100.0 * it.getBytesTransferred() / it.getTotalByteCount()
    }.addOnSuccessListener {
        storageReference.downloadUrl.addOnSuccessListener {

        }
    }

}

fun DatabaseReference.databaseHelper(ctx: Context?, selectedImage: Uri?, username: String?, urisPath: String, userPhoto: String?, comment: String?) {
    val data = UploadImageModel(comment, username, urisPath, userPhoto, selectedImage?.lastPathSegment)
    this.push().setValue(data).addOnCompleteListener {
        if (it.isSuccessful) {
        }
    }
}

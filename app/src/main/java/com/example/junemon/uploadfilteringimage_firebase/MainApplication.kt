package com.example.junemon.uploadfilteringimage_firebase

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import java.io.File

class MainApplication {
    companion object {
        const val KEY = "data"
        const val prefToken = "tokenizer"
        val IMAGE_NAME: String = "empty_image.png"
        var mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var mFirebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
        var mFirebaseInstanceId:FirebaseInstanceId = FirebaseInstanceId.getInstance()
        var gson: Gson
        var mDatabaseReference: DatabaseReference
        var userDatabaseReference: DatabaseReference
        var storageDatabaseReference: StorageReference
        private val uploadNodePhotos = "photo"
        private val uploadNodeUser = "users"
        private val uploadNodePhotoStorage = "photo_storage"
        val defaultMessageLimit = 1000
        val RequestSelectGalleryImage = 102
        val RequestOpenCamera = 234
        val RequestSignIn: Int = 2341
        val saveCaptureImagePath = "picture" + System.currentTimeMillis() + ".jpeg"
        val saveFilterImagePath = "filterImage" + System.currentTimeMillis() + ".jpeg"
        val maxWidth = 612
        val maxHeight = 816

        init {
            mDatabaseReference = mFirebaseDatabase.reference.child(uploadNodePhotos)
            userDatabaseReference = mFirebaseDatabase.reference.child(uploadNodeUser)
            storageDatabaseReference = mFirebaseStorage.getReferenceFromUrl("gs://upload-filter-image.appspot.com/")
            System.loadLibrary("NativeImageProcessor")
            gson = Gson()
        }

        fun sharedLoadDesiredFragment(
            savedInstanceState: Bundle?,
            fragmentManager: FragmentManager?,
            desiredFrag: Fragment
        ) {
            if (savedInstanceState == null) {
                fragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.main_container, desiredFrag)
                    ?.commit()
            }
        }


        fun voidCustomMediaScannerConnection(ctx: Context?, paths: String?) {
            val directory = Environment.getExternalStorageDirectory()
            val passingFile = File(directory, paths)
            MediaScannerConnection.scanFile(ctx, arrayOf(passingFile.toString()), null) { path, uri ->
                Log.i("ExternalStorage", "Scanned $path:")
                Log.i("ExternalStorage", "-> uri=$uri")
            }
        }

    }
}
package com.example.junemon.uploadfilteringimage_firebase

import android.app.Application
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.junemon.uploadfilteringimage_firebase.api.ApiClient
import com.example.junemon.uploadfilteringimage_firebase.api.ApiInterface
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.uploadNodePhotos
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.uploadNodeUser
import com.example.junemon.uploadfilteringimage_firebase.utils.PreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import java.io.File

class MainApplication : Application() {
    companion object {
        lateinit var prefHelper: PreferenceHelper
        lateinit var gson: Gson
        lateinit var mDatabaseReference: DatabaseReference
        lateinit var userDatabaseReference: DatabaseReference
        lateinit var storageDatabaseReference: StorageReference
        lateinit var mFirebaseDatabase: FirebaseDatabase
        lateinit var mFirebaseAuth: FirebaseAuth
        lateinit var mFirebaseStorage: FirebaseStorage
        lateinit var mFirebaseInstanceId: FirebaseInstanceId
        lateinit var sentNotificationFirebase: ApiInterface

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

    override fun onCreate() {
        super.onCreate()
        sentNotificationFirebase = ApiClient().createRequest(this)

        prefHelper = PreferenceHelper(this)
//        prefHelper.setUserLoginState("Logged out")

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()
        mFirebaseInstanceId = FirebaseInstanceId.getInstance()
        mDatabaseReference = mFirebaseDatabase.reference.child(uploadNodePhotos)
        userDatabaseReference = mFirebaseDatabase.reference.child(uploadNodeUser)
        storageDatabaseReference = mFirebaseStorage.getReferenceFromUrl("gs://upload-filter-image.appspot.com/")
        System.loadLibrary("NativeImageProcessor")
        gson = Gson()
    }
}
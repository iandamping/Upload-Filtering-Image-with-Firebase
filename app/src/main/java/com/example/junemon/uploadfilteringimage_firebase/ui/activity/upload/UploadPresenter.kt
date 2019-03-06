package com.example.junemon.uploadfilteringimage_firebase.ui.activity.upload

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.base.BasePresenter
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import com.example.junemon.uploadfilteringimage_firebase.ui.activity.main.MainActivity
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.RequestOpenCamera
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.RequestSelectGalleryImage
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.saveCaptureImagePath
import com.example.junemon.uploadfilteringimage_firebase.utils.ImageUtils
import com.example.junemon.uploadfilteringimage_firebase.utils.startActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.jetbrains.anko.alert
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.yesButton
import java.io.File


class UploadPresenter(private val target: FragmentActivity, private val mView: UploadView) : BasePresenter {
    private lateinit var ctx: Context
    private lateinit var utils: ImageUtils
    private lateinit var uploads: UploadImageModel
    private lateinit var intents: Intent
    private lateinit var uploadTask: UploadTask

    override fun getContext(): Context? {
        return ctx
    }

    override fun onCreate(context: Context) {
        ctx = context
        utils = ImageUtils(ctx)
        mView.initView()
        intents = Intent()
    }

    override fun onStop() {
    }

    fun uploadImageToFirebase(
            storageReference: StorageReference,
            databaseReference: DatabaseReference,
            selectedImage: Uri?,
            username: String?,
            userPhoto: String?,
            comment: String?
    ) {
        if (selectedImage != null) {
            val dialogs = ctx.progressDialog(
                    ctx.resources?.getString(R.string.please_wait),
                    ctx.resources?.getString(R.string.uploading_image)
            )
            val spaceRef = storageReference.child(selectedImage.lastPathSegment)
//            val spaceRef = mFirebaseAuth.uid?.let { storageReference.child(it) }
            uploadTask = spaceRef.putFile(selectedImage)
            uploadTask.addOnProgressListener {
                //adding progress listener to handle progressbar set progress
                dialogs.show()
                val progress = 100.0 * it.getBytesTransferred() / it.getTotalByteCount()
                dialogs.progress = progress.toInt()
            }.addOnSuccessListener {
                spaceRef.downloadUrl.addOnSuccessListener {
                    //get the download url for image firebase storage
                    uploads = UploadImageModel(comment, username, it.toString(), userPhoto, selectedImage.lastPathSegment)
                    databaseReference.push().setValue(uploads).addOnCompleteListener {
                        //adding listener if it successfully upload progresdialog shutdown
                        if (it.isSuccessful) {
                            dialogs.dismiss()
                            ctx.startActivity<MainActivity>()
//                            intents.setClass(ctx, MainActivity::class.java)
                            target.finish()
//                            ctx.startActivity(intents)
                        }
                    }
                }
            }
        }
    }


    fun getAllPermisions() {
        Dexter.withActivity(target).withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted()!!) {
                    mView.allPermisionGranted(report.areAllPermissionsGranted())
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
            ) {
            }

        }).check()
    }

    fun openImageFromGallery(status: Boolean?) {
        if (status != null) {
            if (status) {
                intents = Intent(Intent.ACTION_PICK)
                intents.type = "image/*"
                target.startActivityForResult(intents, RequestSelectGalleryImage)
            } else {
                ctx.alert(ctx.resources?.getString(com.example.junemon.uploadfilteringimage_firebase.R.string.permisison_not_granted)!!) {
                    yesButton {
                        it.dismiss()
                    }
                }.show()
            }
        }

    }

    fun saveImageToGallery(views: View, status: Boolean?, bitmap: Bitmap?) {
        if (status != null) {
            if (status) {
                utils.saveImage(views, bitmap)
            } else {
                ctx.alert(ctx.resources?.getString(com.example.junemon.uploadfilteringimage_firebase.R.string.permisison_not_granted)!!) {
                    yesButton {
                        it.dismiss()
                    }
                }.show()
            }
        }

    }

    fun openCamera(status: Boolean?) {
        if (status != null) {
            if (status) {
                val pictureUri: Uri = FileProvider.getUriForFile(
                        ctx,
                        ctx.resources.getString(com.example.junemon.uploadfilteringimage_firebase.R.string.package_name),
                        createImageFileFromPhoto()
                )

                intents = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //tell the camera where to save the image depending on your File set
                intents.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
                //tell the camera to request Write Permission
                intents.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                target.startActivityForResult(intents, RequestOpenCamera)
            }
        }
    }

    private fun nonVoidCustomMediaScannerConnection(ctx: Context?, paths: String?): File {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val passingFile = File(directory, paths)
        MediaScannerConnection.scanFile(ctx, arrayOf(passingFile.toString()), null) { path, uri ->
            Log.i("ExternalStorage", "Scanned $path:")
            Log.i("ExternalStorage", "-> uri=$uri")
            //this is the way to get image uri when capture it with camera
            mView.getCameraUri(uri)

        }
        return passingFile
    }

    fun createImageFileFromPhoto(): File {
        return nonVoidCustomMediaScannerConnection(ctx, saveCaptureImagePath)
    }
}
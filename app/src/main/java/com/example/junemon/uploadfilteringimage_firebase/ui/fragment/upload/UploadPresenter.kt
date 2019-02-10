package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.upload

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.example.junemon.uploadfilteringimage_firebase.MainApplication
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.RequestSelectGalleryImage
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.base.BasePresenter
import com.example.junemon.uploadfilteringimage_firebase.utils.ImageUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File

class UploadPresenter(var target: FragmentActivity, var mView: UploadView) : BasePresenter {
    lateinit var ctx: Context
    private lateinit var utils: ImageUtils
    override fun getContext(): Context? {
        return ctx
    }

    override fun onCreate(context: Context) {
        ctx = context
        utils = ImageUtils(ctx)
    }

    override fun onStop() {
    }

    fun getCameraPermissions() {

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
                val i = Intent(Intent.ACTION_PICK)
                i.type = "image/*"
                target.startActivityForResult(i, RequestSelectGalleryImage)
            } else {
                Toast.makeText(ctx, ctx.resources?.getString(R.string.permisison_not_granted), Toast.LENGTH_SHORT)
                    .show();
            }
        }

    }

    fun saveImageToGallery(views: View, status: Boolean?, bitmap: Bitmap?) {
        if (status != null) {
            if (status) {
                utils.saveImage(views, bitmap)
            } else {
                Toast.makeText(ctx, ctx.resources?.getString(R.string.permisison_not_granted), Toast.LENGTH_SHORT)
                    .show();
            }
        }

    }

    fun openCamera(status: Boolean?, files: File) {
        if (status != null) {
            if (status) {
                val pictureUri: Uri = FileProvider.getUriForFile(
                    ctx, ctx.resources.getString(R.string.package_name),
                    files
                )

                val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //tell the camera where to save the image depending on your File set
                i.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
                //tell the camera to request Write Permission
                i.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                target.startActivityForResult(i, MainApplication.RequestOpenCamera)
            }
        }
    }

}
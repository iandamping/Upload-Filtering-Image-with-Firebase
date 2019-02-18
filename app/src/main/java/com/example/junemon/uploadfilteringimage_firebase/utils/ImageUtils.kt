package com.example.junemon.uploadfilteringimage_firebase.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.junemon.uploadfilteringimage_firebase.MainApplication
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.saveFilterImagePath
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.voidCustomMediaScannerConnection
import com.example.junemon.uploadfilteringimage_firebase.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.progressDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class ImageUtils(var ctx: Context?) {

    fun getBitmapFromAssets(fileName: String, widthImage: Int, heightImage: Int): Bitmap? {
        val assetManager: AssetManager = ctx?.assets!!
        val inputStreams: InputStream
        try {
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            inputStreams = assetManager.open(fileName)
            //calculate sample size
            options.inSampleSize = calculateSampleSize(options, widthImage, heightImage)
            //decode bitmpat with samplesize set
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeStream(inputStreams, null, options)
        } catch (e: IOException) {
            Log.e(this::class.java.simpleName, e.message)
        }
        return null
    }

    fun getBitmapFromGallery(path: Uri, widthImage: Int, heightImage: Int): Bitmap? {
        val filePathColum = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = ctx?.applicationContext?.contentResolver?.query(path, filePathColum, null, null, null)
        cursor?.moveToFirst()

        val columnIndex: Int? = cursor?.getColumnIndex(filePathColum.get(0))
        val picturePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()

        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(picturePath, options)

        options.inSampleSize = calculateSampleSize(options, widthImage, heightImage)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(picturePath, options)
    }

    fun saveImage(views: View, bitmap: Bitmap?) {
        val pictureDirectory = Environment.getExternalStorageDirectory()
        val imageFile = File(pictureDirectory, saveFilterImagePath)
        val quality = 100
        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
            openImageFromSnackbar(views, imageFile)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        voidCustomMediaScannerConnection(ctx, saveFilterImagePath)
    }

    fun saveFirebaseImageToGallery(storageReference: StorageReference, views: View, lastPathSegment: String?) {
        val dialogs = ctx?.progressDialog(
            ctx?.resources?.getString(R.string.please_wait),
            ctx?.resources?.getString(R.string.downloading_image)
        )
        val spaceRef = lastPathSegment?.let { storageReference.child(it) }
        val pictureDirectory = Environment.getExternalStorageDirectory()
        val imageFile = File(pictureDirectory, MainApplication.saveFilterImagePath)
        /*
        kita memerlukan variable file agar menampung image dari firebase storage kita
        perhatikan juga pada bagian storage refence nya kita memerlukan nama file / last path segment image kita agar tau
        image mana yg akan di download
         */
        spaceRef?.getFile(imageFile)?.addOnProgressListener {
            dialogs?.show()
            val progress = 100.0 * it.getBytesTransferred() / it.getTotalByteCount()
            dialogs?.progress = progress.toInt()
        }?.addOnSuccessListener {
            if (it.task.isSuccessful) {
                MainApplication.voidCustomMediaScannerConnection(ctx, saveFilterImagePath)
                dialogs?.dismiss()
                openImageFromSnackbar(views, imageFile)
            }
        }?.addOnFailureListener {
            Log.e("firebase ", ";local tem file not created  created $it");
        }
    }

    fun shareFirebaseImageThroughTelegram(photoPathSegment: String?) {
        val dialogs = ctx?.indeterminateProgressDialog(
            ctx?.resources?.getString(R.string.please_wait),
            ctx?.resources?.getString(R.string.processing_image)
        )
        val spaceRef = photoPathSegment?.let { MainApplication.storageDatabaseReference.child(it) }
        val pictureDirectory = Environment.getExternalStorageDirectory()
        val imageFile = File(pictureDirectory, saveFilterImagePath)
        spaceRef?.getFile(imageFile)?.addOnSuccessListener {
            dialogs?.show()
            if (it.task.isSuccessful) {
                dialogs?.dismiss()
                spaceRef.downloadUrl.addOnSuccessListener {
                    try {
                        val telegramIntent = Intent(Intent.ACTION_SEND)
                        telegramIntent.setType("image/*")
                        val uri = FileProvider.getUriForFile(
                            ctx!!,
                            ctx!!.resources.getString(com.example.junemon.uploadfilteringimage_firebase.R.string.package_name),
                            imageFile
                        )
                        telegramIntent.putExtra(Intent.EXTRA_STREAM, uri)
                        telegramIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        ctx?.startActivity(Intent.createChooser(telegramIntent, "Choose app"))
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(ctx, "Telegram not installed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    //decode File into Bitmap and compress it
    fun decodeSampledBitmapFromFile(imageFile: File, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFile.absolutePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        var scaledBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)

        //check the rotation of the image and display it properly
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
        val matrix = Matrix()
        if (orientation == 6) {
            matrix.postRotate(90F)
        } else if (orientation == 3) {
            matrix.postRotate(180F)
        } else if (orientation == 8) {
            matrix.postRotate(270F)
        }
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
        return scaledBitmap
    }

    private fun openImageFromSnackbar(views: View, imageFile: File) {
        val snackbar = Snackbar
            .make(views, "Image saved to gallery!", Snackbar.LENGTH_LONG)
            .setAction("OPEN") {
                val i = Intent(Intent.ACTION_VIEW)
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val uri =
                    FileProvider.getUriForFile(ctx!!, ctx!!.resources.getString(R.string.package_name), imageFile)
                i.setDataAndType(uri, "image/*")
                ctx?.startActivity(i)
            }
        snackbar.show()
    }

    private fun calculateSampleSize(options: BitmapFactory.Options, requiredWidth: Int, requiredHeight: Int): Int {
        val height = options.outHeight
        val widht = options.outWidth
        var inSampleSize = 1

        if (height > requiredHeight || widht > requiredHeight) {
            val halfHeight = height / 2
            val halfWidth = widht / 2

            while ((halfHeight / inSampleSize) >= requiredHeight && (halfWidth / inSampleSize) >= requiredWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
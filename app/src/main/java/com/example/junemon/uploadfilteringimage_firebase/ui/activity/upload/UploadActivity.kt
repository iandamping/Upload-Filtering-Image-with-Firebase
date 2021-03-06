package com.example.junemon.uploadfilteringimage_firebase.ui.activity.upload

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.mDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.storageDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.example.junemon.uploadfilteringimage_firebase.ui.adapter.imagefilteradapter.ViewPagerAdapter
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.imageeditor.edit.EditImageFragment
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.imageeditor.edit.EditImageListener
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.imageeditor.filter.FragmentFilterList
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.imageeditor.filter.FragmentFilterListener
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.IMAGE_NAME
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.RequestOpenCamera
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.RequestSelectGalleryImage
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.maxHeight
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.maxWidth
import com.example.junemon.uploadfilteringimage_firebase.utils.ImageUtils
import com.example.junemon.uploadfilteringimage_firebase.utils.backToMainActivity
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class UploadActivity : AppCompatActivity(),
        UploadView, FragmentFilterListener, EditImageListener {

    private var selectedUriForFirebase: Uri? = null
    private lateinit var presenter: UploadPresenter
    private var originalImage: Bitmap? = null
    private var filteredImage: Bitmap? = null
    private var finalImage: Bitmap? = null
    private var filtersListFragment: FragmentFilterList? = null
    private var editImageFragment: EditImageFragment? = null

    private var brightnessFinal = 0
    private var saturationFinal = 1.0f
    private var contrastFinal = 1.0f
    private var stat: Boolean? = null
    private lateinit var userData: UserModel
    //    private var name: String? = null
//    private var userphoto: String? = null
    private var comment: String? = null
    private lateinit var BitmapUtils: ImageUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        presenter = UploadPresenter(this, this)
        presenter.onCreate(this)
        presenter.getAllPermisions()
        BitmapUtils = ImageUtils(this)

        loadImage()
        userData = intent.getParcelableExtra(Constant.userPassedkey)
//        name = userData.name
//        userphoto = userData.userPhotoUrl
        setupViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)

    }

    override fun allPermisionGranted(status: Boolean) {
        stat = status
    }

    private fun setupViewPager(vp: ViewPager) {
        val vpAdapter = ViewPagerAdapter(supportFragmentManager)
        filtersListFragment = FragmentFilterList()
        editImageFragment = EditImageFragment()

        filtersListFragment?.setListener(this)
        editImageFragment?.setListener(this)

        vpAdapter.addFragment(filtersListFragment, getString(R.string.tab_filters))
        vpAdapter.addFragment(editImageFragment, getString(R.string.tab_edit))
        vp.adapter = vpAdapter
    }

    override fun getCameraUri(uris: Uri) {
        selectedUriForFirebase = uris
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestSelectGalleryImage) {
                if (data != null) {
                    selectedUriForFirebase = data.data
                    val bitmap = BitmapUtils.getBitmapFromGallery(data.data!!, maxWidth, maxHeight)
                    clearBitmapMemory()

                    originalImage = bitmap?.copy(Bitmap.Config.ARGB_8888, true)
                    filteredImage = originalImage?.copy(Bitmap.Config.ARGB_8888, true)
                    finalImage = originalImage?.copy(Bitmap.Config.ARGB_8888, true)
                    ivImagePreview.setImageBitmap(originalImage)
                    bitmap?.recycle()

                    filtersListFragment?.prepareThumbnail(originalImage);
                }
            } else if (requestCode == RequestOpenCamera) {
                val bitmap = BitmapUtils.decodeSampledBitmapFromFile(
                        presenter.createImageFileFromPhoto(), reqWidth = maxWidth, reqHeight = maxHeight
                )
                clearBitmapMemory()

                originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                filteredImage = originalImage?.copy(Bitmap.Config.ARGB_8888, true)
                finalImage = originalImage?.copy(Bitmap.Config.ARGB_8888, true)
                ivImagePreview.setImageBitmap(originalImage)
                bitmap.recycle()

                filtersListFragment?.prepareThumbnail(originalImage)
            }
        }
    }

    override fun initView() {
        ivOpenCamera.setOnClickListener {
            invokeCamera()
        }
        ivOpenGallery.setOnClickListener {
            presenter.openImageFromGallery(stat)
        }
        ivSentImage.setOnClickListener {
            comment = etPhotoComment.text.toString().trim()
            if (selectedUriForFirebase != null) {
                presenter.uploadImageToFirebase(
                        storageDatabaseReference,
                        mDatabaseReference, selectedUriForFirebase, userData.name, userData.userPhotoUrl, comment
                )
                etPhotoComment.text = Editable.Factory.getInstance().newEditable("")
            } else {
                alert("Pick image first") {
                    yesButton {
                        it.dismiss()
                    }
                }.show()
            }
        }

    }

    override fun onBackPressed() {
        backToMainActivity()
    }

    override fun onFilterSelected(filter: Filter) {
        resetControls()
        filteredImage = originalImage?.copy(Bitmap.Config.ARGB_8888, true)
        finalImage = filteredImage?.copy(Bitmap.Config.ARGB_8888, true)
        ivImagePreview.setImageBitmap(filter.processFilter(finalImage))

    }

    override fun onBrightnessChanged(brightness: Int) {
        brightnessFinal = brightness
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness));
        ivImagePreview.setImageBitmap(myFilter.processFilter(finalImage?.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        ivImagePreview.setImageBitmap(myFilter.processFilter(finalImage?.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onContrastChanged(contrast: Float) {
        contrastFinal = contrast
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrast))
        ivImagePreview.setImageBitmap(myFilter.processFilter(finalImage?.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onEditStarted() {
    }

    override fun onEditCompleted() {
        val bitmap = filteredImage?.copy(Bitmap.Config.ARGB_8888, true)
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        finalImage = myFilter.processFilter(bitmap)
    }

    // load the default image from assets on app launch
    private fun loadImage() {
        originalImage = BitmapUtils.getBitmapFromAssets(IMAGE_NAME, 300, 300)
        filteredImage = originalImage?.copy(Bitmap.Config.ARGB_8888, true)
        finalImage = originalImage?.copy(Bitmap.Config.ARGB_8888, true)
        ivImagePreview.setImageBitmap(originalImage)
    }

    //reset all contrast,brightness and saturation
    private fun resetControls() {
        if (editImageFragment != null) {
            editImageFragment?.resetControls()
        }
        brightnessFinal = 0
        saturationFinal = 1.0f
        contrastFinal = 1.0f
    }

    //clear bitmap memory
    private fun clearBitmapMemory() {
        originalImage?.recycle()
        filteredImage?.recycle()
        finalImage?.recycle()
    }

    private fun invokeCamera() {
        presenter.openCamera(stat)
    }
}
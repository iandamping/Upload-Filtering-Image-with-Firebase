package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.imageeditor.filter

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentPresenter
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.IMAGE_NAME
import com.example.junemon.uploadfilteringimage_firebase.utils.ImageUtils
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ian on 07/02/19.
 */

class FragmentFilterPresenter(var mView: FragmentFilterView) : BaseFragmentPresenter {
    private var ctx: Context? = null
    private lateinit var utils: ImageUtils
    private lateinit var thumbnailListItem: MutableList<ThumbnailItem>
    private lateinit var compose: CompositeDisposable
    private lateinit var listFilters: List<Filter>

    override fun onAttach(context: Context?) {
        this.ctx = context
        compose = CompositeDisposable()
        utils = ImageUtils(ctx)
        thumbnailListItem = arrayListOf()
        listFilters = FilterPack.getFilterPack(ctx)

    }

    override fun onCreateView(view: View) {
        mView.initView(view)
    }


    fun prepareThumbnail(bitmap: Bitmap?) {
        compose.add(Observable.fromCallable {
            Runnable {
                var thumbImage: Bitmap? = null

                if (bitmap == null) {
                    thumbImage = utils.getBitmapFromAssets(IMAGE_NAME, 100, 100)
                } else {
                    thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
                }
                if (thumbImage == null) {
                    return@Runnable
                }

                ThumbnailsManager.clearThumbs()
                thumbnailListItem.clear()

                //add normal bitmap
                val thumbnailItem = ThumbnailItem()
                thumbnailItem.image = thumbImage
                thumbnailItem.filterName =
                        ctx?.resources?.getString(R.string.filter_normal)
                ThumbnailsManager.addThumb(thumbnailItem)

                for (filter in listFilters) {
                    val tI = ThumbnailItem()
                    tI.image = thumbImage
                    tI.filter = filter
                    tI.filterName = filter.getName()
                    ThumbnailsManager.addThumb(tI)
                }

                thumbnailListItem.addAll(ThumbnailsManager.processThumbs(ctx))
            }.run()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            mView.getListFilter(thumbnailListItem)
        })
    }

    fun onDestroy() {
        if (compose != null && compose.isDisposed) {
            compose.dispose()
        }
    }
}

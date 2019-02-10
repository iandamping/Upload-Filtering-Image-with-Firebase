package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.upload.filter

import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentView
import com.zomato.photofilters.utils.ThumbnailItem

/**
 * Created by ian on 07/02/19.
 */

interface FragmentFilterView : BaseFragmentView {
    fun getListFilter(allData: List<ThumbnailItem>?)
}
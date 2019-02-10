package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.upload.filter

import com.zomato.photofilters.imageprocessors.Filter

/**
 * Created by ian on 07/02/19.
 */

interface FragmentFilterListener {
    fun onFilterSelected(filter: Filter)
}
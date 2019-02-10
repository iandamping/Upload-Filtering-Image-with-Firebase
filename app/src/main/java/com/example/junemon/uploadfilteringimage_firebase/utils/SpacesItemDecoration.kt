package com.example.junemon.uploadfilteringimage_firebase.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by ian on 07/02/19.
 */

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            outRect.left = space
            outRect.right = 0
        } else {
            outRect.right = space
            outRect.left = 0
        }
    }
}
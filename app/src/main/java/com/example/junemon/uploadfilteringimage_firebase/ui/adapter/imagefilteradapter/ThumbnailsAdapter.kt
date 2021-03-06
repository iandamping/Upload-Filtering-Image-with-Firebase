package com.example.junemon.uploadfilteringimage_firebase.ui.adapter.imagefilteradapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.junemon.uploadfilteringimage_firebase.R
import com.zomato.photofilters.utils.ThumbnailItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_thumbnail_list.*

/**
 * Created by ian on 07/02/19.
 */

class ThumbnailsAdapter(var ctx: Context?, var listData: List<ThumbnailItem>, var listener: (ThumbnailItem) -> Unit) :
        RecyclerView.Adapter<ThumbnailsAdapter.ThumbnailAdapterViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ThumbnailAdapterViewHolder {
        val views: View = LayoutInflater.from(ctx).inflate(R.layout.item_thumbnail_list, p0, false)
        return ThumbnailAdapterViewHolder(views, ctx)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(p0: ThumbnailAdapterViewHolder, p1: Int) {
        p0.initData(listData.get(p1), listener, p1)
    }


    class ThumbnailAdapterViewHolder(override val containerView: View, var ctx: Context?) : LayoutContainer,
            RecyclerView.ViewHolder(containerView) {
        var selectedIndex = 0
        fun initData(data: ThumbnailItem, listener: (ThumbnailItem) -> Unit, positions: Int) {
            ivThumbnail.setImageBitmap(data.image)
            tvFilterName.text = data.filterName
            selectedIndex = positions
            itemView.setOnClickListener { listener(data) }

            if (selectedIndex == positions) {
                tvFilterName.setTextColor(ContextCompat.getColor(ctx!!, R.color.filter_label_selected))
            } else {
                tvFilterName.setTextColor(ContextCompat.getColor(ctx!!, R.color.filter_label_normal))
            }
        }
    }
}
package com.example.junemon.uploadfilteringimage_firebase.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_fragment_home.*

class HomeFragmentAdapter(
    var ctx: Context?,
    var listMessage: List<UploadImageModel>,
    val listener: (UploadImageModel) -> Unit
) : RecyclerView.Adapter<HomeFragmentAdapter.viewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewHolder {
        return viewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_fragment_home, p0, false), ctx)
    }

    override fun getItemCount(): Int = listMessage.size

    override fun onBindViewHolder(p0: viewHolder, p1: Int) {
        p0.bindView(listMessage.get(p1), listener)
    }

    class viewHolder(override val containerView: View, var ctx: Context?) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindView(data: UploadImageModel, listener: (UploadImageModel) -> Unit) {
            tvFirebaseName.text = data.name
            tvFirebaseDesc.text = data.text
            ctx?.let { Glide.with(it).load(data.photoUrl).into(ivFirebaseImage) }
            itemView.setOnClickListener {
                listener(data)
            }
        }

    }
}
package com.example.junemon.uploadfilteringimage_firebase.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

fun ImageView.loadUrl(urls: String?) {
    urls?.let { Glide.with(context).load(it).thumbnail(imageThumbnail(it)).into(this) }
}

private fun ImageView.imageThumbnail(urls: String?): RequestBuilder<Drawable> {
    return Glide.with(context).load(urls)
}
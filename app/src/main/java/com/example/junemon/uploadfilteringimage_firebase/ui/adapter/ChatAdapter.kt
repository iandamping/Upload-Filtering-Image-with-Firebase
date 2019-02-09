package com.example.junemon.uploadfilteringimage_firebase.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import com.example.junemon.uploadfilteringimage_firebase.ui.adapter.viewholder.ChatReceiveViewholder
import com.example.junemon.uploadfilteringimage_firebase.ui.adapter.viewholder.ChatSentViewholder

class ChatAdapter(
    val ctx: Context?,
    var listMessage: List<UploadImageModel>,
    val listener: (UploadImageModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val receive = 1
    private val sent = 2

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        var vh: RecyclerView.ViewHolder? = null
        if (p1 == receive) {
            val view = LayoutInflater.from(ctx).inflate(R.layout.item_messages_received, p0, false)
            vh = ChatReceiveViewholder(view, ctx)
        } else if (p1 == sent) {
            val view = LayoutInflater.from(ctx).inflate(R.layout.item_messages_sent, p0, false)
            vh = ChatSentViewholder(view, ctx)
        }
        return vh!!
    }

    override fun getItemCount(): Int = listMessage.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
//        try {
//            if (p0 is ChatReceiveViewholder) {
//                val vh = p0
//                vh.bindViews(listMessage.get(p1), listener)
//            } else if (p0 is ChatSentViewholder) {
//                val vh = p0
//                vh.bindViews(listMessage.get(p1), listener)
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }
}
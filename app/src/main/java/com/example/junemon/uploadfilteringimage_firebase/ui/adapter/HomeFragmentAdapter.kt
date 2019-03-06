package com.example.junemon.uploadfilteringimage_firebase.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.storageDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home.HomeFragmentPresenter
import com.example.junemon.uploadfilteringimage_firebase.utils.getStringResources
import com.example.junemon.uploadfilteringimage_firebase.utils.inflates
import com.example.junemon.uploadfilteringimage_firebase.utils.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_fragment_home.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class HomeFragmentAdapter(
        var ctx: Context?,
        var listMessage: List<UploadImageModel>,
        val presenter: HomeFragmentPresenter,
        val listener: (UploadImageModel) -> Unit
) : RecyclerView.Adapter<HomeFragmentAdapter.viewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewHolder {
        return viewHolder(p0.inflates(R.layout.item_fragment_home), ctx, presenter)
    }

    override fun getItemCount(): Int = listMessage.size

    override fun onBindViewHolder(p0: viewHolder, p1: Int) {
        p0.bindView(listMessage.get(p1), listener)
    }

    class viewHolder(override val containerView: View, var ctx: Context?, val presenter: HomeFragmentPresenter) :
            RecyclerView.ViewHolder(containerView),
            LayoutContainer {
        fun bindView(data: UploadImageModel, listener: (UploadImageModel) -> Unit) {
            ivFirebaseImage.loadUrl(data.photoUrl)
            ivFirebaseProfileImage.loadUrl(data.userPhotoProfileUrl)
            tvFirebaseName.text = data.name
            tvFirebaseDesc.text = data.text

            ivDownloadImage.setOnClickListener {
                presenter.saveFirebaseImageToGallery(
                        storageDatabaseReference,
                        llMainItem,
                        data.userPhotoLastPathSegment
                )
            }

            ivShareImage.setOnClickListener {
                presenter.shareFirebaseImageThroughTelegram(data.userPhotoLastPathSegment)
            }
            ivDeleteData.setOnClickListener {
                ctx?.alert(ctx?.getStringResources(R.string.are_you_sure)!!) {
                    yesButton { presenter.deleteFirebaseImage(data.userPhotoLastPathSegment) }
                    noButton { it.dismiss() }
                    onCancelled { it.dismiss() }
                }?.show()
            }

            itemView.setOnClickListener {
                listener(data)
            }
        }

    }
}
package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.chat

import android.content.Context
import android.view.View
import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentPresenter
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import com.google.firebase.database.DatabaseReference

class ChatFragmentPresenter(var dataReference: DatabaseReference, var mView: ChatFragmentView) :
        BaseFragmentPresenter {
    private var ctx: Context? = null
    private lateinit var theContent: UploadImageModel

    override fun onAttach(context: Context?) {
        this.ctx = context
    }

    override fun onCreateView(view: View) {
        mView.initView(view)
    }


    fun sendUserMessage(message: String?, userName: String?) {
        theContent = UploadImageModel(message, userName, null, null, null)
        dataReference.push().setValue(theContent)
    }
}
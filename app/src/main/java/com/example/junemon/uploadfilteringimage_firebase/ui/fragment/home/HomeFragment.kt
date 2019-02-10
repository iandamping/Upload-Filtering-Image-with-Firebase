package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.mDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.userDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel

class HomeFragment : Fragment(), HomeFragmentView {
    private lateinit var presenter: HomeFragmentPresenter
    private var ctx: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
        presenter = HomeFragmentPresenter(mDatabaseReference, userDatabaseReference, this, this)
        presenter.onAttach(ctx)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.onGetDataback()
    }

    override fun onGetDataback(data: UploadImageModel?) {
        if (data != null) {
            Log.d(this::class.java.simpleName, data.name)
            Log.d(this::class.java.simpleName, data.text)
        }
    }

    override fun initView(view: View) {
        presenter.onCreateView(view)
    }

    fun initiateSignOut() {
        presenter.onSignOutAndCleanUp()
    }
}
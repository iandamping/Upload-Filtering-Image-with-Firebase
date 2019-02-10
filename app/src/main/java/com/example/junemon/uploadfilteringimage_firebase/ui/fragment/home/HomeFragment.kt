package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.mDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.userDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), HomeFragmentView {
    private lateinit var presenter: HomeFragmentPresenter
    private var ctx: Context? = null
    private val userKeyPass = "asdwafas"
    private var userName: String? = null

    fun newInstance(userName: String?): HomeFragment {
        val bundle = Bundle()
        val fragment = HomeFragment()
        bundle.putString(userKeyPass, userName)
        fragment.setArguments(bundle)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        userName = args?.getString(userKeyPass)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
        presenter = HomeFragmentPresenter(mDatabaseReference, userDatabaseReference, this, this)
        presenter.onAttach(ctx)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val views:View = inflater.inflate(R.layout.fragment_home, container,false)
        return views
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.onGetDataback()
    }

    override fun onGetDataback(data: UploadImageModel?) {
        if (data != null) {
            ctx?.let { Glide.with(it).load(data.photoUrl).into(ivFirebaseImage) }
            tvFirebaseName.text = data.text
            tvFirebaseChats.text = data.text
        }
    }

    override fun initView(view: View) {
        presenter.onCreateView(view)
    }

    fun initiateSignOut() {
        presenter.onSignOutAndCleanUp()
    }
}
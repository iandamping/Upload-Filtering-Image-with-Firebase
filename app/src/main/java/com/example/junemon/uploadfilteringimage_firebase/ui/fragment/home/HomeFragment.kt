package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.mDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.userDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.example.junemon.uploadfilteringimage_firebase.ui.adapter.HomeFragmentAdapter
import com.example.junemon.uploadfilteringimage_firebase.utils.alertHelper
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), HomeFragmentView {
    private lateinit var presenter: HomeFragmentPresenter
    private var ctx: Context? = null
    private val userKeyPass = "asdwafas"
    private var userData: UserModel? = null
    private var listAllData: MutableList<UploadImageModel> = mutableListOf()

    fun newInstance(userName: UserModel?): HomeFragment {
        val bundle = Bundle()
        val fragment = HomeFragment()
        bundle.putParcelable(userKeyPass, userName)
        fragment.setArguments(bundle)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        userData = args?.getParcelable(userKeyPass)
        if (userData?.name == null) {
            listAllData.clear()
            ctx?.alertHelper(ctx?.resources?.getString(R.string.login_failed)!!)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
        presenter = HomeFragmentPresenter(mDatabaseReference, userDatabaseReference, this, this)
        presenter.onAttach(ctx)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val views: View = inflater.inflate(R.layout.fragment_home, container, false)
        return views
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.onSignOutAndCleanUp(userData?.name)
        presenter.onGetDataback()
//        presenter.setAndGetDatauser(mFirebaseAuth.currentUser)
    }

    override fun onGetDataback(data: UploadImageModel?) {
        rvHomeFragment.layoutManager = LinearLayoutManager(ctx).also {
            it.reverseLayout = true
            it.stackFromEnd = true
        }
        if (data != null) {
            listAllData.add(data)
            rvHomeFragment.adapter = HomeFragmentAdapter(ctx, listAllData, presenter) {

            }
        }
    }

    override fun initView(view: View) {
        presenter.onCreateView(view)
    }
//
//    override fun ongetDatauser(user: FirebaseUser?) {
//
//    }
}
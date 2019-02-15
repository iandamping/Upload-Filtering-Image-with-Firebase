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
import com.example.junemon.uploadfilteringimage_firebase.ui.adapter.HomeFragmentAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class HomeFragment : Fragment(), HomeFragmentView {
    private lateinit var presenter: HomeFragmentPresenter
    private var ctx: Context? = null
    private val userKeyPass = "asdwafas"
    private var userName: String? = null
    private var listAllData: MutableList<UploadImageModel> = mutableListOf()

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
        if (userName == null) {
            listAllData.clear()
            ctx?.alert(ctx?.resources?.getString(R.string.login_failed)!!) {
                yesButton {
                    it.dismiss()
                }
            }?.show()
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
        presenter.onGetDataback()
//        presenter.setAndGetDatauser(mFirebaseAuth.currentUser)
        presenter.onSignOutAndCleanUp(userName)
    }

    override fun onGetDataback(data: UploadImageModel?) {
        rvHomeFragment.layoutManager = LinearLayoutManager(ctx).also {
            it.reverseLayout = true
            it.stackFromEnd = true
        }
        if (data != null) {
            listAllData.add(data)
            rvHomeFragment.adapter = HomeFragmentAdapter(ctx, listAllData) {
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
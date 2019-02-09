package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.RequestSignIn
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.userDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.R
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment(), ProfileFragmentView {
    private var ctx: Context? = null
    private val userKeyPass = "asdwafas"
    private var userName: String? = null
    private lateinit var presenter: ProfileFragmentPresenter
    private val loginProvider = arrayListOf(
        AuthUI.IdpConfig.FacebookBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )


    fun newInstance(userName: String?): ProfileFragment {
        val bundle = Bundle()
        val fragment = ProfileFragment()
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
        presenter = ProfileFragmentPresenter(userDatabaseReference, this, this)
        presenter.onAttach(ctx)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getUserData(userName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val views: View = inflater.inflate(R.layout.fragment_profile, container, false)
        presenter.onCreateView(views)
        return views
    }

    override fun initView(view: View) {
        view.btnLogin.setOnClickListener {
            createSignInIntent()
        }
        view.btnLogout.setOnClickListener {
            presenter.setLogoutData()
        }
    }


    private fun createSignInIntent() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(loginProvider)
                .build(),
            RequestSignIn
        )
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onGetDataBack(user: String?) {
        tvProfileName.text = user
    }

}
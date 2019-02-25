package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.userDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.RequestSignIn
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment(), ProfileFragmentView {
    private var ctx: Context? = null
    private val userKeyPass = "asdwafas"
    private var userData: UserModel? = UserModel()
    private lateinit var presenter: ProfileFragmentPresenter
    private val loginProvider = arrayListOf(
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
    )


    fun newInstance(users: UserModel?): ProfileFragment {
        val bundle = Bundle()
        val fragment = ProfileFragment()
        bundle.putParcelable(userKeyPass, users)
        fragment.setArguments(bundle)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        userData = args?.getParcelable(userKeyPass)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
        presenter = ProfileFragmentPresenter(userDatabaseReference, this, this)
        presenter.onAttach(ctx)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getUserData(userData)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val views: View = inflater.inflate(R.layout.fragment_profile, container, false)
        presenter.onCreateView(views)
        return views
    }

    override fun initView(view: View) {
        ctx?.let { Glide.with(it).load(userData?.userPhotoUrl).into(view.tvPhotoProfileUser) }
        view.tvProfileName.text = userData?.name
        view.tvProfileEmail.text = userData?.email
        view.tvProfilePhoneNumber.text = userData?.phoneNumber ?: "-"

        if (userData?.name.isNullOrEmpty()) {
            view.btnLogin.visibility = View.VISIBLE
        } else {
            view.btnLogout.visibility = View.VISIBLE
            view.llProfile.visibility = View.VISIBLE
        }


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
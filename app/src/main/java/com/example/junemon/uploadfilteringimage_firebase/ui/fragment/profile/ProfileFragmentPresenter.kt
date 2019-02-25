package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.junemon.uploadfilteringimage_firebase.MainApplication
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.gson
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.mFirebaseAuth
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.prefHelper
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentPresenter
import com.example.junemon.uploadfilteringimage_firebase.data.ProfileViewModel
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.example.junemon.uploadfilteringimage_firebase.ui.activity.main.MainAppbarActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class ProfileFragmentPresenter(
        var mDatabaseReference: DatabaseReference,
        var mView: ProfileFragmentView,
        var target: Fragment
) : BaseFragmentPresenter {

    private var ctx: Context? = null
    private lateinit var listener: FirebaseAuth.AuthStateListener
    private lateinit var userFromFirebase: UserModel
    private lateinit var intentToMainActivity: Intent
    private var jsonModelToString: String? = null
    private var currentUser: FirebaseUser? = null
    private var vm: ProfileViewModel? = null
    private var userToken: String? = null

    override fun onAttach(context: Context?) {
        this.ctx = context
        userPrivateToken()
        currentUser = mFirebaseAuth.currentUser
        intentToMainActivity = Intent(ctx, MainAppbarActivity::class.java)
        vm = ViewModelProviders.of(target).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(view: View) {
        mView.initView(view)
    }


    fun onResume() {
        if (listener != null) {
            mFirebaseAuth.addAuthStateListener(listener)
        }
    }

    fun onPause() {
        if (listener != null) {
            mFirebaseAuth.removeAuthStateListener(listener)
        }
    }

    fun setLogoutData() {
        ctx?.let { AuthUI.getInstance().signOut(it) }
        prefHelper.setUserLoginState(ctx?.resources?.getString(R.string.user_logout))
        ctx?.startActivity(intentToMainActivity)
    }

    fun getUserData(users: UserModel?) {
        listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth != null) {
                if (users?.name.isNullOrEmpty()) {
                    vm?.setFirebaseUser(firebaseAuth.currentUser)
                } else {
                    mView.onGetDataBack(users?.name)
                }

                vm?.getFirebaseUser()?.observe(target.viewLifecycleOwner, Observer {
                    if (it != null) {
                        userFromFirebase =
                                UserModel(it.displayName, it.email, it.photoUrl.toString(), userToken, it.phoneNumber)
                        mDatabaseReference.child(it.uid).setValue(userFromFirebase)
                        jsonModelToString = gson.toJson(userFromFirebase)
                        prefHelper.setUserLoginState(jsonModelToString)
                        ctx?.startActivity(intentToMainActivity)
                    }
                })
            } else {
                ctx?.alert(ctx?.resources?.getString(R.string.login_failed)!!) {
                    yesButton {
                        it.dismiss()
                    }
                }?.show()
            }
        }
    }

    private fun userPrivateToken() {
        MainApplication.mFirebaseInstanceId.instanceId.addOnSuccessListener {
            userToken = it.token
        }
    }

}
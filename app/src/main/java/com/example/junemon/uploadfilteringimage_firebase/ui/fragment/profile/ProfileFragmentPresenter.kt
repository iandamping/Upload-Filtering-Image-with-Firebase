package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.KEY
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.gson
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.mFirebaseAuth
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.prefToken
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentPresenter
import com.example.junemon.uploadfilteringimage_firebase.data.ProfileViewModel
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class ProfileFragmentPresenter(
    var mDatabaseReference: DatabaseReference,
    var mView: ProfileFragmentView,
    var target: Fragment
) : BaseFragmentPresenter {

    private var ctx: Context? = null
    private lateinit var listener: FirebaseAuth.AuthStateListener
    private lateinit var userFromFirebase: UserModel
    private var jsonModelToString: String? = null
    private var currentUser: FirebaseUser? = null
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var vm: ProfileViewModel? = null

    override fun onAttach(context: Context?) {
        this.ctx = context
        currentUser = mFirebaseAuth.currentUser
        pref = ctx?.applicationContext?.getSharedPreferences(prefToken, Context.MODE_PRIVATE)
        editor = pref?.edit()
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

    fun getUserData(username: String?) {
        listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth != null) {
                if (username.isNullOrEmpty()) {
                    vm?.setFirebaseUser(firebaseAuth.currentUser)
                } else {
                    mView.onGetDataBack(username)
                }

                vm?.getFirebaseUser()?.observe(target, Observer {
                    if (it != null) {
                        userFromFirebase = UserModel(it.displayName, it.email)
                        mDatabaseReference.child(it.uid).setValue(userFromFirebase)
                        jsonModelToString = gson.toJson(userFromFirebase)
                        editor?.putString(KEY, jsonModelToString)
                        editor?.apply()
                    } else {
                        Toast.makeText(
                            ctx,
                            ctx?.resources?.getString(R.string.login_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(ctx, ctx?.resources?.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

}
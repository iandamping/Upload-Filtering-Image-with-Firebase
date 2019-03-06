package com.example.junemon.uploadfilteringimage_firebase.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.example.junemon.uploadfilteringimage_firebase.ui.activity.upload.UploadActivity
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home.HomeFragment
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile.ProfileFragment
import com.example.junemon.uploadfilteringimage_firebase.utils.*
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.RequestSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, MainActivityView {
    private lateinit var presenter: MainActivityPresenter
    private var userData: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainActivityPresenter(this)
        presenter.onCreate(this)
        presenter.getUserData()
        initBottomNav()

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.NavigationHome -> {
                supportFragmentManager.switchFragment(null, HomeFragment().newInstance(userData))
                true
            }
            R.id.NavigationChat -> {
                if (userData?.name != null) {
                    startActivityWithParcel<UploadActivity>(Constant.userPassedkey, userData)
                } else {
                    alertHelper(getStringResources(R.string.login_failed))
                }
                true
            }
            R.id.NavigationProfile -> {
                supportFragmentManager.switchFragment(null, ProfileFragment().newInstance(userData))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun initBottomNav() {
        bottom_navigation.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.switchFragment(null, HomeFragment().newInstance(userData))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestSignIn) {
            if (resultCode == Activity.RESULT_OK) {
//                sharedLoadDesiredFragment(null, supportFragmentManager, ProfileFragment())
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onGetDataBack(user: UserModel?) {
        userData = user
    }

    override fun initView() {
    }
}

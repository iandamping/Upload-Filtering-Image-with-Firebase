package com.example.junemon.uploadfilteringimage_firebase.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.RequestSignIn
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.sharedLoadDesiredFragment
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.chat.ChatFragment
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home.HomeFragment
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, MainActivityView {
    private lateinit var presenter: MainActivityPresenter
    //    private var vm: ProfileViewModel? = null
    private var userName: String? = null

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
                true
            }
            R.id.NavigationChat -> {
//                sharedLoadDesiredFragment(null, supportFragmentManager, ChatFragment())
                LoadChatFragmentWithValue(null, userName)
                true
            }
            R.id.NavigationProfile -> {
//                sharedLoadDesiredFragment(null, supportFragmentManager, ProfileFragment())
                LoadProfileFragmentWithValue(null, userName)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun LoadChatFragmentWithValue(savedInstanceState: Bundle?, username: String?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, ChatFragment().newInstance(username))
                .commit()
        }
    }

    private fun LoadProfileFragmentWithValue(savedInstanceState: Bundle?, username: String?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, ProfileFragment().newInstance(username))
                .commit()
        }
    }

    private fun initBottomNav() {
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment())
            .commit()
    }

//    private fun getUserData() {
//        vm = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
//        vm?.getFirebaseUser()?.observe(this, Observer {
//            userName = it.displayName
//        })
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestSignIn) {
            if (resultCode == Activity.RESULT_OK) {
                sharedLoadDesiredFragment(null, supportFragmentManager, ProfileFragment())
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onGetDataBack(user: UserModel?) {
        userName = user?.name
    }

    override fun onFailedGetDataBack(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun initView() {
    }
}

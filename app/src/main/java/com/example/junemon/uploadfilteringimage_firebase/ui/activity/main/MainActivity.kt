package com.example.junemon.uploadfilteringimage_firebase.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.sharedLoadDesiredFragment
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.example.junemon.uploadfilteringimage_firebase.ui.activity.upload.UploadActivity
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home.HomeFragment
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile.ProfileFragment
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.RequestSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        MainActivityView {
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
                sharedLoadDesiredFragment(null, supportFragmentManager, HomeFragment().newInstance(userData?.name))
                true
            }
            R.id.NavigationChat -> {
                if (userData?.name != null) {
                    val i = Intent(this, UploadActivity::class.java)
                    i.putExtra("testing", userData?.name)
                    startActivity(i)
                } else {
                    alert(resources.getString(R.string.login_failed)) {
                        yesButton {
                            it.dismiss()
                        }
                    }.show()
                }
                true
            }
            R.id.NavigationProfile -> {
                sharedLoadDesiredFragment(null, supportFragmentManager, ProfileFragment().newInstance(userData))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun initBottomNav() {
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment().newInstance(userData?.name))
                .commit()
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

//    override fun onFailedGetDataBack(message: String?) {
//        alert(message.toString()) {
//            yesButton {
//                it.dismiss()
//            }
//        }.show()
//    }

    override fun initView() {
    }
}

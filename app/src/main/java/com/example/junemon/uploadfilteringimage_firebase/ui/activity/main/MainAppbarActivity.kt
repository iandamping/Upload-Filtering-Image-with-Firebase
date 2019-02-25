package com.example.junemon.uploadfilteringimage_firebase.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.junemon.uploadfilteringimage_firebase.MainApplication
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.model.UserModel
import com.example.junemon.uploadfilteringimage_firebase.ui.activity.upload.UploadActivity
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home.HomeFragment
import com.example.junemon.uploadfilteringimage_firebase.ui.fragment.profile.ProfileFragment
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.RequestSignIn
import kotlinx.android.synthetic.main.activity_bottom_appbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class MainAppbarActivity : AppCompatActivity(), MainActivityView {
    private lateinit var presenter: MainActivityPresenter
    private var userData: UserModel? = null
    private var userToken: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_appbar)
        setSupportActionBar(btmAppBar)
        presenter = MainActivityPresenter(this)
        presenter.onCreate(this)
        presenter.getUserData()

        initBottomNav()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.NavigationHome -> {
                MainApplication.sharedLoadDesiredFragment(
                        null,
                        supportFragmentManager,
                        HomeFragment().newInstance(userData?.name)
                )
                true
            }
            R.id.NavigationProfile -> {
                MainApplication.sharedLoadDesiredFragment(
                        null,
                        supportFragmentManager,
                        ProfileFragment().newInstance(userData)
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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
        if (user != null) {
            userData = user
            userData?.userToken = userToken
        }
    }

    override fun initView() {
        fabAppBar.setOnClickListener {
            if (userData?.name != null) {
                val i = Intent(this, UploadActivity::class.java)
                i.putExtra("userName", userData?.name)
                i.putExtra("userPhoto", userData?.userPhotoUrl)
                startActivity(i)
            } else {
                alert(resources.getString(R.string.login_failed)) {
                    yesButton {
                        it.dismiss()
                    }
                }.show()
            }
        }
    }

    private fun initBottomNav() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, HomeFragment().newInstance(userData?.name))
                .commit()
    }

}
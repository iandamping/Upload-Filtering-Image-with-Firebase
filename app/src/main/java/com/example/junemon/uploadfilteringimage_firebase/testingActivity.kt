package com.example.junemon.uploadfilteringimage_firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class testingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testing_deeplink)
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        val action = intent?.action
        val data = intent?.dataString
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            Log.d(this::class.java.simpleName, data + "ini ayam")
//            tvDeeplink.visibility = View.VISIBLE
        }
    }

}
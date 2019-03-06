package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.sentNotificationFirebase
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentPresenter
import com.example.junemon.uploadfilteringimage_firebase.data.HomeViewmodel
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.userLogin
import com.example.junemon.uploadfilteringimage_firebase.utils.Constant.userLogout
import com.example.junemon.uploadfilteringimage_firebase.utils.ImageUtils
import com.example.junemon.uploadfilteringimage_firebase.utils.deleteSpesificFirebaseData
import com.example.junemon.uploadfilteringimage_firebase.utils.getAllData
import com.example.junemon.uploadfilteringimage_firebase.utils.withViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.storage.StorageReference
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject


class HomeFragmentPresenter(var dataReference: DatabaseReference, var userDataReference: DatabaseReference, var mView: HomeFragmentView, var target: Fragment) : BaseFragmentPresenter {
    private var ctx: Context? = null
    private lateinit var utils: ImageUtils
    private var json = JSONObject()
    private var dataJson = JSONObject()
    private lateinit var compose: CompositeDisposable

    override fun onAttach(context: Context?) {
        this.ctx = context
        utils = ImageUtils(ctx)
        target.getAllData(dataReference, userLogin)
        compose = CompositeDisposable()
    }

    override fun onCreateView(view: View) {
        mView.initView(view)
    }

    fun onSignOutAndCleanUp(username: String?) {
        if (username == null) {
            target.getAllData(dataReference, userLogout)
            target.getAllData(userDataReference, userLogout)
        }
    }

    fun onGetDataback() {
        target.withViewModel<HomeViewmodel> {
            this.getImageData()?.observe(target.viewLifecycleOwner, Observer {
                mView.onGetDataback(it)
            })
        }
    }

    fun deleteFirebaseImage(segment: String?) {
        val dialogs = ctx?.indeterminateProgressDialog(
                ctx?.resources?.getString(R.string.please_wait),
                ctx?.resources?.getString(R.string.removing_image)
        )
        val data: Query? = dataReference.orderByChild("userPhotoLastPathSegment").equalTo(segment)
        ctx?.deleteSpesificFirebaseData(data, dialogs)

    }

    fun saveFirebaseImageToGallery(storageReference: StorageReference, views: View, lastPathSegment: String?) {
        utils.saveFirebaseImageToGallery(storageReference, views, lastPathSegment)
    }

    fun shareFirebaseImageThroughTelegram(lastPathSegment: String?) {
        utils.shareFirebaseImageThroughTelegram(lastPathSegment)
    }

    fun sentNotification(textPassed: String?, tittlePassed: String?, userToken: String?) {
        dataJson.put("text", textPassed)
        dataJson.put("tittle", tittlePassed)
        dataJson.put("priority", "high")
        json.put("notification", dataJson)
        json.put("to", userToken)
        compose.add(
                sentNotificationFirebase.sentNotification(json).subscribeOn(Schedulers.io())
                        .subscribe({ Toast.makeText(ctx, "Message done", Toast.LENGTH_SHORT).show() },
                                { Log.e(this::class.java.simpleName, it.localizedMessage) })
        )

    }

}

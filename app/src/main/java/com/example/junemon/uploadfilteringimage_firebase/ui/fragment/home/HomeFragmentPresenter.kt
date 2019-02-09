package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.home

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.junemon.uploadfilteringimage_firebase.base.BaseFragmentPresenter
import com.example.junemon.uploadfilteringimage_firebase.data.HomeViewmodel
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class HomeFragmentPresenter(
    var dataReference: DatabaseReference,
    var userDataReference: DatabaseReference,
    var mView: HomeFragmentView,
    var target: Fragment
) :
    BaseFragmentPresenter, ChildEventListener {
    private lateinit var uploadModel: UploadImageModel
    private lateinit var vm: HomeViewmodel

    override fun onAttach(context: Context?) {
        dataReference.addChildEventListener(this)
        uploadModel = UploadImageModel()
        vm = ViewModelProviders.of(target).get(HomeViewmodel::class.java)

    }

    override fun onCreateView(view: View) {
        mView.initView(view)
    }

    fun onSignOutAndCleanUp() {
        dataReference.removeEventListener(this)
        userDataReference.removeEventListener(this)
    }

    override fun onCancelled(p0: DatabaseError) {
        //ini method yang di panggil ketika ada masalah biasanya karena tidak punya akses untuk membaca data
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        //ini method yang di panggil ketika data merubah posisi nya pada list
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        //ini method yang di panggil ketika ada perubahan pada data
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        //ini method yang pertama kali dipanggil ketika ada penambahan data
        vm.setImageData(p0.getValue(UploadImageModel::class.java)!!)
    }

    override fun onChildRemoved(p0: DataSnapshot) {
        //ini method yang di panggil ketika menghapus data
    }

    fun onGetDataback() {
        vm.getImageData()?.observe(target, Observer {
            mView.onGetDataback(it)
        })
    }
}
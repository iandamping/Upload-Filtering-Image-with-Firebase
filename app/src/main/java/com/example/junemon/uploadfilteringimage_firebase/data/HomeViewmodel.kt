package com.example.junemon.uploadfilteringimage_firebase.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.junemon.uploadfilteringimage_firebase.model.UploadImageModel

class HomeViewmodel : ViewModel() {
    private var imageData: MutableLiveData<UploadImageModel>? = MutableLiveData()

    fun setImageData(data: UploadImageModel) {
        imageData?.value = data
    }

    fun getImageData(): MutableLiveData<UploadImageModel>? = imageData
}
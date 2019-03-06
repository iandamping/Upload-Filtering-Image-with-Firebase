package com.example.junemon.uploadfilteringimage_firebase.api

import io.reactivex.Completable
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST
    fun sentNotification(@Body jsonObject: JSONObject): Completable
}
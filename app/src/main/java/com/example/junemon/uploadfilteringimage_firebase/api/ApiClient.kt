package com.example.junemon.uploadfilteringimage_firebase.api

import android.content.Context
import android.os.Build
import com.example.junemon.uploadfilteringimage_firebase.BuildConfig
import com.example.junemon.uploadfilteringimage_firebase.R
import com.example.junemon.uploadfilteringimage_firebase.api.ApiConfig.Companion.baseUrl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    val gson: Gson = GsonBuilder().setLenient().create()
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
//        private val client = OkHttpClient().newBuilder()
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
//            })
//            .build()

    private fun createClient(ctx: Context, withHeader: Boolean): Retrofit {
        retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient(ctx, withHeader))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .build()
        return retrofit
    }

    private fun getOkHttpClient(ctx: Context, withHeader: Boolean): OkHttpClient {
        val cacheSize = 20 * 1024 * 1024 // 10 MB
        val cache = Cache(ctx.cacheDir, cacheSize.toLong())
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 20
        dispatcher.maxRequestsPerHost = 20

        val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .dispatcher(dispatcher)

        if (withHeader) {
            okHttpBuilder.addInterceptor { chain ->
                val ongoing = chain.request().newBuilder()
                ongoing.addHeader(ctx.resources.getString(R.string.retrofit_header1), ctx.resources.getString(R.string.fcm_key))
                ongoing.addHeader(ctx.resources.getString(R.string.retrofit_header2), ctx.resources.getString(R.string.retrofit_value_header2))
                //ongoing.addHeader(Constant.NEW_X_OC_MERCHANT_ID, Constant.NEW_X_OC_MERCHANT_VALUE);
                chain.proceed(ongoing.build())
            }
        }

        if (BuildConfig.DEBUG) {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.addInterceptor(interceptor)
        }

        okHttpClient = enableTls12OnPreLollipop(okHttpBuilder).build()
        return okHttpClient

    }

    private fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_0)
                    .allEnabledCipherSuites()
                    .build()
            client.connectionSpecs(listOf(spec))
        }

        return client
    }

    fun createRequest(ctx: Context): ApiInterface {
        return createClient(ctx, true).create(ApiInterface::class.java)
    }

}
package com.learning.common.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://www.wanandroid.com"
const val CONNECT_TIMEOUT: Long = 5000
const val WRITE_TIMEOUT: Long = 5000
const val READ_TIMEOUT: Long = 5000

object RetrofitClient {
    private var mRetrofit: Retrofit

    fun <T : Any> create(clazz: Class<T>): T = mRetrofit.create(clazz)

    init {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(setOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun setOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .proxy(Proxy.NO_PROXY)
            .build()
    }
}
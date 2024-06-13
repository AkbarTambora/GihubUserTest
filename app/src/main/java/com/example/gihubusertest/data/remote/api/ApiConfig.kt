package com.example.gihubusertest.data.remote.api

import com.example.gihubusertest.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(): ApiService {
        val BASE_URL = "https://api.github.com/"

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", BuildConfig.token)
                .build()
            chain.proceed(requestHeaders)
        }
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}



//    private const val BASE_URL = "https://api.github.com/"
//
//    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//
//    private val client = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
//        .build()
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
//        .build()
//
//    val apiServiceInstance: ApiService = retrofit.create(ApiService::class.java)

package com.skinective.network.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIService {
    private const val BASE_URL = "https://skinective-backend-api-sss3kmtsda-et.a.run.app"
    private const val BASE_URL_ML = "https://skinective-ml-api-sss3kmtsda-et.a.run.app"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitML: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ML)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    fun getService(): AuthService {
        return authService
    }

    fun getAPIConsumer(): APIConsumer {
        return retrofit.create(APIConsumer::class.java)
    }

    fun getAPIML(): APIML {
        return retrofitML.create(APIML::class.java)
    }
}

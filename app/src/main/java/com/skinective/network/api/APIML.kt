package com.skinective.network.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface APIML {

        @Multipart
        @POST("/detect/{userId}")
        fun detect(@Part file: MultipartBody.Part, @Path("userId") userId: String): Call<DetectResponse>

        @GET("/detect/history/{userId}")
        fun getHistoryDetect(@Path("userId") userId: String): Call<HistoryDetectResponse>

    }

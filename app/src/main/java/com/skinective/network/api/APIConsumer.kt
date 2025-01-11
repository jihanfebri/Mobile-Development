package com.skinective.network.api

import com.skinective.network.responses.ArticleResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

    interface APIConsumer {

        @GET("/users")
        fun getUsers(): Call<List<User>>

        @GET("/users/{userId}")
        fun getUserById(@Path("userId") userId: String): Call<User>

        @PUT("/users/{userId}/changeDetails")
        fun updateUserDetails(
            @Path("userId") userId: String,
            @Body request: UpdateUserDetailsRequest
        ): Call<Void>

        @POST("/users/{userId}/changePassword")
        fun changePassword(
            @Path("userId") userId: String,
            @Body request: ChangePasswordRequest
        ): Call<Void>

        @DELETE("/users/{userId}/delete")
        fun deleteUser(@Path("userId") userId: String): Call<Void>

        @GET("/articles")
        fun getArticles(): Call<ArticleResponse>

        @GET("/articles/{articleId}")
        fun getArticleById(@Path("articleId") articleId: String): Call<Article>

        @POST("/articles/{userId}/create")
        fun createArticle(
            @Path("userId") userId: String,
            @Body request: CreateArticleRequest
        ): Call<Void>

        @DELETE("/articles/{userId}/delete")
        fun deleteArticle(
            @Path("userId") userId: String,
            @Query("articleId") articleId: Int
        ): Call<Void>

        @GET("/diseases")
        fun getDiseases(): Call<List<Disease>>

        @GET("/diseases/{diseaseId}")
        fun getDiseaseById(@Path("diseaseId") diseaseId: Int): Call<Disease>

        @POST("/disease/{userId}/add")
        fun addDisease(
            @Path("userId") userId: String,
            @Body request: AddDiseaseRequest
        ): Call<Void>
    }

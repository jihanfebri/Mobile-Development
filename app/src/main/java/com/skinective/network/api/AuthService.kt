package com.skinective.network.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.PUT

interface AuthService {
    @POST("/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>


    @POST("/login")
    fun loginUser(@Body request: LoginRequest): Call<UserResponse>


    @GET("/users/{userId}")
    fun getUserById(@Path("userId") userId: String): Call<UserResponse>

    @PUT("/users/{userId}/changeDetails")
    fun updateProfile(@Path("userId") userId: String, @Body request: UpdateProfileRequest): Call<UserResponse>


}

data class RegisterRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("fName")
    val firstName: String,

    @SerializedName("lName")
    val lastName: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String,

    val age: Int
)

data class RegisterResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)

data class UserResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: User
)

data class LoginRequest(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)

data class UpdateProfileRequest(
    val email: String,
    val fName: String,
    val lName: String
)

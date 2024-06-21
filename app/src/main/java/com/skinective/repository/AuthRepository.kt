package com.skinective.repository

import com.skinective.network.api.AuthService
import com.skinective.network.api.LoginRequest
import com.skinective.network.api.RegisterRequest
import com.skinective.network.api.RegisterResponse
import com.skinective.network.api.User
import com.skinective.network.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val authService: AuthService) {

    fun registerUser(email: String, firstName: String, lastName: String, password: String, confirmPassword: String, age: Int, callback: (Boolean, String?) -> Unit) {

        val request = RegisterRequest(email, firstName, lastName, password, confirmPassword, age)
        authService.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null && registerResponse.status) {
                        callback(true, null) // Registrasi berhasil
                    } else {
                        val message = registerResponse?.message ?: "Unknown error occurred"
                        callback(false, message)
                    }
                } else {
                    val errorMessage = "Server error: ${response.code()}"
                    callback(false, errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                val errorMessage = t.message ?: "Unknown error occurred"
                callback(false, errorMessage)
            }
        })
    }

    fun loginUser(password: String, email: String, callback: (Boolean, String?, User?) -> Unit) {
        val request = LoginRequest(password, email)
        authService.loginUser(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.status) {
                        callback(true, null, loginResponse.data)
                    } else {
                        val message = loginResponse?.message ?: "Unknown error occurred"
                        callback(false, message, loginResponse?.data)
                    }
                } else {
                    val errorMessage = "Server error: ${response.code()}"
                    callback(false, errorMessage, null)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                val errorMessage = t.message ?: "Unknown error occurred"
                callback(false, errorMessage, null)
            }
        })
    }
}

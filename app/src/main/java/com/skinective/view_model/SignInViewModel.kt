package com.skinective.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skinective.network.api.User
import com.skinective.repository.AuthRepository

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    private val isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private val errorMessage: MutableLiveData<String> = MutableLiveData()
    private val loginResult: MutableLiveData<Boolean> = MutableLiveData()
    private val userResult: MutableLiveData<User?> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getLoginResult(): LiveData<Boolean> = loginResult
    fun getUserResult(): LiveData<User?> = userResult

    fun loginUser(password: String, email: String) {
        isLoading.value = true

        authRepository.loginUser(password, email) { isSuccess, message, data ->
            isLoading.value = false

            if (isSuccess) {
                loginResult.value = true
                userResult.value = data
            } else {
                errorMessage.value = message ?: "Unknown error occurred"
            }
        }
    }
}

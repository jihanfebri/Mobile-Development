package com.skinective.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skinective.repository.AuthRepository

class SignUpActivityViewModel(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    private val isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private val errorMessage: MutableLiveData<String> = MutableLiveData()
    private val registrationResult: MutableLiveData<Boolean> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<String> = errorMessage
    fun getRegistrationResult(): LiveData<Boolean> = registrationResult

    fun registerUser(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        confirmPassword: String,
        age: Int
    ) {
        isLoading.value = true

        authRepository.registerUser(
            email,
            firstName,
            lastName,
            password,
            confirmPassword,
            age
        ) { isSuccess, message ->
            isLoading.value = false

            if (isSuccess) {
                registrationResult.value = true // Registrasi berhasil
            } else {
                errorMessage.value = message ?: "Unknown error occurred"
            }
        }
    }
}

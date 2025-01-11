package com.skinective.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skinective.repository.AuthRepository
import java.security.InvalidParameterException

class SignUpActivityViewModelFactory(private val authRepository: AuthRepository, private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpActivityViewModel::class.java)){
            return SignUpActivityViewModel(authRepository, application) as T
        } else if (modelClass.isAssignableFrom(SignInViewModel::class.java)){
            return SignInViewModel(authRepository, application) as T
        }
        throw InvalidParameterException("Unable to Construct SignUpActivityViewModel")
    }
}

package com.skinective.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.skinective.databinding.ActivitySignUpBinding
import com.skinective.network.api.APIService
import com.skinective.repository.AuthRepository
import com.skinective.view_model.SignUpActivityViewModel
import com.skinective.view_model.SignUpActivityViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authService = APIService.getService()
        val authRepository = AuthRepository(authService)
        viewModel = ViewModelProvider(this, SignUpActivityViewModelFactory(authRepository, application))
            .get(SignUpActivityViewModel::class.java)

        viewModel.getIsLoading().observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.getErrorMessage().observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getRegistrationResult().observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                // Navigate to the next screen or perform desired action on successful registration
            }
        })

        // Handling sign up button click
        binding.btnSignup.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val firstName = binding.inputFirstName.text.toString().trim()
            val lastName = binding.inputLastName.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

            // Perform basic validation here if needed
            if (validateInput(email, firstName, lastName, password, confirmPassword)) {
                viewModel.registerUser(email, firstName, lastName, password, confirmPassword)
            }
        }

        // Handling sign in link click
        binding.toRegister.setOnClickListener {
            // Handle navigation to Sign In screen
            finish()
        }
    }

    private fun validateInput(email: String, firstName: String, lastName: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            binding.edtEmail.error = "Email is required"
            binding.edtEmail.requestFocus()
            return false
        }

        if (firstName.isEmpty()) {
            binding.inputFirstName.error = "First name is required"
            binding.inputFirstName.requestFocus()
            return false
        }

        if (lastName.isEmpty()) {
            binding.inputLastName.error = "Last name is required"
            binding.inputLastName.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.edtPassword.error = "Password is required"
            binding.edtPassword.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            binding.edtConfirmPassword.error = "Confirm password is required"
            binding.edtConfirmPassword.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            binding.edtConfirmPassword.error = "Passwords do not match"
            binding.edtConfirmPassword.requestFocus()
            return false
        }

        return true
    }
}

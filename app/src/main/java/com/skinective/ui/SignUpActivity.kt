package com.skinective.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.skinective.databinding.ActivitySignInBinding
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
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val authService = APIService.getService()
        val authRepository = AuthRepository(authService)
        viewModel =
            ViewModelProvider(this, SignUpActivityViewModelFactory(authRepository, application))[SignUpActivityViewModel::class.java]

        viewModel.getIsLoading().observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.getErrorMessage().observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getRegistrationResult().observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }

        // Handling sign up button click
        binding.btnSignup.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val firstName = binding.inputFirstName.text.toString().trim()
            val lastName = binding.inputLastName.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()
            val age = binding.edtAge.text.toString().toInt()

            // Perform basic validation here if needed
            if (validateInput(email, firstName, lastName, password, confirmPassword, age)) {
                viewModel.registerUser(email, firstName, lastName, password, confirmPassword, age)
            }
        }

        // Handling sign in link click
        binding.toRegister.setOnClickListener {
            // Handle navigation to Sign In screen
            finish()
        }
    }

    private fun validateInput(email: String, firstName: String, lastName: String, password: String, confirmPassword: String, age: Int): Boolean {
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

        if (age.toString().isEmpty()) {
            binding.edtAge.error = "Age is required"
            binding.edtAge.requestFocus()
            return false
        }

        return true
    }
}

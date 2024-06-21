package com.skinective.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.skinective.databinding.ActivitySignInBinding
import com.skinective.network.api.APIService
import com.skinective.repository.AuthRepository
import com.skinective.utils.Constants
import com.skinective.view_model.SignInViewModel
import com.skinective.view_model.SignUpActivityViewModelFactory

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged in
        val sharedPreferences = getSharedPreferences("com.skinective.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val lastLoginTime = sharedPreferences.getLong("LAST_LOGIN_TIME", 0)
        if (System.currentTimeMillis() - lastLoginTime < 3600000) {
            navigateToMain()
        }

        // Apply edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ViewModel
        val authService = APIService.getService()
        val authRepository = AuthRepository(authService)
        viewModel = ViewModelProvider(
            this,
            SignUpActivityViewModelFactory(authRepository, application)
        )[SignInViewModel::class.java]

        // Observe ViewModel LiveData
        observeViewModel()

        // Set up button click listeners
        binding.toRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (validateInput(email, password)) {
                viewModel.loginUser(password, email)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getIsLoading().observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.getErrorMessage().observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getLoginResult().observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val sharedPreferences = getSharedPreferences("com.skinective.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putLong("LAST_LOGIN_TIME", System.currentTimeMillis())
                    apply()
                }
                navigateToMain()
            }
        }

        viewModel.getUserResult().observe(this) { user ->
            Constants.USER = user
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.edtEmail.error = "Email is required"
                binding.edtEmail.requestFocus()
                false
            }
            password.isEmpty() -> {
                binding.edtPassword.error = "Password is required"
                binding.edtPassword.requestFocus()
                false
            }
            else -> true
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}

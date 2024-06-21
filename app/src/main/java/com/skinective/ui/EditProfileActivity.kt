package com.skinective.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.skinective.R
import com.skinective.databinding.ActivityEditProfileBinding
import com.skinective.network.api.APIService
import com.skinective.network.api.UpdateProfileRequest
import com.skinective.network.api.User
import com.skinective.network.api.UserResponse
import com.skinective.ui.HomeFragment.Companion.loadImage
import com.skinective.utils.Constants

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        intent?.getParcelableExtra<User>("user")?.let {
            binding.profileImage.loadImage(it.userImgUrl)
            binding.etFirstname.setText(it.userFName)
            binding.etLastname.setText(it.userLName)
            binding.etEmail.setText(it.userEmail)
        }

        binding.btnSave.setOnClickListener {
            when {
                binding.etFirstname.text.toString().isEmpty() -> {
                    binding.etFirstname.error = "First name is required"
                }

                binding.etLastname.text.toString().isEmpty() -> {
                    binding.etLastname.error = "Last name is required"
                }

                binding.etEmail.text.toString().isEmpty() -> {
                    binding.etEmail.error = "Email is required"
                }

                else -> {
                    binding.progressBar.isVisible = true
                    val apiService = APIService.getService().updateProfile(
                        Constants.USER?.userId.orEmpty(),
                        UpdateProfileRequest(
                            binding.etEmail.text.toString(),
                            binding.etFirstname.text.toString(),
                            binding.etLastname.text.toString()
                        )
                    )
                    apiService.enqueue(object : retrofit2.Callback<UserResponse> {
                        override fun onResponse(call: retrofit2.Call<UserResponse>, response: retrofit2.Response<UserResponse>) {
                            binding.progressBar.isVisible = false
                            if (response.isSuccessful) {
                                Toast.makeText(this@EditProfileActivity, "Success update profile", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@EditProfileActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<UserResponse>, t: Throwable) {
                            binding.progressBar.isVisible = false
                            Toast.makeText(this@EditProfileActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}
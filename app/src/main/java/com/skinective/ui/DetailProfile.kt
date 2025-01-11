package com.skinective.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skinective.R
import com.skinective.databinding.ActivityDetailProfileBinding
import com.skinective.network.api.APIService
import com.skinective.network.api.User
import com.skinective.network.api.UserResponse
import com.skinective.ui.HomeFragment.Companion.loadImage
import com.skinective.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProfile : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backButton.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        getDetailProfile()
    }

    private fun getDetailProfile() {
        val apiService = APIService.getService().getUserById(Constants.USER?.userId.orEmpty())
        apiService.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()

                    binding.tvFirstName.text = user?.data?.userFName
                    binding.tvLastName.text = user?.data?.userLName
                    binding.tvEmail.text = user?.data?.userEmail
                    binding.profileImage.loadImage(user?.data?.userImgUrl.orEmpty())

                    binding.editProfileButton.setOnClickListener { startActivity(Intent(this@DetailProfile, EditProfileActivity::class.java).apply {
                        putExtra("user", User(
                            userFName = binding.tvFirstName.text.toString(),
                            userLName = binding.tvLastName.text.toString(),
                            userEmail = binding.tvEmail.text.toString(),
                            userImgUrl = binding.profileImage.toString()
                        ))
                    }) }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // handle error
            }
        })
    }

}
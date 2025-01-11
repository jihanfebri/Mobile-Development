package com.skinective.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skinective.R
import com.skinective.databinding.ActivityProfileBinding
import com.skinective.network.api.APIService
import com.skinective.network.api.UserResponse
import com.skinective.ui.HomeFragment.Companion.loadImage
import com.skinective.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.llSignOut.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            Constants.USER = null
            startActivity(intent)
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
                    binding.tvProfileEmail.text = user?.data?.userEmail
                    binding.profileImage.loadImage(user?.data?.userImgUrl.orEmpty())

                    binding.llMyProfile.setOnClickListener {
                        startActivity(
                            Intent(
                                this@ProfileActivity,
                                DetailProfile::class.java
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // handle error
            }
        })
    }
}
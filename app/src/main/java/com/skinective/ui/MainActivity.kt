package com.skinective.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.skinective.R
import com.skinective.utils.Constants

import com.skinective.utils.Constants.Companion.KEY_IS_LOGGED_IN

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)

            val intent = if (isLoggedIn) {
                Intent(this, LoginActivity::class.java) // Jika sudah login, arahkan ke LoginActivity
            } else {
                Intent(this, SignUpActivity::class.java) // Jika belum login, arahkan ke SignUpActivity
            }

            startActivity(intent)
            finish()
        }, 600)
    }
}

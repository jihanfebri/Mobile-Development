package com.skinective.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skinective.R
import com.skinective.databinding.ActivityMainBinding
import com.skinective.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNav()
        binding.btnScan.setOnClickListener {
            startActivity(Intent(this, DetectActivity::class.java))
        }

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE)

        // TODO uncomment this
//        Handler(Looper.getMainLooper()).postDelayed({
//            val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
//
//            val intent = if (isLoggedIn) {
//                Intent(this, LoginActivity::class.java) // Jika sudah login, arahkan ke LoginActivity
//            } else {
//                Intent(this, SignUpActivity::class.java) // Jika belum login, arahkan ke SignUpActivity
//            }
//
//            startActivity(intent)
//            finish()
//        }, 600)
    }

    private fun setupBottomNav() {
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }
}

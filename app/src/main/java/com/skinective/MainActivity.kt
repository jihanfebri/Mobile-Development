package com.skinective

import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val DELAY_MILLIS: Long = 600 // Delay in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)

        val handler = Handler()
        handler.postDelayed({
            // Apply your effect or execute your code here
            // For example, you can change visibility, animate views, etc.
            // Example: fadeInEffect()
        }, DELAY_MILLIS)
    }

    // Example method for applying an effect
    private fun fadeInEffect() {
        // Implement your effect here, for example, fading in a view
        // Example: viewToAnimate.alpha = 0f; viewToAnimate.animate().alpha(1f).setDuration(1000).start()
    }
}

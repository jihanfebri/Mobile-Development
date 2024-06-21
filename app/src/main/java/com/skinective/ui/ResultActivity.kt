package com.skinective.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skinective.databinding.ActivityResultBinding
import com.skinective.network.api.HistoryDetect
import com.skinective.ui.HomeFragment.Companion.loadImage

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bindResultView()
        binding.backButton.setOnClickListener { finish() }
    }

    private fun bindResultView() {
        val data = intent.getParcelableExtra<HistoryDetect>("result")
        if (data != null) {
            binding.tvResult.text = data.diseaseName
            binding.tvRoutineSteps.text = data.diseaseDescription
            binding.tvDescription.text = data.diseaseAction
            if (data.historyImgUrl != null) {
                binding.ivResultImage.loadImage(data.historyImgUrl)
            } else {
                binding.ivResultImage.loadImage(intent.getStringExtra("uri").orEmpty())
            }
        }
    }


}
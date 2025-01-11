package com.skinective.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.skinective.databinding.ActivityHistoryBinding
import com.skinective.network.api.APIService
import com.skinective.network.api.HistoryDetectResponse
import com.skinective.utils.Constants
import com.skinective.utils.Constants.Companion.convertDateIntoDayAndDate
import com.skinective.utils.Constants.Companion.sortedByCreatedAt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getAllHistoryDetection()
        binding.backButton.setOnClickListener { finish() }
    }

    private fun getAllHistoryDetection() {
        val apiService = APIService.getAPIML().getHistoryDetect(Constants.USER?.userId.orEmpty())
        apiService.enqueue(object : Callback<HistoryDetectResponse> {
            override fun onResponse(
                data: Call<HistoryDetectResponse>,
                response: Response<HistoryDetectResponse>
            ) {
                if (response.isSuccessful) {
                    val history = response.body()
                    if (history?.data != null) {
                        val dataRV = history.data.sortedByCreatedAt()
                        binding.rvSkinReport.adapter = HistoryAdapter(dataRV) {
                            startActivity(
                                Intent(
                                    this@HistoryActivity,
                                    ResultActivity::class.java
                                ).apply {
                                    putExtra("result", it)
                                })
                        }
                        binding.rvSkinReport.layoutManager =
                            LinearLayoutManager(this@HistoryActivity)
                        binding.tvLastDetectScan.text =
                            dataRV.firstOrNull()?.createdAt?.convertDateIntoDayAndDate()
                        binding.tvLastDateScan.text =
                            dataRV.firstOrNull()?.createdAt?.convertDateIntoDayAndDate()
                    }
                }
            }

            override fun onFailure(p0: Call<HistoryDetectResponse>, p1: Throwable) {
            }

        })
    }
}
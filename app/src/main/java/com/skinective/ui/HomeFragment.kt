package com.skinective.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.skinective.R
import com.skinective.databinding.FragmentHomeBinding
import com.skinective.network.api.APIService
import com.skinective.network.api.HistoryDetect
import com.skinective.network.api.HistoryDetectResponse
import com.skinective.network.responses.ArticleResponse
import com.skinective.utils.Constants
import com.skinective.utils.Constants.Companion.IS_CLICK_BUTTON_DETECT
import com.skinective.utils.Constants.Companion.sortedByCreatedAt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var dataLastDetected = listOf<HistoryDetect>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDetect.setOnClickListener {
            startActivity(Intent(requireActivity(), DetectActivity::class.java))
        }
        getNewsData()

        binding.tvWelcomeName.text = getString(R.string.welcome_text, Constants.USER?.userFName)
        binding.tvLearnMoreHistory.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }

        binding.imageView.setOnClickListener {
            startActivity(Intent(requireActivity(), ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getHistoryLastDetected()
        if (IS_CLICK_BUTTON_DETECT) {
            binding.rvThreeLastDetect.isVisible = true
            binding.btnDetect.isVisible = false
        } else {
            binding.rvThreeLastDetect.isVisible = false
            binding.btnDetect.isVisible = true
        }
    }

    private fun getHistoryLastDetected() {
        val apiService = APIService.getAPIML().getHistoryDetect(Constants.USER?.userId.orEmpty())
        apiService.enqueue(object : Callback<HistoryDetectResponse> {
            override fun onResponse(
                data: Call<HistoryDetectResponse>,
                response: Response<HistoryDetectResponse>
            ) {
                if (response.isSuccessful) {
                    val history = response.body()
                    if (history?.data != null) {
                        dataLastDetected = history.data.sortedByCreatedAt()
                        binding.rvThreeLastDetect.adapter =
                            HistoryAdapter(dataLastDetected.take(3)) {
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        ResultActivity::class.java
                                    ).apply {
                                        putExtra("result", it)
                                    })
                            }
                        binding.rvThreeLastDetect.layoutManager =
                            LinearLayoutManager(requireContext())
                        binding.tvLastDetectedDisease.text =
                            dataLastDetected.firstOrNull()?.diseaseName
                        binding.tvDetectedNumber.text = dataLastDetected.size.toString()
                    }
                }
            }

            override fun onFailure(p0: Call<HistoryDetectResponse>, p1: Throwable) {
            }

        })
    }

    private fun getNewsData() {
        val apiService = APIService.getAPIConsumer().getArticles()
        apiService.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                data: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    val article = response.body()
                    if (article?.data != null) {
                        article.data.getOrNull(0)?.let {
                            binding.idImgScan.loadImage(it.articleImgUrl.orEmpty())
                            binding.tvArticleTitle.text = it.articleTitle
                            binding.tvArticleDescription.text = it.articleContent?.take(100)
                            binding.cvItem1.setOnClickListener { view ->
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        NewsDetailActivity::class.java
                                    ).apply {
                                        putExtra("article", it)
                                    })
                            }
                        }
                        article.data.getOrNull(1)?.let {
                            binding.idImgScan2.loadImage(it.articleImgUrl.orEmpty())
                            binding.tvArticleTitle2.text = it.articleTitle
                            binding.tvArticleDescription2.text = it.articleContent?.take(100)
                            binding.cvItem2.setOnClickListener { view ->
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        NewsDetailActivity::class.java
                                    ).apply {
                                        putExtra("article", it)
                                    })
                            }
                        }
                        article.data.getOrNull(2)?.let {
                            binding.idImgScan3.loadImage(it.articleImgUrl.orEmpty())
                            binding.tvArticleTitle3.text = it.articleTitle
                            binding.tvArticleDescription3.text = it.articleContent?.take(100)
                            binding.cvItem3.setOnClickListener { view ->
                                startActivity(
                                    Intent(
                                        requireActivity(),
                                        NewsDetailActivity::class.java
                                    ).apply {
                                        putExtra("article", it)
                                    })
                            }
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<ArticleResponse>, p1: Throwable) {
                p1.printStackTrace()
            }

        })

        binding.tvViewMoreNews.setOnClickListener {
            startActivity(Intent(requireActivity(), NewsActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun ImageView.loadImage(url: String) {
            Glide.with(this)
                .load(url)
                .into(this)
        }
    }
}
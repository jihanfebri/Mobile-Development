package com.skinective.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skinective.databinding.ActivityNewsDetailBinding
import com.skinective.network.responses.ArticleItem
import com.skinective.ui.HomeFragment.Companion.loadImage
import com.skinective.utils.Constants.Companion.convertDate

class NewsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsDetailBinding
    private var articleItem: ArticleItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        articleItem = intent.getParcelableExtra("article")
        if (articleItem != null) {
            binding.newsTitle.text = articleItem?.articleTitle
            binding.newsAuthor.text = articleItem?.articleAuthor
            binding.newsDate.text = articleItem?.createdAt?.convertDate()
            binding.newsContent.text = articleItem?.articleContent
            binding.newsImage.loadImage(articleItem?.articleImgUrl.orEmpty())
        }
        binding.backButton.setOnClickListener { finish() }
    }
}
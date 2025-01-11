package com.skinective.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.skinective.databinding.ActivityNewsBinding
import com.skinective.network.api.APIService
import com.skinective.network.responses.ArticleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private var articleResponse: ArticleResponse? = null
    private var adapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getAllNewsData()
        binding.btnBack.setOnClickListener { finish() }

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = articleResponse?.data?.filter {
                    it?.articleTitle?.contains(newText ?: "", ignoreCase = true) == true
                }
                adapter?.updateArticles(filteredList.orEmpty())
                return false
            }
        })
    }

    private fun getAllNewsData() {
        binding.progressBar.isVisible = true
        val apiService = APIService.getAPIConsumer().getArticles()
        apiService.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                data: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                binding.progressBar.isVisible = false
                if (response.isSuccessful) {
                    articleResponse = response.body()
                    if (articleResponse != null) {
                        adapter = ArticleAdapter(articleResponse?.data.orEmpty()) {
                            startActivity(Intent(this@NewsActivity, NewsDetailActivity::class.java).apply {
                                putExtra("article", it)
                            })
                        }
                        binding.rvNews.adapter = adapter
                        binding.rvNews.layoutManager = LinearLayoutManager(this@NewsActivity)
                    }
                }
            }

            override fun onFailure(p0: Call<ArticleResponse>, p1: Throwable) {
                binding.progressBar.isVisible = false
                p1.printStackTrace()
            }

        })
    }
}
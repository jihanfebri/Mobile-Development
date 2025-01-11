package com.skinective.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skinective.databinding.NewsItemListBinding
import com.skinective.network.responses.ArticleItem
import com.skinective.utils.Constants.Companion.convertDate

class ArticleAdapter(
    private var articleList: List<ArticleItem?>,
    private val onClickItem: (ArticleItem) -> Unit = {}
) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateArticles(newArticles: List<ArticleItem?>) {
        this.articleList = newArticles
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(private val binding: NewsItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleItem?) {
            if (article == null) return
            binding.tvArticleTitle.text = article.articleTitle
            binding.tvArticleAuthor.text = article.articleAuthor
            binding.tvArticleDate.text = article.createdAt?.convertDate()
            Glide.with(binding.root)
                .load(article.articleImgUrl)
                .into(binding.idImgScan)
            binding.root.setOnClickListener {
                onClickItem.invoke(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            NewsItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articleList[position])
    }

    override fun getItemCount(): Int = articleList.size
}
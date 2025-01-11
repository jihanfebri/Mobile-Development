package com.skinective.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skinective.databinding.ResultItemBinding
import com.skinective.network.api.HistoryDetect
import com.skinective.ui.HomeFragment.Companion.loadImage
import com.skinective.utils.Constants.Companion.convertDateIntoDayAndDate
import com.skinective.utils.Constants.Companion.convertToTime

class HistoryAdapter(
    private var articleList: List<HistoryDetect?>,
    private val onClickItem: (HistoryDetect) -> Unit = {}
) :
    RecyclerView.Adapter<HistoryAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryDetect?) {
            if (history == null) return
            binding.tvResultTitle.text = history.diseaseName
            binding.tvResultDate.text = history.createdAt?.convertDateIntoDayAndDate()
            binding.ivResultImage.loadImage(history.historyImgUrl.orEmpty())
            binding.tvResultTime.text = history.createdAt?.convertToTime()
            binding.root.setOnClickListener {
                onClickItem.invoke(history)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articleList[position])
    }

    override fun getItemCount(): Int = articleList.size
}
package com.skinective.network.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArticleResponse(

	@field:SerializedName("data")
	val data: List<ArticleItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

@Parcelize
data class ArticleItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("articleTitle")
	val articleTitle: String? = null,

	@field:SerializedName("articleId")
	val articleId: String? = null,

	@field:SerializedName("articleContent")
	val articleContent: String? = null,

	@field:SerializedName("articleImgUrl")
	val articleImgUrl: String? = null,

	@field:SerializedName("articleAuthor")
	val articleAuthor: String? = null
) : Parcelable

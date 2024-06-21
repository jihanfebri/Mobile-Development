package com.skinective.network.responses

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: List<UserItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class UserItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("userPassword")
	val userPassword: String? = null,

	@field:SerializedName("userLName")
	val userLName: String? = null,

	@field:SerializedName("userEmail")
	val userEmail: String? = null,

	@field:SerializedName("userImgUrl")
	val userImgUrl: String? = null,

	@field:SerializedName("isAdmin")
	val isAdmin: Boolean? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("userFName")
	val userFName: String? = null,

	@field:SerializedName("userAge")
	val userAge: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

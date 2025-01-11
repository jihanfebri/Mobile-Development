package com.skinective.network.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val userId: String = "",
    val userEmail: String = "",
    val userFName: String = "",
    val userLName: String = "",
    val userPassword: String = "",
    val userImgUrl: String = "",
    val userAge: Int = 0,
    val isAdmin: Boolean = false,
    val createdAt: String = "",
    val updatedAt: String = ""
) : Parcelable

// Article data model
data class Article(
    val articleId: String,
    val title: String,
    val content: String,
)

// Disease data model
data class Disease(
    val diseaseId: String,
    val diseaseName: String,
    val diseaseAdvice: String
)



//data class LoginRequest(
//    val email: String,
//    val password: String
//)

data class UpdateUserDetailsRequest(
    val email: String?,
    val fName: String?,
    val lName: String?
)

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)

data class CreateArticleRequest(
    val title: String,
    val content: String
)

data class AddDiseaseRequest(
    val diseaseId: Int,
    val diseaseName: String,
    val diseaseAdvice: String
)

data class DetectResponse (
    val message: String,
    val data: DetectData
)

data class DetectData (
    val detectHistoryId: String,
    val diseaseName: String,
    val diseaseAction: String,
    val diseaseDescription: String
)

data class HistoryDetectResponse (
    val message: String,
    val data: List<HistoryDetect>
)

@Parcelize
data class HistoryDetect (
    val detectHistoryId: String? = null,
    val userId: String? = null,
    val diseaseId: String? = null,
    val historyImgUrl: String? = null,
    val createdAt: String? = null,
    val diseaseName: String? = null,
    val diseaseAction: String? = null,
    val diseaseDescription: String? = null
) : Parcelable
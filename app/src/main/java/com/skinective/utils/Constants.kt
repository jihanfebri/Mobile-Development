package com.skinective.utils

import com.skinective.network.api.HistoryDetect
import com.skinective.network.api.User
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class Constants {
    companion object {
        const val SHARED_PREF_NAME = "SkinectivePrefs"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
        var USER: User? = User()
        var IS_CLICK_BUTTON_DETECT = false

        fun String.convertDate(): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val date = inputFormat.parse(this)
            val result = date?.let { outputFormat.format(it) }
            return result.orEmpty()
        }

        fun String.convertDateIntoDayAndDate(): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

            val date = inputFormat.parse(this)
            val result = date?.let { outputFormat.format(it) }
            return result.orEmpty()
        }

        fun String.convertToTime(): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault()

            val date = inputFormat.parse(this)
            val result = date?.let { outputFormat.format(it) }
            return result.orEmpty()
        }

        fun List<HistoryDetect>.sortedByCreatedAt(): List<HistoryDetect> {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return this.sortedByDescending { dateFormat.parse(it.createdAt.orEmpty()) }
        }
    }
}
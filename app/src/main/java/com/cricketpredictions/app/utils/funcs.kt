package com.cricketpredictions.app.utils

import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cricketpredictions.app.MainActivity
import com.cricketpredictions.app.R
import com.cricketpredictions.app.api.App
import com.cricketpredictions.app.model.ShowingAds
import com.cricketpredictions.app.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import java.lang.Exception

lateinit var APP_ACTIVITY: MainActivity

lateinit var APP: App
lateinit var VIEW_MODEL: MainViewModel
lateinit var SHARED_MANAGER: SharedManager
lateinit var isShowingAds: ShowingAds

fun ImageView.loadAndSetImage(url: String) {
    if (url != "") {
        Picasso.get()
            .load(url)
            .fit()
            .centerCrop()
            .into(this)
    }
}

fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    if (fragment != null) {
        val fm = APP_ACTIVITY.supportFragmentManager
        if (!fm.isDestroyed) {
            if (addToBackStack) {
                fm.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.data_container, fragment)
                    .commit()
            } else {
                fm.beginTransaction()
                    .replace(R.id.data_container, fragment)
                    .commit()
            }
        }
    }
}

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun restartActivity() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.finish()
    APP_ACTIVITY.startActivity(intent)
}

fun String.timestampToNormalTime(): String {
    val formattedDate = this.substring(11, 16)
    val hour = formattedDate.split(":")[0]
    return try {
        if (hour.toInt() in 1..11) {
            "$formattedDate AM"
        } else {
            "$formattedDate PM"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        formattedDate
    }
}

fun String.timestampToTime(): String {
    val formattedDate = this.substring(11, 16)
    val hour = formattedDate.substring(0, 2).toInt() + 5
    val minutes = formattedDate.substring(3, 5).toInt() + 30
    return "$hour:$minutes GMT +5:30"
}

fun String.timestampToMonth() = this.substring(5, 7)
fun String.timestampToDay() = this.substring(8, 10)
fun String.timestampToYear() = this.substring(0, 4)

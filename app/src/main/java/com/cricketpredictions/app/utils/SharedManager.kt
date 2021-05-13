package com.cricketpredictions.app.utils

import android.content.Context
import android.content.SharedPreferences

class SharedManager(val context: Context? = null) {

    private var sharedPreferences: SharedPreferences = if (context != null) {
        context.getSharedPreferences("criket_predictions", Context.MODE_PRIVATE)
    } else {
        APP_ACTIVITY.getSharedPreferences("criket_predictions", Context.MODE_PRIVATE)
    }
    private var editor = sharedPreferences.edit()

    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String) = sharedPreferences.getString(key, "")

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String) = sharedPreferences.getInt(key, 0)

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)

}
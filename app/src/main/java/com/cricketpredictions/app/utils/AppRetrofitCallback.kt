package com.cricketpredictions.app.utils

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val RETROFIT_ERROR_KEY = "RETROFIT_ERROR"

class AppRetrofitCallback<T>(val onSuccess: (response: Response<T>) -> Unit) : Callback<T> {

    override fun onFailure(call: Call<T>, t: Throwable) {
        Log.d(RETROFIT_ERROR_KEY, "onFailure: ${t.message}")
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.body() != null && response.code() != 404) {
            onSuccess(response)
        }
    }

}
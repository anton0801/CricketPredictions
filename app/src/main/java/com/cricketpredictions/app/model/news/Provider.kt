package com.cricketpredictions.app.model.news

import com.google.gson.annotations.SerializedName

data class Provider(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("url")
    var url: String = ""
)
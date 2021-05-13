package com.cricketpredictions.app.model.news

import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("title")
    var title: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("description_text")
    var description_text: String = "",
    @SerializedName("updated")
    var publishedDate: String = "",
    @SerializedName("provider")
    var provider: Provider = Provider()
)
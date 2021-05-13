package com.cricketpredictions.app.model.news

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("news")
    var news: List<NewsModel> = ArrayList()
)
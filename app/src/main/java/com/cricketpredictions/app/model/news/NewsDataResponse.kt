package com.cricketpredictions.app.model.news

import com.google.gson.annotations.SerializedName

data class NewsDataResponse(
    @SerializedName("data")
    var data: NewsResponse = NewsResponse()
)
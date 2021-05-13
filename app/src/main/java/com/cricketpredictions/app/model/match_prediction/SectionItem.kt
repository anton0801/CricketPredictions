package com.cricketpredictions.app.model.match_prediction

data class SectionItem(
    var name: String = "",
    var items: List<MatchPrediction> = ArrayList()
)
package com.cricketpredictions.app.model.match_prediction

data class MatchPrediction(
    var name: String = "",
    var human_time: String = "",
    var place: String = "",
    var prediction: String = "",
    var team_a: Team = Team(),
    var team_b: Team = Team(),
    var tips: List<String> = ArrayList(),
    var timestamp: Int = 0
)

data class Team(
    var full_name: String = "",
    var image: String = "",
    var name: String = ""
)
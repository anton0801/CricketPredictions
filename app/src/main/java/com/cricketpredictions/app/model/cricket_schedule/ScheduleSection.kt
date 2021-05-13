package com.cricketpredictions.app.model.cricket_schedule

import com.google.gson.annotations.SerializedName

data class ScheduleSection(
    var name: String = "",
    var items: List<ScheduleItem> = ArrayList()
)

data class ScheduleItem(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("venue")
    var place: String = "",
    @SerializedName("title")
    var desription: String = "",
    @SerializedName("start_date")
    var startTime: StartTime = StartTime()
)

data class StartTime(
    var iso: String = ""
)
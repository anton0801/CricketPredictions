package com.cricketpredictions.app.model.cricket_schedule

import com.google.gson.annotations.SerializedName

data class ScheduleDataResponse(
    @SerializedName("data")
    var data: ScheduleResponse = ScheduleResponse()
)

data class ScheduleResponse(
    @SerializedName("months")
    var months: List<ScheduleMont> = ArrayList(),
    @SerializedName("current_month")
    var currentMonth: String = "",
    @SerializedName("next_month")
    var nextMonth: String = ""
)

data class ScheduleMont(
    @SerializedName("days")
    var days: List<ScheduleDay> = ArrayList()
)

data class ScheduleDay(
    @SerializedName("matches")
    var matches: List<ScheduleItem> = ArrayList(),
    @SerializedName("day")
    var day: Int = 0
)
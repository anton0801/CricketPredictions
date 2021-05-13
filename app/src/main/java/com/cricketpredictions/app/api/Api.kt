package com.cricketpredictions.app.api

import com.cricketpredictions.app.model.AuthResponse
import com.cricketpredictions.app.model.cricket_schedule.ScheduleDataResponse
import com.cricketpredictions.app.model.news.NewsDataResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("v2/auth/")
    fun auth(
        @Field("access_key") accessKey: String,
        @Field("secret_key") secret_key: String,
        @Field("app_id") app_id: String,
        @Field("device_id") device_id: String
    ): Call<AuthResponse>

    @GET("v2/news_aggregation/")
    fun getNews(@Query("access_token") accessToken: String): Call<NewsDataResponse>

    @GET("v2/schedule/")
    fun getCricketSchedule(@Query("access_token") accessToken: String): Call<ScheduleDataResponse>

    @GET("v2/schedule/")
    fun getCricketSchedule(
        @Query("access_token") accessToken: String,
        @Query("date") date: String
    ): Call<ScheduleDataResponse>

}
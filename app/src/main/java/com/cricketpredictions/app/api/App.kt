package com.cricketpredictions.app.api

import android.app.Application
import com.amplitude.api.Amplitude
import com.onesignal.OneSignal
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://rest.cricketapi.com/rest/"
const val YANDEX_METRICA_API_KEY = "414f7a9f-6054-4b5a-8226-5d81b89dd47e"

class App : Application() {

    private lateinit var retrofit: Retrofit
    private lateinit var api: Api

    override fun onCreate() {
        super.onCreate()

        // yandex metrica
        val config = YandexMetricaConfig.newConfigBuilder(YANDEX_METRICA_API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    HttpLoggingInterceptor.Level.BODY
            })
            .writeTimeout(60000, TimeUnit.SECONDS)
            .readTimeout(60000, TimeUnit.SECONDS)
            .connectTimeout(60000, TimeUnit.SECONDS)
            .callTimeout(60000, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        api = retrofit.create(Api::class.java)
        // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.setNotificationWillShowInForegroundHandler {
            Amplitude.getInstance().logEvent("Notification Received")
        }
        OneSignal.setNotificationOpenedHandler {
            Amplitude.getInstance().logEvent("Notification Clicked")
        }
    }

    fun getApi() = api

    companion object {
        private val ONESIGNAL_APP_ID = "58da5786-400f-429b-9094-ef27931b05dd"
    }

}